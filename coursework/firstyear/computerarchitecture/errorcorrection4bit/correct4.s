	.global	correct4

correct4:
	push	{R1-R12}
	mov	R5,#1
	mov	R6,#0

@Storing the bits
	and	R1,R5,R0,LSR#0	@RX0 binary
	and	R2,R5,R0,LSR#1	@RX1 binary
	and	R3,R5,R0,LSR#2	@RX2 binary
	and	R4,R5,R0,LSR#3	@RX3 binary

	and	R7,R5,R0,LSR#4	@RX4 checkbit 1
	and	R8,R5,R0,LSR#5	@RX5 checkbit 2
	and	R9,R5,R0,LSR#6	@RX6 checkbit 3

@Even parity part A
	eor	R10,R1,R3	@R1 XOR R3
	eor	R10,R10,R4	@Previous XOR R4
	eor	R10,R10,R7	@Previous XOR R7

@Even Parity Part B
	eor	R11,R2,R3	@R2 XOR R3
	eor	R11,R11,R4	@Previous XOR R4
	eor	R11,R11,R8	@Previous XOR R8

@Even Parity Part C
	eor	R12,R1,R2	@R1 XOR R2
	eor	R12,R12,R4	@Previous XOR R4
	eor	R12,R12,R9	@Previous XOR R9

@Part1
	eor	R2,R5,R11	@Overwrite R2 to save on register space
	and	R1,R2,R10	@Overwrite R1 to save on register space
	and	R1,R1,R12	@Completes the tripple and statement with the not value
	and	R3,R5,R0,LSR#0	@Bringing back the previous bit from the input
	eor	R1,R1,R3	@Completes the final XOR for the result bit
	cmp	R1,R5
	addeq	R6,#1		@Appends the bit to the final result

@Part2
	eor	R2,R5,R10	@NOT R10
	and	R1,R2,R11
	and	R1,R1,R12	@Tripple input AND statement with A B and C
	and	R3,R5,R0,LSR#1
	eor	R1,R1,R3	@Completes the XOR with RX1
	cmp	R1,R5
	addeq	R6,#2

@Part3
	eor	R2,R5,R12	@NOT R12
	and	R1,R2,R10
	and	R1,R1,R11	@Tripple input AND statement with A B and C
	and	R3,R5,R0,LSR#2
	eor	R1,R1,R3	@Completes the XOR with RX2
	cmp	R1,R5
	addeq	R6,#4

@Part4
	and	R1,R10,R11
	and	R1,R1,R12	@Tripple input AND statement with A B and C
	and	R3,R5,R0,LSR#3
	eor	R1,R1,R3	@Completes the XOR with RX3
	cmp	R1,R5
	addeq	R6,#8

@Setting to R0
	mov	R0,R6		@Swaps the value of R0 with R6 (The constructed output)

	pop	{R1-R12}	@Pops the used registers
	bx	LR		@Returns to the calling function


@R0: Original input / new output
@R1-4: Data bits from binary
@R5: Value #1
@R6: Copy of binary
@R7-9: Check Bits
@R10-12: Logic gates
