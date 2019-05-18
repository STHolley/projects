	.global generate16
generate16:
	push	{R1-R8}
	mov	R1,#1
	mov	R8,#0
	mov	R7,#0
@Grab every bit from the input individually
@pos 5, 13, 17, 19 and 20 checkbits
	mov	R3,#-1
place:	add	R3,#1
	and	R2,R1,R0,LSR R3

@calculate the bit value to generate the 21 bit data with blank checkbits
calcval:
	mov	R4,R3
	add	R4,#1
	cmp	R4,#6
	addge	R4,#1
	cmp	R4,#14
	addge	R4,#1
	cmp	R4,#18
	addge	R4,#1
	mov	R5,#1
	cmp	R5,R4
	beq	insert
	sub	R4,#1
@calculate the power of 2 that the bit corresponds to
pow:	add	R5,R5
	sub	R4,#1
	cmp	R4,#0
	bne	pow

@Add the value to the running total
insert:	cmp	R2,#1
	addeq	R8,R5

@Move on to the next bit if there is one
	cmp	R3,#15
	bne	place


@Making the checkbits, R8 = 21bit
	mov	R2,#1	@checkbit size/gap
startBit:
	mov	R1,#21	@position from left
	sub	R1,R2
	mov	R3,#1	@value #1
	mov	R4,#0	@#1 tally for even parity
	mov	R7,#21
	sub	R7,R7,R1	@Getting position from the right

	mov	R6,R2	@Make a copy of the checkbit

check:
	and	R5,R3,R8,LSR R1	@retrieveing bit data
	cmp	R5,#1	@Checking if the bit is 1 or 0
	addeq	R4,#1	@Add 1 to the tally if the value is 1

	sub	R1,#1	@Move the pointer to the next bit

	sub	R6,#1	@Calculating when the next gap starts
	cmp	R6,#0	@Check if the gap starts
	beq	skip	@For the gaps in the sequence that aren't checked

	cmp	R1,#0	@Check to see if there is a next bit available
	bge	check	@Move to the next bit in the sequence

@All bits checked for this checkbit
@Calculate checkbit for even parity
checkEven:
	and	R4,R3,R4,LSR#0	@Evenness determined by smallest bit
	cmp	R4,#1		@Compares to 1. If eq: odd, if ne: even
	beq	calcBit		@is odd. Update checkbit to 1 in correct position
	bne	nextBit		@is Even

nextBit:
	add	R2,R2	@Double R2 to make the next checkbit
	cmp	R2,#32	@Check if the checkbit has gone too far
	bne	startBit	@Move on to the next checkbit
	b	end	@Go to the end if all checkbits are done

calcBit:	@R2 = checkbit number
	cmp	R2,#1	@checkbit val 1		@Check if the checkbit is 1
	addeq	R8,#0b100000000000000000000	@Turn the first checkbit 1
	beq	nextBit				@Branch back if checkbit updated
	cmp	R2,#2				@Check if the checkbit is 2
	addeq	R8,#0b010000000000000000000	@Turn the second checkbit 1
	beq	nextBit				@Branch back if checkbit updated
	cmp	R2,#4				@Check if the checkbit is 4
	addeq	R8,#0b000100000000000000000	@Turn the third checkbit 1
	beq	nextBit				@Branch back if checkbit updated
	cmp	R2,#8				@Check if the checkbit is 8
	addeq	R8,#0b000000010000000000000	@Turn the fourth checkbit 1
	beq	nextBit				@Branch back if checkbit updated
	add	R8,#0b000000000000000100000	@The remaining checkbit is 16, set to 1
	b	nextBit				@Branch back

skip:	sub	R1,#1	@Continue moving the pointer
	add	R6,#1	@Fill the gap
	cmp	R1,#0	@Check if at end of input
	blt	checkEven	@If at end, go to end
	cmp	R6,R2	@Check if the sequence starts again
	beq	check	@Go back if it does
	b	skip	@Keep skipping if not

end:
	mov	R0,R8
@End
	pop	{R1-R8}
	bx	LR
