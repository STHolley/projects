var twoP = false;
var diff;
var gameOver = false;
var cols = rows = 20; //Amount of columns and rows in the grid
var sf = 25; //Size of all the squares in the grid in pixels
var s, ai, s2; //Both players and AI
var food; //food object
var Loadup = true; //Boolean for showing the Loadup screen
var controls = false; //Boolean for showing the controls screen
var play = false;
var timeelapsed = 0;
var pressed = false;
var wid;
var openSet = [];
function preload() {
	Gbody1 = loadImage("img/Green/Gbody1.png");
	Gbody4 = loadImage("img/Green/Gbody4.png");
	Ghead1 = loadImage("img/Green/Ghead1.png");
	Ghead2 = loadImage("img/Green/Ghead2.png");
	Ghead3 = loadImage("img/Green/Ghead3.png");
	Ghead4 = loadImage("img/Green/Ghead4.png");
	Gtail1 = loadImage("img/Green/Gtail1.png");
	Gtail2 = loadImage("img/Green/Gtail2.png");
	Gtail3 = loadImage("img/Green/Gtail3.png");
	Gtail4 = loadImage("img/Green/Gtail4.png");
	GbendNE = loadImage("img/Green/GbendNE.png");
	GbendES = loadImage("img/Green/GbendES.png");
	GbendSW = loadImage("img/Green/GbendSW.png");
	GbendWN = loadImage("img/Green/GbendWN.png");
	Rbody1 = loadImage("img/Red/Rbody1.png");
	Rbody4 = loadImage("img/Red/Rbody4.png");
	Rhead1 = loadImage("img/Red/Rhead1.png");
	Rhead2 = loadImage("img/Red/Rhead2.png");
	Rhead3 = loadImage("img/Red/Rhead3.png");
	Rhead4 = loadImage("img/Red/Rhead4.png");
	Rtail1 = loadImage("img/Red/Rtail1.png");
	Rtail2 = loadImage("img/Red/Rtail2.png");
	Rtail3 = loadImage("img/Red/Rtail3.png");
	Rtail4 = loadImage("img/Red/Rtail4.png");
	RbendNE = loadImage("img/Red/RbendNE.png");
	RbendES = loadImage("img/Red/RbendES.png");
	RbendSW = loadImage("img/Red/RbendSW.png");
	RbendWN = loadImage("img/Red/RbendWN.png");
	FoodImg = loadImage("img/food.png");
}
function setup(){
	timeelapsed = 0;
	for(var i = 0; i < 20; i++){
		grid[i] = [];
	}
	for(var i = 0; i < 20; i++){
		for(var j = 0; j < 20; j++){
			grid[i][j] = new CellProp(i, j);
		}
	}
	for(var i = 0; i < 20; i++){
		for(var j = 0; j < 20; j++){
			grid[i][j].AddNeigh();
		}
	}
	var btn = document.createElement("BUTTON");
	var t = document.createTextNode("Play");
	btn.appendChild(t);
	btn.onclick = function(){
		var Sing = document.createElement("BUTTON");
		var Mult = document.createElement("BUTTON");
		Sing.appendChild(document.createTextNode("Single Player"));
		Mult.appendChild(document.createTextNode("Multiplayer Player"));
		Mult.onclick = function(){
			Diff = 0;
			twoP = true;
			body.removeChild(Sing);
			body.removeChild(Mult);
			createCanvas(500, 500); //Create a canvas element using the P5 createCanvas()
			timer();
			s = new Snake();
			s2 = new Snake2();
			dob = true;
			var canvas = document.getElementById("defaultCanvas0")
			canvas.style.display = "block";
			loop();
		}
		Sing.onclick = function(){
			var EZ = document.createElement("BUTTON");
			var MD = document.createElement("BUTTON");
			var HR = document.createElement("BUTTON");
			EZ.appendChild(document.createTextNode("Easy"));
			MD.appendChild(document.createTextNode("Medium"));
			HR.appendChild(document.createTextNode("Hard"));
			EZ.onclick = function(){
				Diff = 1;
				twoP = false;
				body.removeChild(EZ);
				body.removeChild(MD);
				body.removeChild(HR);
				createCanvas(500, 500); //Create a canvas element using the P5 createCanvas()
				timer();
				ai = new SnakeAi();
				s = new Snake();
				dob = true;
				var canvas = document.getElementById("defaultCanvas0")
				canvas.style.display = "block";
				loop();
			}
			MD.onclick = function(){
				Diff = 2;
				twoP = false;
				body.removeChild(EZ);
				body.removeChild(MD);
				body.removeChild(HR);
				createCanvas(500, 500); //Create a canvas element using the P5 createCanvas()
				timer();
				ai = new SnakeAi();
				s = new Snake();
				dob = true;
				var canvas = document.getElementById("defaultCanvas0")
				canvas.style.display = "block";
				loop();
			}
			HR.onclick = function(){
				Diff = 3;
				twoP = false;
				body.removeChild(EZ);
				body.removeChild(MD);
				body.removeChild(HR);
				createCanvas(500, 500); //Create a canvas element using the P5 createCanvas()
				timer();
				ai = new SnakeAi();
				s = new Snake();
				dob = true;
				var canvas = document.getElementById("defaultCanvas0")
				canvas.style.display = "block";
				loop();
			}
			document.body.appendChild(EZ);
			document.body.appendChild(MD);
			document.body.appendChild(HR);
			body.removeChild(Mult);
			body.removeChild(Sing);
		}
		document.body.appendChild(Sing);
		document.body.appendChild(Mult);
		this.parentElement.removeChild(this);
	}
	document.body.appendChild(btn);
	//frameRate(100); //Set the pace to 100 functions per second
	var time = document.createElement("p");//Create a new element to display below the canvas
	var node = document.createTextNode("");//Make it an empty paragraph
	time.setAttribute("id", "timer");//Give the paragraph the ID "Timer"
	time.appendChild(node);//Attach the attributes
	document.getElementById("body").appendChild(time);//Pin the paragraph element to the body
	background(0); //Set the colour of the canvas to black
	food = createVector(floor(random(cols)) * sf, floor(random(rows)) * sf);//Create a food item
	//in a random cell location.	
	noLoop();
}
//createCanvas(500, 500); //Create a canvas element using the P5 createCanvas()
function timer(){//Quick timer for the games.
	timeelapsed += 1;//Increase the timer by 1
	var timeLeft = 30 - timeelapsed;//Get the time left - 60 is the game length
	if(timeLeft > 0){//IF there is still time remaining
		document.getElementById("timer").innerHTML = "Time Remaining: " + timeLeft;
		//Set the paragraph element text to "Time remaining: ##" where ## is the time remaining
		setTimeout(timer, 1000) //Recall the function every second
	}else{//If there is no time left
		document.getElementById("timer").innerHTML = "Game Over!";//Set the paragraph element to empty
		var canvas = document.getElementById("defaultCanvas0")
		canvas.style.display = "none";
		setup();
	}
}
var dob = false;
function draw(){
	
	if(dob){
		rectMode(CORNER);
		frameRate(8)
		background(0);
		fill(0);
		rect(0, 0, width, height);
		for(i = 0; i < cols; i++){
			stroke(128);
			line(i*sf, 0, i*sf, height);
		}
		for(i = 0; i < rows; i++){
			stroke(128);
			line(0, i*sf, width, i*sf);
		}
		line(499, 0, 499, 499);
		line(0, 499, 499, 499);
		stroke(255);
		rect((cols+1)*sf, 0,(cols+1)*sf, (rows+1)*sf)
		noStroke()
		fill(255);
		image(FoodImg , food.x, food.y);
		if(twoP){
			if((s.getFood())||(s2.getFood())){
				food = createVector(floor(random(cols)) * sf, floor(random(rows)) * sf);
				var unocc = false;
				var OccGrid = [];
				while(unocc){
					for(var i = 0; i < s.body.length; i++){
						OccGrid.push(s.body[i]);
					}
					for(var i = 0; i < ai.body.length; i++){
						OccGrid.push(s2.body[i]);
					}
					for(var i = 0; i < OccGrid.length; i++){
						var x = OccGrid[i].x;
						var y = OccGrid[i].y;
						if((x == food.x) && (y == food.y)){
							food = createVector(floor(random(cols)) * sf, floor(random(rows)) * sf);
						}else{
							unocc = true;
						}
					}
				}
			}
		}else{
			if((s.getFood())||(ai.getFood())){
				food = createVector(floor(random(cols)) * sf, floor(random(rows)) * sf);
				var unocc = false;
				var OccGrid = [];
				while(unocc){
					for(var i = 0; i < s.body.length; i++){
						OccGrid.push(s.body[i]);
					}
					for(var i = 0; i < ai.body.length; i++){
						OccGrid.push(ai.body[i]);
					}
					for(var i = 0; i < OccGrid.length; i++){
						var x = OccGrid[i].x;
						var y = OccGrid[i].y;
						if((x == food.x) && (y == food.y)){
							food = createVector(floor(random(cols)) * sf, floor(random(rows)) * sf);
						}else{
							unocc = true;
						}
					}
				}
				con = true;
			}
		}
		if(twoP){
			s.move();
			s2.move();
			s.show();
			s2.show();
			s.die();
			s2.die();
		}else if(Diff == 3){
			noStroke()
			s.move();
			ai.move();
			if(con){
				grid = []
				openSet = [];
				closedSet = [];
				for (var i = 0; i < cols; i++) {
					grid[i] = [];
				}
				path = [];
				for (var i = 0; i < cols; i++) {
					for (var j = 0; j < rows; j++) {
						grid[i][j] = new CellProp(i, j);
					}
				}
				for (var i = 0; i < cols; i++) {
					for (var j = 0; j < rows; j++) {
						grid[i][j].AddNeigh();
					}
				}
				for(var i = 0; i < ai.body.length; i++){
					grid[ai.body[i].x/sf][ai.body[i].y/sf].isWall = true;
				}
				start = grid[ai.x / sf][ai.y / sf];
				finish = grid[food.x / sf][(food.y / sf)];
				openSet.push(start);
				con = false;
				AiStar()
			}
			if(path.length > 0){
				ai.xdir = path[path.length - 1].x - (ai.x / sf);
				ai.ydir = path[path.length - 1].y - (ai.y / sf);
				if((ai.xdir == 1) && (ai.ydir == 0)){
					ai.facing = 2;
				}else if((ai.xdir == -1) && (ai.ydir == 0)){
					ai.facing = 4;
				} else if((ai.xdir == 0) && (ai.ydir == 1)){
					ai.facing = 3;
				} else if((ai.xdir == 0) && (ai.ydir == -1)){
					ai.facing = 1;
				}
				path.pop();
			}
			ai.show();
			s.show();
			ai.die();
			s.die();
		}else if(Diff == 2){
			ai.dir2();
			s.move();
			ai.move();
			ai.show();
			s.show();
			s.die();
			ai.die();
		}else if(Diff == 1){
			ai.dir1();
			s.move();
			ai.move();
			ai.show();
			s.show();
			s.die();
			ai.die();
		}
	}
}
var con = true
function sleep(){
	Loadup = false;
	controls = true;
}
var start;
var finish;
var closedSet = [];
var path = [];
var grid = [];
function removeFromArray(openSet, current){
	for(var i = 0; i < openSet.length; i++){
		if(openSet[i] == current){
			openSet.splice(i, 1);
		}
	}
}

function checkInc(arr, param){
	var cont = false;
	for(var i = 0; i < arr.length; i++){
		if(param == arr[i]){
			cont = true;
		}
	}
	return cont;
}

function AiStar(){
	// Am I still searching?
	if (openSet.length > 0) {
		// Best next option
		var winner = 0;
		for (var i = 0; i < openSet.length; i++) {
			if (openSet[i].f < openSet[winner].f) {
				winner = i;
			}
		}
		var current = openSet[winner];
		// Did I finish?
		if (current === finish) {
			path.unshift(finish)
			path.pop()
			return
		}
		// Best option moves from openSet to closedSet
		removeFromArray(openSet, current);
		closedSet.push(current);
		// Check all the neighbors
		var neighbors = current.Neighbours;
		for (var i = 0; i < neighbors.length; i++) {
			var neighbor = neighbors[i];
			// Valid next spot?
			if (!checkInc(closedSet, neighbor) && !(neighbor.isWall)) {
				var tempG = current.g + heuristic(neighbor, current);
				// Is this a better path than before?
				var newPath = false;
				if (checkInc(openSet, neighbor)) {
					if (tempG < neighbor.g) {
						neighbor.g = tempG;
						newPath = true;
					}
				} else {
					neighbor.g = tempG;
					newPath = true;
					openSet.push(neighbor);
				}
				// Yes, it's a better path
				if (newPath) {
					neighbor.h = heuristic(neighbor, finish);
					neighbor.f = neighbor.g + neighbor.h;
					neighbor.previous = current;
				}
			}
		}
	// Uh oh, no solution
	} else {
		con = true;
		return;
	}
	// Find the path by working backwards
	path = [];
	var temp = current;
	path.push(temp);
	if(temp){
		while (temp.previous) {
			path.push(temp.previous);
			temp = temp.previous;
		}
	}
	AiStar();
}
function CellProp(x, y){
	this.previous = undefined;
	this.x = x;
	this.y = y;
	this.isWall = false;
	this.Neighbours = []
	this.AddNeigh = function(){
		if(y > 0){
			this.Neighbours.push(grid[x][y-1]);
		}
		if(y < 19){
			this.Neighbours.push(grid[x][y+1]);
		}
		
		if(x > 0){
			this.Neighbours.push(grid[x-1][y]);
		}
		
		if(x < 19){
			this.Neighbours.push(grid[x+1][y]);
		}
	}
	this.g = 0; //Total path distance
	this.h = 0; //Cost of next node - Heuristic
	this.f = 0; //g + h
	//Wikipedia A* algorithm
}
function heuristic(Nei, Cur){
	var d = dist(Nei.x, Nei.y, Cur.x, Cur.y);
	return d;
}