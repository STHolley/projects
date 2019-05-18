#include "RFTheader.h"
#include <arpa/inet.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <stdbool.h>
#include <string.h>
 #include <time.h> 

/* error message format for fatalerror - Source Nick Cook*/
static const char *ERR_FMT = "%s:%d - %s, errno %d: %s\n";

/**************************************************************************
**************************************************************************
 All functions' signature
**************************************************************************
**************************************************************************/
int checksum(char *content, int len);
FILE * check_file(char *fileName);
int file_size(FILE *file);
void fatalerror(int line, char *msg);
bool isCorrupted(float prob);

struct sockaddr_in create_server_data(int sockfd , char* host, int port);
void send_meta_data(FILE *file,char *output, int sockfd, struct sockaddr_in server);
void send_file_normal(FILE *fp,int sockfd, struct sockaddr_in server);
void send_file_with_timeout(FILE *fp,int sockfd, struct sockaddr_in server, float prob_loss);

/**************************************************************************
**************************************************************************
 Functions that you need to implement
**************************************************************************
**************************************************************************/
/* arguments: socket id, host name, port number 
   return: The internet socket address of the server */ 
struct sockaddr_in create_server_data(int sockfd , char* host, int port)
{  
    struct sockaddr_in server; 
    /* Replace the following with function implementation */ 
	char echoBuffer[256];
	if(sockfd <0)
	{
		perror("SERVER: Failed to open socket"); 
		close (sockfd );
		exit(1);
	}
	server.sin_family = AF_INET;           /* Internet address family */
	server.sin_addr.s_addr = htonl(host);
	server.sin_port = htons(port); /* Server port */
	return server;
} 
  
/* arguments: input file, the name of the output file, socket id, 
              port number, structure containing the server internet address */ 
void send_meta_data(FILE *file,char *output, int sockfd, struct sockaddr_in server)
{ 
	meta_data mData;
	strcpy(mData.name, output);
	mData.size = file_size(file);
    /* Replace the following with function implementation */ 
    if (sendto(sockfd, &mData, sizeof(mData), 0, (struct sockaddr *) &server, sizeof(server))<=0)
	{
       perror("sendto failed");
       exit(1);
    }
} 
  
/* arguments: input file, socket id, internet socket address of the receiver  */
void send_file_normal(FILE *fp,int sockfd, struct sockaddr_in server)
{ 
   /* Replace the following with function implementation */ 
    int serverLen = sizeof(server);
	int packetSize = 15; //With \0
	int len = file_size(fp);
	char *fileData = malloc(sizeof(char) * len);
	fseek(fp, 0L, SEEK_SET);
	int count = ((len+1) / packetSize) + ((len+1) % 2);
	printf("Number of packets: %d\n", count);
	fgets(fileData, len+1, fp);
	for(int i = 0; i < count; i++){
		int size = strlen(fileData)+1;
		if(i != count-1){
			size = packetSize;
		}
		char *load = malloc(sizeof(char) * size);
		strncpy(load, fileData, size);
		segment seg;
		strcpy(seg.payload, load);
		seg.size = size;
		printf("Packet id: %d\tPayload Sent: %s\t Size: %d\n", i, seg.payload, size);
		seg.sq = i%2;
		seg.type = TYPE_DATA;
		seg.checksum = checksum(load, size);
		fileData += size;
		free(load);
		//SEND
		
		if(sendto(sockfd, &seg, sizeof(seg), 0, (struct sockaddr *) &server, sizeof(server)) <= 0){
			perror("sendto failed");
			exit(1);
		}
		
		segment rec_ack;
		rec_ack.type = TYPE_ACK;
		
		if(recvfrom(sockfd, &rec_ack, sizeof(seg), 0, (struct sockaddr *) &server, &serverLen) < 0 ){
			perror("rec failed");
			exit(1);
		}
		
	}
	fileData -= len;
}


 /* arguments: input file, socket id, internet socket address of the receiver  */
void send_file_with_timeout(FILE *fp,int sockfd, struct sockaddr_in server, float prob_loss)
{ 
    /* Replace the following with function implementation */ 
    int serverLen = sizeof(server);
	int packetSize = 15; //With \0
	int len = file_size(fp);
	char *fileData = malloc(sizeof(char) * len);
	fseek(fp, 0L, SEEK_SET);
	int count = ((len+1) / packetSize) + ((len+1) % 2);
	printf("Number of packets: %d\n", count);
	fgets(fileData, len+1, fp);
	for(int i = 0; i < count; i++){
		int size = strlen(fileData)+1;
		if(i != count-1){
			size = packetSize;
		}
		char *load = malloc(sizeof(char) * size);
		strncpy(load, fileData, size);
		segment seg;
		strcpy(seg.payload, load);
		seg.size = size;
		seg.sq = i%2;
		seg.type = TYPE_DATA;
		
		int temp = size;
		
		if(isCorrupted(prob_loss) == true){
			temp = 0;
		}
		
		seg.checksum = checksum(load, temp);
		fileData += size;
		free(load);
		//SEND
		
		if(sendto(sockfd, &seg, sizeof(seg), 0, (struct sockaddr *) &server, sizeof(server)) <= 0){
			perror("sendto failed");
			exit(1);
		}
		
		segment rec_ack;
		rec_ack.type = TYPE_ACK;
		
		struct timeval timeout;
		fd_set sel_time_out;
		
		FD_ZERO(&sel_time_out);
		FD_SET(sockfd, &sel_time_out);
		
		timeout.tv_sec = 2;
		timeout.tv_usec = 0;
		printf("--------------------------------------------------\n");
		printf("SENDER: Sending segment: (Sq:%d, size:%d, checksum:%d, content:%s)\n", seg.sq, seg.size, seg.checksum, seg.payload);
		printf("SENDER: Waiting for an ACK\n");
		if(select(sockfd + 1, &sel_time_out, NULL, NULL, &timeout) == 0){
			printf("SENDER: Packet may be corrupted. Attempting to re-send\n");
			fileData -= size;
			i--;
		} else {
			if(recvfrom(sockfd, &rec_ack, sizeof(seg), 0, (struct sockaddr *) &server, &serverLen) < 0){
				perror("rec failed");
				exit(1);
			}
			printf("SENDER: ACK sq:%d RECIEVED\n", seg.sq);
		}
		printf("--------------------------------------------------\n");
	}
	
	fileData -= len;
}

/**************************************************************************
**************************************************************************
                  The main function  
**************************************************************************
**************************************************************************/
int main(int argc,char *argv[])
{
	int sockfd ;
	FILE *file;
    struct sockaddr_in server;
	
	/* 	accept input from console in the form of 
	./client inputFile outputFile localhost portNumber 	*/
	if (argc != 5) {
		fprintf(stderr, "Usage: <inputFile> <outputFile> <localhost> <portNumber> \n");
		exit(1);
	}
		
	/* check the file actually exist	*/
	file=check_file(argv[1]);
	printf ("----------------------------------------------------\n");
	printf ("SENDER: File (%s) exists with a size of (%d) bytes\n",argv[1], file_size(file));
	printf ("----------------------------------------------------\n");
	printf ("----------------------------------------------------\n");

	/* create a socket */
	sockfd =socket(AF_INET,SOCK_DGRAM,0);
	if(sockfd <0)
	{
		perror("SENDER: Failed to open socket"); 
		close (sockfd );
		exit(1);
	}
	printf ("SENDER: Socket is created\n");

	/* create a connection */
	server = create_server_data(sockfd, argv[3], atoi(argv[4]));
 	printf ("SENDER: Server data is prepared\n");
 	
	/* Send meta data */
	send_meta_data(file,argv[2],sockfd,server);
	
	/* transmission options */
	int choice=0;
    float loss=0;
	printf ("----------------------------------------------------\n");
	printf ("----------------------------------------------------\n");
	printf("Choose one of the following options (1 or 2):\n ");
	printf("1. Normal transmission (no data segment is lost) \n ");
	printf("2: Transmission with time-out capabilities\n ");
    scanf("%d", &choice);
    switch(choice)
    {
        case 1:
            send_file_normal(file,sockfd, server);
            break;
        
        case 2:
			printf("Enter the probability of a corrupted checksum (between 0 and 1):\n ");
			scanf("%f", &loss);
			send_file_with_timeout(file,sockfd, server, loss);
            break;

         default:
            printf("Error! enter 1 or 2 \n");
    } 

	printf("SENDER: File is sent\n");
	
	/* Close the file */
	fclose(file);
	/* Close the socket */
	close (sockfd );
 	return 0;
} 

/*************************************************************************
**************************************************************************
 These functions are implemented for you .. Do NOT Change them
**************************************************************************
**************************************************************************/


/* calculate the segment checksum by adding the payload */
int checksum(char *content, int len)
{
	int i;
	int sum = 0;
	for (i = 0; i < len; i++)
		sum += (int)(*content++);
	return sum;
}

/* check if the input file does exist */
FILE * check_file(char *fileName)
{
	FILE *file = fopen(fileName, "rb");
	if(!file) {
		perror("SENDER: File does not exists");
 		fclose(file);
		exit(1);
	}
	return file;
}

/* return file size */
int file_size(FILE *file)
{
	fseek(file, 0L, SEEK_END);
	int size = ftell(file);
	return size;
}

void fatalerror(int line, char *msg) {
    printf("Line %d : %s\n", line, msg);
    exit(0);
}

/* decide whether the segment is corrupted or not given a certain probability */
bool isCorrupted(float prob)
{ 
    if(((float)(rand())/(float)(RAND_MAX)) < prob)
	{
	return true;
	}
    else return false;
}
/*************************************************************************** 
 *************************************************************************** 
 *************************************************************************** 
 ***************************************************************************/

