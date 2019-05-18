	.global	v_hex		@ Provide the starting address to the linker
v_hex:	push	{R0-R7}		@ Saves the states of the cache regesiters
	mov	R4,#0b1111	@ Stores a complete binary nibble in register 4
	mov	R6,R2,lsl #2	@ Load the number of bits to display
	mov	R3,R0		@ Set R3 to the hexidecimal input
	ldr	R1,=pre		@ Sets R1 to the prefix to append to the start
	mov	R2,#2		@ Sets the print length to 2 to print 0x
	mov	R0,#1		@ Sets the print register to register 1
	mov	R7,#4		@ Linux code for printing
	svc	0		@ Executes the print

	ldr	R5,=dig		@ Sets register 5 to the ASCII for the hex
	mov	R2,#1		@ Sets the print length to 1

	cmp	R6,#32		@ Compares the length of R6, the register containing the hex, to the limit of the register
	movhi	R6,#0		@ If it is too large, set it to 0
	subs	R6,#4		@ Reduce the size by 4 to check the first nibble
	mov	R8,#0		@ A boolean to check if the first MSB is found
	bge	hex_bin		@ Branches to the hex_bin loop if R6 - 4 is > 0

	mov	R6,#28		@ If it ended up negative for any reason set the size back to 28
hex_bin:ands	R1,R4,R3,lsr R6	@ Check to see the value of the current hex number being checked
	movne	R8,#1		@ If the current number is 1 or more then set R8 to true as the MSB has been found
	add	R1,R5		@ Set the bit to be printed to the location in the ASCII
	cmp	R8,#1		@ Check if R8 is true
	svceq	0		@ If true, print all 0s that come up
	subs	R6,#4		@ Takes a nibble away from the total
	bge	hex_bin		@ Loops if there are still nibbles to check
	cmp	R8,#0		@ If no MSB was found,
	svceq	0		@ Prints a singular "0"
	pop	{R0-R7}		@ Clears the cache registers
	bx	LR		@ Branches back to the calling function

	.data
dig:	.ascii	"0123456789"	@ ASCII to store the numerical hex values
	.ascii	"ABCDEF"	@ ASCII to store the alphabetical hex values
pre:	.ascii	"0x"		@ ASCII to store the prefix of 0x
	.end
