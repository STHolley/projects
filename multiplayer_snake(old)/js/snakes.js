function SnakeAi(){
	this.x = (cols-1)*sf;
	this.y = 0;
	this.facing = 4;
	this.xdir = 0;
	this.ydir = 0;
	this.dx;
	this.dy;
	this.gradient;
	this.xintercept;
	this.score = 0;
	this.body = [];
	this.counter = 0;
	this.dir1 = function(){
		var prevx = this.xdir;
		var prevy = this.ydir;
		var dy = food.y - this.y;
		var dx = food.x - this.x;
		if(random(1) < 0.5){
			//x
			if(random(1) <= 0.75){ //75% to go towards food. Adjustable.
				if(dx < 0){
					if(prevx != 1){
						this.xdir = -1;
						this.ydir = 0;
						this.facing = 4;
					}else{
						if(random(1) < 0.5){
							this.xdir = 0;
							this.ydir = 1;
							this.facing = 3;
						}else{
							this.xdir = 0;
							this.ydir = -1;
							this.facing = 1;
						}
					}
				}else{
					if(prevx != -1){
						this.xdir = 1;
						this.ydir = 0;
						this.facing = 2;
					}else{
						if(random(1) < 0.5){
							this.xdir = 0;
							this.ydir = 1;
							this.facing = 3;
						}else{
							this.xdir = 0;
							this.ydir = -1;
							this.facing = 1;
						}
					}
				}
			}else{
				if(random(1) < 0.5){
					if(prevx != 1){
						this.xdir = -1;
						this.ydir = 0;
						this.facing = 4;
					}else{
						if(random(1) < 0.5){
							this.xdir = 0;
							this.ydir = 1;
							this.facing = 3;
						}else{
							this.xdir = 0;
							this.ydir = -1;
							this.facing = 1;
						}
					}
				}else{
					if(prevx != -1){
						this.xdir = 1;
						this.ydir = 0;
						this.facing = 2;
					}else{
						if(random(1) < 0.5){
							this.xdir = 0;
							this.ydir = 1;
							this.facing = 3;
						}else{
							this.xdir = 0;
							this.ydir = -1;
							this.facing = 1;
						}
					}
				}
			}
		}else{
			if(random(1) <= 0.75){ //75% to go towards food. Adjustable.
				if(dx < 0){
					if(prevy != 1){
						this.ydir = -1;
						this.xdir = 0;
						this.facing = 1;
					}else{
						if(random(1) < 0.5){
							this.ydir = 0;
							this.xdir = 1;
							this.facing = 2;
						}else{
							this.ydir = 0;
							this.xdir = -1;
							this.facing = 4;
						}
					}
				}else{
					if(prevy != -1){
						this.ydir = 1;
						this.xdir = 0;
						this.facing = 3;
					}else{
						if(random(1) < 0.5){
							this.ydir = 0;
							this.xdir = 1;
							this.facing = 2;
						}else{
							this.ydir = 0;
							this.xdir = -1;
							this.facing = 4;
						}
					}
				}
			}else{
				if(random(1) < 0.5){
					if(prevy != 1){
						this.ydir = -1;
						this.xdir = 0;
						this.facing = 1;
					}else{
						if(random(1) < 0.5){
							this.ydir = 0;
							this.xdir = 1;
							this.facing = 2;
						}else{
							this.ydir = 0;
							this.xdir = -1;
							this.facing = 4;
						}
					}
				}else{
					if(prevy != -1){
						this.ydir = 1;
						this.xdir = 0;
						this.facing = 2;
					}else{
						if(random(1) < 0.5){
							this.ydir = 0;
							this.xdir = 1;
							this.facing = 3;
						}else{
							this.ydir = 0;
							this.xdir = -1;
							this.facing = 1;
						}
					}
				}
			}
		}
	}
	this.dir2 = function(){
		var prevx = this.xdir;
		var prevy = this.ydir;
		var dy = food.y - this.y;
		var dx = food.x - this.x;
		if(dx === 0){
			if(dy < 0){
				if(prevy != 1){
					this.xdir = 0;
					this.ydir = -1;
					this.facing = 1;
				}else{
					if(random(1) < 0.5){
						this.xdir = -1;
						this.ydir = 0;
						this.facing = 4;
					}else{
						this.xdir = 1;
						this.ydir = 0;
						this.facing = 2;
					}
				}
			}else{
				if(prevy != -1){
					this.xdir = 0;
					this.ydir = 1;
					this.facing = 3;
				}else{
					if(random(1) < 0.5){
						this.xdir = -1;
						this.ydir = 0;
						this.facing = 4;
					}else{
						this.xdir = 1;
						this.ydir = 0;
						this.facing = 2;
					}
				}
			}
		}else{
			if(dx < 0){
				if(prevx != 1){
					this.xdir = -1;
					this.ydir = 0;
					this.facing = 4;
				}else{
					if(random(1) < 0.5){
						this.ydir = -1;
						this.xdir = 0;
						this.facing = 1;
					}else{
						this.ydir = 1;
						this.xdir = 0;
						this.facing = 3;
					}
				}
			}else{
				if(prevx != -1){
					this.xdir = 1;
					this.ydir = 0;
					this.facing = 2;
				}else{
					if(random(1) < 0.5){
						this.ydir = -1;
						this.xdir = 0;
						this.facing = 1;
					}else{
						this.ydir = 1;
						this.xdir = 0;
						this.facing = 3;
					}
				}
			}
		}
	}
	this.move = function(){
		this.body[0] = createVector(this.x, this.y, this.facing);
		for(var i = this.body.length - 1; i > 0; i--){
			this.body[i] = this.body[i-1];
		}
		if((this.x >= ((sf*cols) - sf)) && (this.xdir === 1)){
			this.x = -sf;
		}
		if((this.x <= 0) && (this.xdir === -1)){
			this.x = (sf*cols);
		}
		if((this.y <= 0) && (this.ydir === -1)){
			this.y = (sf*rows);
		}
		if((this.y >= (sf*rows)-sf) && (this.ydir === 1)){
			this.y = -sf;
		}
		this.x += this.xdir * sf;
		this.y += this.ydir * sf;
		this.moved = false
	}
	this.die = function(){
		for(i = 1; i < this.body.length; i++){
			if((this.x === this.body[i].x) && (this.y === this.body[i].y)){
				this.forceKill();
				path = [];
				con = true;
			}
		}
		for(i = 1; i < s.body.length; i++){
			if((this.x === s.body[i].x) && (this.y === s.body[i].y)){
				this.forceKill();
				path = [];
				con = true;
			}
		}
		if((this.x === s.x)&&(this.y === s.y)){
			this.forceKill();
			s.forceKill();
			path = [];
			con = true;
		}
	}
	this.show = function(){
		for (var i = 1; i < this.body.length - 1; i++) {
			var z = this.body[i].z;
			if((z === 1)||(z === 3)){
				var body = Rbody1;
			}
			if((z === 4)||(z === 2)){
				var body = Rbody4;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 2)){
				var body = RbendWN;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 4)){
				var body = RbendNE;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 1)){
				var body = RbendES;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 3)){
				var body = RbendNE;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 2)){
				var body = RbendSW;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 4)){
				var body = RbendES;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 1)){
				var body = RbendSW;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 3)){
				var body = RbendWN;
			}
			image(body, this.body[i].x, this.body[i].y);
			//rect(this.body[i].x, this.body[i].y, sf, sf);
		}
		if(this.body.length > 1){
			var z = this.body[this.body.length - 1].z;
			var tail;
			if(z === 1){
				tail = Rtail1;
			}else if(z === 2){
				tail = Rtail2;
			}else if(z === 3){
				tail = Rtail3;
			}else if(z === 4){
				tail = Rtail4;
			}
			image(tail, this.body[this.body.length - 1].x, this.body[this.body.length - 1].y)
		}
		var z = this.body[0].z;
		var Rhead;
		if(z === 1){
			Rhead = Rhead1;
		}else if(z === 2){
			Rhead = Rhead2;
		}else if(z === 3){
			Rhead = Rhead3;
		}else if(z === 4){
			Rhead = Rhead4;
		}
		image(Rhead, this.x, this.y)
	}
	this.getFood = function(){
		var d = dist(this.x, this.y, food.x, food.y);
		if(d < 1){
			eaten = true;
			this.body.push(this.body[this.body.length - 1]);
			this.score++
			return true;
		} else {
			return false;
		}
	}
	this.forceKill = function(){
		this.body = [];
		this.x = (cols - 1) * sf;
		this.y = 0;
		this.xdir = -1;
		this.ydir = 0;
		this.score = 0;
	}
}
function Snake(){
	this.moved = false;
	this.x = -sf;
	this.y = 0;
	this.xdir = 1;
	this.ydir = 0;
	this.facing = 2;
	this.dead = false;
	this.body = [];
	this.score = 0;
	this.dir = function(x, y){
		this.xdir = x;
		this.ydir = y;
	}
	this.move = function(){	
		this.body[0] = createVector(this.x, this.y, this.facing);
		for(var i = this.body.length - 1; i > 0; i--){
			this.body[i] = this.body[i-1];
		}
		if((this.x >= ((sf*cols) - sf)) && (this.xdir === 1)){
			this.x = -sf;
		}
		if((this.x <= 0) && (this.xdir === -1)){
			this.x = (sf*cols);
		}
		if((this.y <= 0) && (this.ydir === -1)){
			this.y = (sf*rows);
		}
		if((this.y >= (sf*rows)-sf) && (this.ydir === 1)){
			this.y = -sf;
		}
		this.x += this.xdir * sf;
		this.y += this.ydir * sf;
		this.moved = false
	}
	this.die = function(){
		if(twoP){
			var opp = s2;
		}else{
			var opp = ai
		}
		for(i = 1; i < this.body.length; i++){
			if((this.x === this.body[i].x) && (this.y === this.body[i].y)){
				this.forceKill()
			}
		}
		for(i = 1; i < opp.body.length; i++){
			if((this.x === opp.body[i].x) && (this.y === opp.body[i].y)){
				this.forceKill()
			}
		}
		if((this.x === opp.x)&&(this.y === opp.y)){
			this.forceKill();
			opp.forceKill();
		}
	}
	this.show = function(){
		for (var i = 1; i < this.body.length - 1; i++) {
			var z = this.body[i].z;
			if((z === 1)||(z === 3)){
				var body = Gbody1;
			}
			if((z === 4)||(z === 2)){
				var body = Gbody4;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 2)){
				var body = GbendWN;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 4)){
				var body = GbendNE;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 1)){
				var body = GbendES;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 3)){
				var body = GbendNE;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 2)){
				var body = GbendSW;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 4)){
				var body = GbendES;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 1)){
				var body = GbendSW;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 3)){
				var body = GbendWN;
			}
			image(body, this.body[i].x, this.body[i].y);
		}
		if(this.body.length > 1){
			var z = this.body[this.body.length - 1].z;
			var tail;
			if(z === 1){
				tail = Gtail1;
			}else if(z === 2){
				tail = Gtail2;
			}else if(z === 3){
				tail = Gtail3;
			}else if(z === 4){
				tail = Gtail4;
			}
			image(tail, this.body[this.body.length - 1].x, this.body[this.body.length - 1].y)
		}
		var z = this.body[0].z;
		var head;
		if(z === 1){
			head = Ghead1;
		}else if(z === 2){
			head = Ghead2;
		}else if(z === 3){
			head = Ghead3;
		}else if(z === 4){
			head = Ghead4;
		}
		image(head, this.x, this.y)
	}
	this.getFood = function(){
		var d = dist(this.x, this.y, food.x, food.y);
		if(d < 1){
			eaten = true;
			this.body.push(this.body[this.body.length - 1]);
			this.score++
			return true;
		} else {
			return false;
		}
	}
	this.forceKill = function(){
		this.body = [];
		this.x = 0;
		this.y = 0;
		this.xdir = 1;
		this.ydir = 0;
		this.score = 0;
		this.facing = 2;
	}
}
function Snake2(){
	this.moved = false;
	this.x = (cols - 1) * sf;
	this.y = 0;
	this.xdir = -1;
	this.ydir = 0;
	this.dead = false;
	this.body = [];
	this.score = 0;
	this.facing = 4;
	this.dir = function(x, y){
		this.xdir = x;
		this.ydir = y;
	}
	this.move = function(){	
		this.body[0] = createVector(this.x, this.y, this.facing);
		for(var i = this.body.length - 1; i > 0; i--){
			this.body[i] = this.body[i-1];
		}
		if((this.x >= ((sf*cols) - sf)) && (this.xdir === 1)){
			this.x = -sf;
		}
		if((this.x <= 0) && (this.xdir === -1)){
			this.x = (sf*cols);
		}
		if((this.y <= 0) && (this.ydir === -1)){
			this.y = (sf*rows);
		}
		if((this.y >= (sf*rows)-sf) && (this.ydir === 1)){
			this.y = -sf;
		}
		this.x += this.xdir * sf;
		this.y += this.ydir * sf;
		this.moved = false
	}
	this.die = function(){
		for(i = 1; i < this.body.length; i++){
			if((this.x === this.body[i].x) && (this.y === this.body[i].y)){
				this.forceKill()
			}
		}
		for(i = 1; i < s.body.length; i++){
			if((this.x === s.body[i].x) && (this.y === s.body[i].y)){
				this.forceKill()
			}
		}
		if((this.x === s.x)&&(this.y === s.y)){
			this.forceKill();
			s.forceKill();
		}
	}
	this.show = function(){
		for (var i = 1; i < this.body.length - 1; i++) {
			var z = this.body[i].z;
			if((z === 1)||(z === 3)){
				var body = Rbody1;
			}
			if((z === 4)||(z === 2)){
				var body = Rbody4;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 2)){
				var body = RbendWN;
			}
			if((this.body[i].z === 1)&&(this.body[i+1].z === 4)){
				var body = RbendNE;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 1)){
				var body = RbendES;
			}
			if((this.body[i].z === 2)&&(this.body[i+1].z === 3)){
				var body = RbendNE;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 2)){
				var body = RbendSW;
			}
			if((this.body[i].z === 3)&&(this.body[i+1].z === 4)){
				var body = RbendES;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 1)){
				var body = RbendSW;
			}
			if((this.body[i].z === 4)&&(this.body[i+1].z === 3)){
				var body = RbendWN;
			}
			image(body, this.body[i].x, this.body[i].y);
		}
		if(this.body.length > 1){
			var z = this.body[this.body.length - 1].z;
			var tail;
			if(z === 1){
				tail = Rtail1;
			}else if(z === 2){
				tail = Rtail2;
			}else if(z === 3){
				tail = Rtail3;
			}else if(z === 4){
				tail = Rtail4;
			}
			image(tail, this.body[this.body.length - 1].x, this.body[this.body.length - 1].y)
		}
		var z = this.body[0].z;
		var Rhead;
		if(z === 1){
			Rhead = Rhead1;
		}else if(z === 2){
			Rhead = Rhead2;
		}else if(z === 3){
			Rhead = Rhead3;
		}else if(z === 4){
			Rhead = Rhead4;
		}
		image(Rhead, this.x, this.y)
	}
	this.getFood = function(){
		var d = dist(this.x, this.y, food.x, food.y);
		if(d < 1){
			eaten = true;
			this.body.push(this.body[this.body.length - 1]);
			this.score++
			return true;
		} else {
			return false;
		}
	}
	this.forceKill = function(){
		this.body = [];
		this.x = (cols-1)*sf;
		this.y = 0;
		this.xdir = -1;
		this.ydir = 0;
		this.score = 0;
		this.facing = 4;
	}
}
var pause = false
function stopIt(){
	if(pause){
		noLoop()
		pause = false;
	}else{
		loop()
		pause = true;
	}
}
