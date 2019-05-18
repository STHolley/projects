	.global _start
_start:	mov	R0,#0b1010
	bl	generate4
	mov	R7,#1
	svc	0

generate4:
	push	{R0-R7}		@Resets registers to be used
	mov	R5,R0		@Clone R0 (The binary) to R5
	mov	R4,#1		@Set R4 to a constant value of 1 for comparisons

	@R1-R3: Contains the value from the AND gate for the 3 check bits
	@R6-R7: conatins the XOR results for even parity
	@R0: Contains the original binary along with the 3 check bits at the start

@Checkbit unit 1
	and	R1,R4,R5,LSR#3	@Shifts to the last bit and compares it to 1
	and	R2,R4,R5,LSR#2	@Shifts to the third bit and compares it to 1
	and	R3,R4,R5,LSR#0	@Shifts to the first bit and compares it to 1
	eor	R6,R2,R1	@The first of the two XOR gates for even parity
	eor	R7,R6,R3	@The second of the two XOR gates for even parity
	add	R0,R7,LSL#4	@Append to the start of the initial binary

@Checkbit unit 2
	and	R1,R4,R5,LSR#3	@Shifts to the last bit and compares it to 1
	and	R2,R4,R5,LSR#2	@Shifts to the third bit and compares it to 1
	and	R3,R4,R5,LSR#1	@Shifts to the second bit and compares it to 1
	eor	R6,R2,R1	@The first of the two XOR gates for even parity
	eor	R7,R6,R3	@The second of the two XOR gates for even parity
	add	R0,R7,LSL#4	@Append to the start of the initial binary

@Checkbit unit 3
	and	R1,R4,R5,LSR#3	@Shifts to the last bit and compares it to 1
	and	R2,R4,R5,LSR#1	@Shifts to the second bit and compares it to 1
	and	R3,R4,R5,LSR#0	@Shifts to the first bit and compares it to 1
	eor	R6,R2,R1	@The first of the two XOR gates for even parity
	eor	R7,R6,R3	@The second of the two XOR gates for even parity
	add	R0,R7,LSL#4	@Append to the start of the initial binary

	pop	{R1-R7}		@Pop all data in the registers
	bx	LR		@Branch back to the calling function
