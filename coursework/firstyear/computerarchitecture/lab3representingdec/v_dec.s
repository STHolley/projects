	.global v_dec

v_dec:	push	{R0-R7}
	mov	R3,R0		@ Creates a copy of the input and stores it to R3 to be manipulated
	mov	R8,R0		@ Creates a second copy for comparisons at the end
	mov	R9,#2		@ Checks how many digits are at the start of the hundereds chunk, starts at 2
	mov	R2,#1		@ Sets the write size to 1
	mov	R0,#1		@ Sets the display register to R1
	mov	R7,#4		@ Linux command for printing
	cmp	R3,#0		@ Compares the value of the input to check if it is positive or negative
	bge	absval		@ If it is positive skip the next few steps
	ldr	R1,=obrac	@ Print a open parenthasis at the start
	svc	0
	rsb	R3,R3,#0	@ Sets R3 to its absolute value and overwrites itself

absval:	cmp	R3,#10		@ Test whether only the ones column is needed
	blt	onecol		@ Branch if necessary
	ldr	R6,=pow10+8	@ Sets the pointer to the hundereds column in the data list

high10:	ldr	R5,[R6],#4	@ Load the next highest power of 10
	sub	R9,#1		@ Removes 1 from the checker for the lenght of the hundereds chunk
	cmp	R9,#0		@ Checks to see if it has reached a new hundereds chunk
	addlt	R9,#3		@ Resets if necessary
	cmp	R3,R5		@ Test if the highest power of 10 has been reached
	bge	high10		@ Re-iterate if the previous statement was not met
	sub	R6,#8		@ Stepped 2 integers too far

nxtdec:	ldr	R1,=dig-1	@ Point to one byte before the numerical ascii string
	ldr	R5,[R6],#-4	@ Load the next lower power of 10

mod10:	add	R1,#1		@ Set R1 to pointing at the next digit from 0-9
	subs	R3,R5		@ Do a count to find the correct digit
	bge	mod10		@ Keep counting down to the current value
	addlt	R3,R5		@ Counted one too many
	svc	0		@ Print the current value
	add	R9,#1		@ Adds 1 to R9 which is responsible for placing the commas
	cmp	R9,#3		@ Checks if R9 has reached 3 (the gap size for the commas)
	ldr	R1,=comm	@ Load a comma to print
	svceq	0		@ Print a comma if R9 is 3
	subeq	R9,#3		@ Reset R3 if a comma was printed
	cmp	R5,#10		@ Test if the ones column was reached
	bgt	nxtdec		@ If not in the ones column keep printing

onecol:	ldr	R1,=dig		@ Pointer to the ascii for the numbers
	add	R1,R3		@ Point to the corret number
	svc	0		@ Print it

	ldr	R1,=cbrac	@ Load a closing parenthesis
	cmp	R8,#0		@ Check if the original number was negative
	svcmi	0		@ Print if the number was negative

	pop	{R0-R7}		@ Restore register contents
	bx	LR		@ Return to calling program

	.data
pow10:	.word	1		@ 
	.word	10		@ 
	.word	100		@ 
	.word	1000		@ 
	.word	10000		@ 
	.word	100000		@ 
	.word	1000000		@ 
	.word	10000000	@ 
	.word	100000000	@ 
	.word	1000000000	@ 
	.word	0x7FFFFFF	@ 
dig:	.ascii	"0123456789"	@ 
msign:	.ascii	"-"		@ 
obrac:	.ascii	"("		@ 
cbrac:	.ascii	")"		@ 
comm:	.ascii	","		@ 
	.end
