@Binary input stored in R3 instead of R0
@to avoid printing conflict

	.global	v_bin
v_bin:	ldr	R1,=pre	@Loading the 0b text to start
	mov	R2,#2	@Setting the length of the printed text to 2
	mov	R0,#1	@Printing register set to 1
	mov	R7,#4	@Constant for printing strings
	svc	0	@Service command to write a string
	push	{R0-R7}
	@Checking if the input is less than 32 bits so that it can fit in a register
	mov	R2,#31	@Set R2 back to 31, the length of the total data size
	sub	R6,R2,#1	@sub 1 from 31 and store it in R6
	cmp	R6,#31	@Comparing R6 to 31 to see if it is larger
	movhi	R6,#0	@Automatically prints 0 if it is too high
	mov	R4,#1	@Stores the value 1 for comparison
	ldr	R5,=dig	@Sets R5 to 01 from the data area to be printed
	mov	R2,#1	@Sets the size of the print to 1
	mov	R0,#1	@Sets the display to 1
	mov	R7,#4	@Constant for printing strings
	mov	R6,#32	@Sets R6 to 32 as it will instantly decrease

@The loop continues until the first 1 (MSB) is found

floatz:	subs	R6,#1	@Subs 1 from R6 which starts at 32, to make it 31 to start with
	and	R1,R4,R3,lsr R6	@Checks to see if the current bit is a 1 or 0
	cmp	R6,#0	@checks to see if there are any bits left to check
	bne	noval	@if all bits were a 0 then go to print a single "0"
	cmp	R1,#1	@If the current bit is a 1, set R1 to 1
	bne	floatz	@If the comparison above was a 0, loop around to the next bit
	b	nxtbit
@As soon as the MSB is found, nextbit is run
@if no values are "1", a single 0 is printed

noval:	ldr	R1,=dig	@sets the print register to "01" with a length of 1 to make it print "0"
	svc	0	@print "0"
	b	endof	@branch to the end

nxtbit:	and	R1,R4,R3,lsr R6	@Checks to see if it is a 1 or 0
	add	R1,R5	@Selects the string value '1' or '0' from R5 which is the digit data
	svc	0	@Prints the value
	subs	R6,#1	@Moves on to the next bit in the sequence
	bge	nxtbit	@Loops around nxtbit

endof:	pop	{R0-R7}	@Restores register contents
	bx	LR	@Returns to the calling program

	.data
dig:	.ascii	"01"	@String values of 1 and 0
pre:	.ascii	"0b"	@Contains the preliminary '0b' at the start of the print
