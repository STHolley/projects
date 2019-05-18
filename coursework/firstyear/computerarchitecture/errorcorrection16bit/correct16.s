	.global correct16
correct16:
	push	{R1-R8}
	mov	R2,#1		@Constant number 1 for comparison
	mov	R3,R0		@Duplicate of R0
	mov	R6,#0		@Calculates position of error
loop1:	and	R1,R2,R3,LSR R4	@Getting individual bits
	add	R5,R1		@Counts all the 1s in the stream
	add	R4,#2		@Increments the position
	cmp	R4,#20		@Checking to see if there are any bits left to check
	blt	loop1		@Looping if there are still bits to check
	tst	R5,#1		@Test to see if the value is odd or even
	addne	R6,#1		@Add 1 to the bit location if it is odd parity

	mov	R5,#0		@Reset register 5
	mov	R4,#2		@Reset Register 4 and adjust the starting position
loop2:	and	R1,R2,R3,LSR R4	@Since it is in blocks of 2
	add	R5,R1		@get 2 blocks and then skip 2
	add	R4,#1
	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#2
	cmp	R4,#21		@only check if there are any blocks after as pulling a 0 out won't affect anything
	blt	loop2
	tst	R5,#1
	addne	R6,#2		@adds 1 to the counter if it is even

	mov	R5,#0
	mov	R4,#-2		@starts slightly backwards but can still work
loop3:	and	R1,R2,R3,LSR R4	@blocks of 4 instead of 2
	add	R5,R1
	add	R4,#1
	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#1
	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#1
	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#4
	cmp	R4,#21
	blt	loop3
	tst	R5,#1
	addne	R6,#4

	mov	R5,#0
	mov	R4,#6
loop4:	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#1
	cmp	R4,#14
	blt	loop4
	tst	R5,#1
	addne	R6,#8

	mov	R5,#0
	mov	R4,#0
loop5:	and	R1,R2,R3,LSR R4
	add	R5,R1
	add	R4,#1
	cmp	R4,#6
	blt	loop5
	tst	R5,#1
	addne	R6,#8

	mov	R7,#21		@Set R7 to 21
	sub	R7,R6		@Take the value of R6 away from the heart
	mov	R6,R7
	mov	R7,#0b1		@Create a blank binary string for inverting a binary option
	mov	R7,R7,LSL R6	@Shifts the binary to the location of the error bit
	eor	R8,R7,R3	@Exclusive or to exchange the value of the error bit with a correction

	mov	R2,#1		@Value #1
	mov	R3,R8		@Copy of the corrected data word
	mov	R4,#0		@Location of the 21 bit data word
	mov	R6,#0		@The location of where to store bits in the new data
condense:
	and	R1,R2,R3,LSR R4	@
	cmp	R4,#5
	addeq	R4,#1
	beq	condense	@Skip the fifth bit
	cmp	R4,#13
	addeq	R4,#1
	beq	condense	@Skip the 13th bit
	cmp	R4,#17
	addeq	R4,#1
	beq	condense	@Skip the 17th bit
	LSL	R1,R6		@Pad out the current value with zeroes until  merge completed
	add	R6,#1
	add	R4,#1
	cmp	R6,#16
	blt	condense	@Keep looping untl perfect

	mov	R0,R8		@Sets the output to be the returned 16 bit corrected data word
	pop	{R1-R8}

	bx	LR

