import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/******************************************************/
/*** Purpose: Test class to illustrate Search class ***/
/***                                                ***/
/*** Author: Jason Steggles                         ***/
/*** Date: 16/10/2018                               ***/
/******************************************************/

public class TestSearch
{
   public static void main(String[] args) 
   {  
	   
	   /***********************************************/
       /*** Reading both files and storing the data ***/
       /***********************************************/
	   
	   int[] search1 = new int[10];
	   try{
		   int i = 0;
	       /** Set up file for reading **/
	       FileReader reader = new FileReader("search1.txt");
	       Scanner in = new Scanner(reader);
	       while(in.hasNextInt()){
	    	   search1[i] = in.nextInt();
	    	   i++;
	       }
	   }catch (IOException e){
	       System.out.println("Error processing file " + "search1.txt");
	   }
	   int[] search2 = new int[10];
	   try{
		   int i = 0;
	       /** Set up file for reading **/
	       FileReader reader = new FileReader("search2.txt");
	       Scanner in = new Scanner(reader);
	       while(in.hasNextInt()){
	    	   search2[i] = in.nextInt();
	    	   i++;
	       }
	   }catch (IOException e){
	       System.out.println("Error processing file " + "search2.txt");
	   }
	   
	   /**************************************************/
       /*** Using search 1 data 1 for all 3 algorithms ***/
       /**************************************************/
	   
	   Search S = new Search(100);
	   
       /** Read in test data **/
       S.readFileIn("data1.txt");
    
       /** Display data array **/
       S.displayData(20, "Data Array from data1.txt");
       System.out.println("");
       
       /*************************/
       /*** Sequential Search ***/
       /*************************/
	   System.out.println("---Starting Sequential Search---");
       
       /** Variables for calculating the average **/
       int avgS = 0;
       int avgF = 0;
       int avg = 0;
       int noOfS = 0;
       int noOfF = 0;
       
       /** Running through the file of values to search for **/
       for(int i = 0; i < search1.length; i++){
    	   int newSearch = search1[i];
    	   int pos = S.seqSearch(newSearch);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compSeq + " comparisons");
	    	   avgS += S.compSeq;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compSeq + " comparisons");
	    	   avgF += S.compSeq;
	    	   noOfF++;
	       }
    	   S.compSeq = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---Sequential Search Results---");
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");
       
       /*********************/
       /*** Binary Search ***/
       /*********************/
       System.out.println("---Starting Binary Search---");
       /** Variables for calculating the average **/
       avgS = avgF = avg = noOfS = noOfF = 0;
       
       /** Running through the file of values to search for **/
       for(int i = 0; i < search1.length; i++){
    	   int newSearch = search1[i];
    	   int pos = S.binSearch(newSearch, 0, 99);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compBin + " comparisons");
	    	   avgS += S.compBin;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compBin + " comparisons");
	    	   avgF += S.compBin;
	    	   noOfF++;
	       }
    	   S.compBin = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---Binary Search Results---");
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");
       
       /******************/
       /*** New Search ***/
       /******************/
       System.out.println("---Starting New Search---");
       /** Running through the file of values to search for **/
       int lowestComp = 99;
       int lowestInc = 0;
       for(int j = 1; j < 100; j++){
    	   int avgTest = 0;
    	   int avgInc = 0;
	       for(int i = 0; i < search1.length; i++){
	    	   int newSearch = search1[i];
	    	   S.newSearch(newSearch, j);
	    	   avgTest += S.compNew;
	    	   avgInc++;
	    	   S.compNew = 0;
	       }
	       avgTest = avgTest/avgInc;
	       if(avgTest < lowestComp){
    		   lowestComp = avgTest;
    		   lowestInc = j;
    	   }
       }
       
       /** Variables for calculating the average **/
       avgS = avgF = avg = noOfS = noOfF = 0;
       
       for(int i = 0; i < search1.length; i++){
    	   int newSearch = search1[i];
    	   int pos = S.newSearch(newSearch, lowestInc);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compNew + " comparisons");
	    	   avgS += S.compNew;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compNew + " comparisons");
	    	   avgF += S.compNew;
	    	   noOfF++;
	       }
    	   S.compNew = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---New Search Results---");
       System.out.println("Best case for m: " + lowestInc);
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");
       
       /**************************************************/
       /*** Using search 2 data 2 for all 3 algorithms ***/
       /**************************************************/
       
       S = new Search(1000);
       
       /** Read in test data **/
       S.readFileIn("data2.txt");
    
       /** Display data array **/
       S.displayData(20, "Data Array from data2.txt");
       System.out.println("");
       /*************************/
       /*** Sequential Search ***/
       /*************************/
       System.out.println("---Starting Sequential Search---");
       /** Variables for calculating the average **/
       avgS = avgF = avg = noOfS = noOfF = 0;
       
       /** Running through the file of values to search for **/
       for(int i = 0; i < search2.length; i++){
    	   int newSearch = search2[i];
    	   int pos = S.seqSearch(newSearch);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compSeq + " comparisons");
	    	   avgS += S.compSeq;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compSeq + " comparisons");
	    	   avgF += S.compSeq;
	    	   noOfF++;
	       }
    	   S.compSeq = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---Sequential Search Results---");
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");
       
       /*********************/
       /*** Binary Search ***/
       /*********************/
       System.out.println("---Starting Binary Search---");
       /** Variables for calculating the average **/
       avgS = avgF = avg = noOfS = noOfF = 0;
       
       /** Running through the file of values to search for **/
       for(int i = 0; i < search2.length; i++){
    	   int newSearch = search2[i];
    	   int pos = S.binSearch(newSearch, 0, 999);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compBin + " comparisons");
	    	   avgS += S.compBin;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compBin + " comparisons");
	    	   avgF += S.compBin;
	    	   noOfF++;
	       }
    	   S.compBin = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---Binary Search Results---");
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");

       /******************/
       /*** New Search ***/
       /******************/
       System.out.println("---Starting New Search---");
       /** Running through the file of values to search for **/
       lowestComp = 999;
       lowestInc = 0;
       for(int j = 1; j < 1000; j++){
    	   int avgTest = 0;
    	   int avgInc = 0;
	       for(int i = 0; i < search2.length; i++){
	    	   int newSearch = search2[i];
	    	   S.newSearch(newSearch, j);
	    	   avgTest += S.compNew;
	    	   avgInc++;
	    	   S.compNew = 0;
	       }
	       avgTest = avgTest/avgInc;
	       if(avgTest < lowestComp){
    		   lowestComp = avgTest;
    		   lowestInc = j;
    	   }
       }
       
       /** Variables for calculating the average **/
       avgS = avgF = avg = noOfS = noOfF = 0;
       
       for(int i = 0; i < search2.length; i++){
    	   int newSearch = search2[i];
    	   int pos = S.newSearch(newSearch, lowestInc);
    	   if(pos > -1){
    		   /** Value found **/
    		   System.out.println("Found " + newSearch + " at location: " + pos + " using " + S.compNew + " comparisons");
	    	   avgS += S.compNew;
	    	   noOfS++;
	       } else {
	    	   /** Value not found **/
	    	   System.out.println("Failed to find " + newSearch + " and used " + S.compNew + " comparisons");
	    	   avgF += S.compNew;
	    	   noOfF++;
	       }
    	   S.compNew = 0;
       }
       avg = (avgS + avgF) / (noOfS + noOfF);
       avgS /= noOfS;
       avgF /= noOfF;
       System.out.println("\n---New Search Results---");
       System.out.println("Best case for m: " + lowestInc);
       System.out.println("Average Success Comparisons: " + avgS);
       System.out.println("Average Failed Comparisons: " + avgF);
       System.out.println("Average Comparisons: " + avg + "\n");
       
   }
   
}