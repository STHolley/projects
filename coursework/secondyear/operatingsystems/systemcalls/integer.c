/*
 * Replace the following string of 0s with your student number
 * 170340421
 */
#include <stdbool.h>
#include <errno.h>
#include <limits.h>
#include <stdlib.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>

#include "obj_store.h"
#include "integer.h"

/* Prototype of private _add function prototype for implementation of the 
 * add member of struct integer
 */
static Integer _add(Integer self, Integer i); 

/* Prototype of private _subtract function prototype for implementation of 
 * the subtract member of struct integer
 */
static Integer _subtract(Integer self, Integer i); 

/* Prototype of private _multiply function prototype for implementation of 
 * the multiply member of struct integer
 */
static Integer _multiply(Integer self, Integer i); 

/* Prototype of private _divide function prototype for implementation of the
 * divide member of struct integer
 */
static Integer _divide(Integer self, Integer i);   

/* Prototype of private _modulo function prototype for implementation of the
 * modulo member of struct integer
 */
static Integer _modulo(Integer self, Integer i);   

/* Prototype of private _get_value function prototype for implementation of the
 * get_value member of struct integer
 */
static int _get_value(Integer self);

/* private INT_MAX and INT_MIN objects to illustrate caching in newInteger */
static Integer _IntMax = NULL;
static Integer _IntMin = NULL;

/* the string representation of an Integer for saving to file */
static char* STR_REP_FMT = "int:%d\n";

/* check for cached integers for illustration - this could be 
 * extended for caching commonly used values as in Java or for enforcing 
 * single instances of each integer value as in Python. 
 * Do NOT change this function.
 */
static Integer _is_cached(int value) {
    if (_IntMax && _IntMax->_val == value)
        return _IntMax;
    
    if (_IntMin && _IntMin->_val == value)
        return _IntMin;
        
    return NULL;
}

/* caching values - only INT_MAX and INT_MIN in this case 
 * Do NOT change this function.
 */
static void _cache(Integer i) {
    if (!i) 
        return;
        
    if (!_IntMax && i->_val == INT_MAX) 
        _IntMax = i;
        
    if (!_IntMin && i->_val == INT_MIN) 
        _IntMin = i;
}

/* private _new_str_rep function to dynamically allocate a new string
 * representation of an Integer.
 * Pass a pointer to this function to the store_obj function of the obj_store
 * library.
 * Do NOT change this function.
 */
static char* _new_str_rep(void* obj) {
    char* str_rep = NULL;
    
    if (obj) {
        Integer i = (Integer) obj;
        /* asprintf allocates new string */
        (void) asprintf(&str_rep, STR_REP_FMT, i->_val);
    }
    
    return str_rep;
}

/* newInteger: implemented, do NOT change */
Integer newInteger(int value) {
    Integer self = _is_cached(value);

    if (self) return self;
    
    self = (Integer) malloc(sizeof(struct integer));
   
    if (self) {
        self->_val = value;
        
        self->add = _add;
        self->subtract = _subtract;
        self->multiply = _multiply;
        self->divide = _divide;
        self->modulo = _modulo;
     
        self->get_value = _get_value;
        
        if (ostore_is_on() && !store_obj(self, _new_str_rep)) {
            /* ostore is on but failed to store object */
            free(self);
            self = NULL;        
        } else {
            _cache(self);
        }
    }

    return self;
}

/* deleteInteger: implemented, do NOT change */
void deleteInteger(Integer* ai) {
    if (ai && *ai != _IntMax && *ai != _IntMin) {
        if (ostore_is_on())
            unlink_obj(*ai);

        free(*ai);
        
        *ai = NULL;
    }
}

/* IntMax: implemented, do NOT change */
Integer IntMax() {
    if (!_IntMax) {
        _IntMax = newInteger(INT_MAX);
    }
    
    return _IntMax;
}

/* IntMin: implemented, do NOT change */
Integer IntMin() {
    if (!_IntMin) {
        _IntMax = newInteger(INT_MIN);
    }
    
    return _IntMin;
}

/* _add: implemented, do NOT change */
Integer _add(Integer self, Integer i) {
    if (!self || !i) {
        errno = EINVAL;
        return NULL;
    }
    
    Integer r = NULL;
    
    if ((self->_val < 0 && i->_val > 0) || (self->_val > 0 && i->_val < 0)) {
        r = newInteger(self->_val + i->_val);
    } else if (self->_val >= 0 && (self->_val <= INT_MAX - i->_val)) {
        r = newInteger(self->_val + i->_val);
    } else if (self->_val <= 0 && (self->_val >= -INT_MAX - (i->_val + 1))) {
        /* note: -INT_MAX - (i->_val + 1) == INT_MIN - i->_val but guards 
         * against i->_val == INT_MIN */
        r = newInteger(self->_val + i->_val);
    }
        
    if (!r) errno = ERANGE;
    
    return r;
}

/* 
 * TODO: IMPLEMENT _subtract
 * see comments to the subtract member of struct integer in integer.h for the
 * specification of this function
 */
Integer _subtract(Integer self, Integer i) {
	if(!self || !i){
		errno = EINVAL;
		return NULL;
	}
	Integer r = NULL;
	
	if(((self->_val >= 0) && (i->_val >= 0)) || ((self->_val < 0) && (i->_val < 0))){ //positive - positive or negative - negative
		r = newInteger (self->_val - i->_val);
	} else if((self->_val >= 0) && ((INT_MIN + self->_val + 1) <= i->_val)){ //positive - negative
		r = newInteger (self->_val - i->_val);
	} else if((self->_val < 0) && ((self->_val + -INT_MIN)) >= i->_val){ //Negative - positive
		r = newInteger (self->_val - i->_val);
	}
	
	if (!r) errno = ERANGE;
	
    return r;
}

/* _multiply: implemented, do NOT change */
Integer _multiply(Integer self, Integer i) {
    if (!self || !i) {
        errno = EINVAL;
        return NULL;
    }
    
    Integer r = NULL;
    
    bool valid = false;
	
    if (!self->_val || !i->_val) { //If either value is 0
        valid = true;
    } else if (self->_val > 0) {
        valid = i->_val > 0 ? self->_val <= INT_MAX / i->_val  //+self & +i, 
                            : i->_val >= INT_MIN / self->_val; //+self & -i, 
    } else {
        valid = i->_val > 0 ? self->_val >= INT_MIN / i->_val  //-self & +i, 
                            : self->_val >= INT_MAX / i->_val; //-self & -i, 
    }
    
    if (valid) {
        r = newInteger(self->_val * i->_val);
    } else {
        errno = ERANGE;
    }
    
    return r;
}

/* 
 * TODO: IMPLEMENT _divide 
 * see comments to the divide member of struct integer in integer.h for the
 * specification of this function
 */
Integer _divide(Integer self, Integer i) {
	if (!self || !i) {
		errno = EINVAL;
		return NULL;
    }
    
    Integer r = NULL;
	
	bool valid = false;
	
	//i->_val cannot be 0 so i->_val has to be true / more than 0
	
	if(i->_val != 0){
		valid = true;
	}
	if(self->_val == INT_MIN && i->_val == -1){
		valid = false;
	}
	
	
    if (valid) {
        r = newInteger(self->_val / i->_val);
    } else {
        errno = ERANGE;
    }
    
    return r;
}

/* 
 * TODO: IMPLEMENT _modulo
 * see comments to the modulo member of struct integer in integer.h for the
 * specification of this function
 */
Integer _modulo(Integer self, Integer i) {
	if (!self || !i) {
		errno = EINVAL;
		return NULL;
    }
    
    Integer r = NULL;
	
	bool valid = false;
	
	if(i->_val != 0){
		valid = true;
	}
	if(self->_val == INT_MIN && i->_val == -1){
		valid = false;
	}
	
    if (valid) {
        r = newInteger(self->_val % i->_val);
    } else {
        errno = ERANGE;
    }
    
    return r;
}

/* _get_value: implemented, do NOT change */
int _get_value(Integer self)  {
    if (self) {
        return self->_val;
    } else {
        errno = EINVAL;
        return 0;
    }
}