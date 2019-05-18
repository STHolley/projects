/*
 * Replace the following string of 0s with your student number
 * 000000000
 */
#include <lib.h>    // provides _syscall and message
#include <errno.h>
#include "fsloglib.h"

int startfslog(unsigned short ops2log) {
	if(ops2log < FSOP_NONE || ops2log > FSOP_ALL){
		errno= EINVAL;
		return -1;
	}
	message m;
	m.m1_i1 = i;
	_syscall(VFS_PROC_NR, STARTFSLOG, &m);
    return 0;
}

int stopfslog(unsigned short ops2stoplog) {
	if(ops2log < FSOP_NONE || ops2log > FSOP_ALL){
		errno= EINVAL;
		return -1;
	}
	message m;
	m.m1_i1 = i;
	_syscall(VFS_PROC_NR, STOPFSLOG, &m);
    return 0;
}

int getfsloginf(struct fsloginf *fsloginf) {
	if(ops2log < FSOP_NONE || ops2log > FSOP_ALL){
		errno= EINVAL;
		return -1;
	}
	message m;
	m.m1_p1 = (char*)&fsloginf;
	m.m1_p2 = (char*)fslog;
	_syscall(VFS_PROC_NR, GETFSLOGINF, &m);
    return 0;
}

int getfslog(struct fsloginf *fsloginf, struct fslogrec fslog[]) {
    if(ops2log < FSOP_NONE || ops2log > FSOP_ALL){
		errno= EINVAL;
		return -1;
	}
	message m;
	m.m1_p1 = (char*)&fsloginf;
	m.m1_p2 = (char*)fslog;
	_syscall(VFS_PROC_NR, GETFSLOG, &m);
    return 0;
}
