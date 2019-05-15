function keyPressed() {
	//if(!Loadup){ 
		if(s.body.length > 2){
			if ((keyCode === UP_ARROW)&&(s.ydir != 1) && (s.moved === false)) {
				s.dir(0, -1);
				s.facing = 1;
				s.moved = true;
			} else if ((keyCode === DOWN_ARROW)&&(s.ydir != -1) && (s.moved === false)) {
				s.dir(0, 1);
				s.facing = 3;
				s.moved = true;
			} else if ((keyCode === RIGHT_ARROW)&&(s.xdir != -1) && (s.moved === false)) {
				s.dir(1, 0);
				s.facing = 2;
				s.moved = true;
			} else if ((keyCode === LEFT_ARROW)&&(s.xdir != 1) && (s.moved === false)) {
				s.dir(-1, 0);
				s.facing = 4;
				s.moved = true;
			}
		}else{
			if ((keyCode === UP_ARROW) && (s.moved === false)) {
				s.dir(0, -1);
				s.facing = 1;
				s.moved = true;
			} else if ((keyCode === DOWN_ARROW) && (s.moved === false)) {
				s.dir(0, 1);
				s.facing = 3;
				s.moved = true;
			} else if ((keyCode === RIGHT_ARROW) && (s.moved === false)) {
				s.dir(1, 0);
				s.facing = 2;
				s.moved = true;
			} else if ((keyCode === LEFT_ARROW) && (s.moved === false)) {
				s.dir(-1, 0);
				s.facing = 4;
				s.moved = true;
			}
		}
		if(twoP){
			if ((keyCode === 87)&&(s2.ydir != 1) && (s2.moved === false)) {
				s2.dir(0, -1);
				s2.facing = 1;
				s2.moved = true;
			} else if ((keyCode === 83)&&(s2.ydir != -1) && (s2.moved === false)) {
				s2.dir(0, 1);
				s2.facing = 3;
				s2.moved = true;
			} else if ((keyCode === 68)&&(s2.xdir != -1) && (s2.moved === false)) {
				s2.dir(1, 0);
				s2.facing = 2;
				s2.moved = true;
			} else if ((keyCode === 65)&&(s2.xdir != 1) && (s2.moved === false)) {
				s2.dir(-1, 0);
				s2.facing = 4;
				s2.moved = true;
			}
		}
	//}
}
/*var StartX;
var StartY;
var Swiped = false;
function touchStarted(){
	if((wid < 500)&&(!Loadup)&&(!controls)){
		if(!Swiped){
			StartX = mouseX;
			StartY = mouseY;
			Swiped = true;
		}
	}
}
function touchEnded(){
	if((wid < 500)&&(!Loadup)&&(!controls)){
		Swiped = false;
		var dy = mouseY - StartY;
		var dx = mouseX - StartX;
		if((abs(dx) > 75)||(abs(dy) > 75)){
			if(s.body.length >= 1){
				if(abs(dy) > abs(dx)){
					//change ydir
					if(dy < 0){
						s.dir(0, -1);
						s.facing = 1;
						s.moved = true;
					} else {
						s.dir(0, 1);
						s.facing = 3;
						s.moved = true;
					}
				} else {
					//change xdir
					if(dx < 0){
						s.dir(-1, 0);
						s.facing = 4;
						s.moved = true;
					} else {
						s.dir(1, 0);
						s.facing = 2;
						s.moved = true;
					}
				}
			}else{
				if(abs(dy) > abs(dx)){
					//change ydir
					if(dy < 0){
						if(s.ydir != 1){
							s.dir(0, -1);
							s.facing = 1;
							s.moved = true;
						}
					} else {
						if(s.ydir != -1){
							s.dir(0, 1);
							s.facing = 3;
							s.moved = true;
						}
					}
				} else {
					//change xdir
					if(dx < 0){
						if(s.xdir != 1){
							s.dir(-1, 0);
							s.facing = 4;
							s.moved = true;
						}
					} else {
						if(s.xdir != -1){
							s.dir(1, 0);
							s.facing = 2;
							s.moved = true;
						}
					}
				}
			}
		}
	}
}*/