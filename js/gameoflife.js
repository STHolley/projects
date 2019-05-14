var resolution = 50;
var grid;
var flag = 0;
var stateLock = 0;

function Pixel(x, y){
	
	this.x = x;
	this.y = y;
	this.previousState = false;
	this.state = false;
	this.nextState = false;
	this.neighbours;// = new Array(null, null, null, null, null, null, null, null);
	this.neighboursAlive = 0;
	this.setNeighbours = function(){
		//N-Ne-E-Se-S-Sw-W-Nw
		var x = this.x;
		var y = this.y;
		var north;
		var northEast;
		var east;
		var southEast;
		var south;
		var southWest;
		var west;
		var northWest;
		//4 Cardinal Directions
		if(x == 0){
			west = null;
		} else {
			west = grid[x-1][y];
		}
		if(x == grid.length - 1) {
			east = null;
		} else {
			east = grid[x+1][y];
		}
		if(y == 0){
			north = null;
		} else {
			north = grid[x][y-1];
		}
		if(y == grid.length - 1){
			south = null;
		} else {
			south = grid[x][y+1];
		}
		//8 Cardinal Directions
		if(x == 0 || y == 0){
			northWest = null;
		} else {
			northWest = grid[x-1][y-1];
		}
		if(x == grid.length - 1 || y == 0) {
			northEast = null;
		} else {
			northEast = grid[x+1][y-1];
		}
		if(y == grid.length - 1 || x == grid.length - 1){
			southEast = null;
		} else {
			southEast = grid[x+1][y+1];
		}
		if(y == grid.length - 1 || x == 0){
			southWest = null;
		} else {
			southWest = grid[x-1][y+1];
		}
		//Set them
		this.neighbours = {north, northEast, east, southEast, south, southWest, west, northWest};
		return this.neighbours;
	}
	this.checkNeighbours = function(){
		var counter = 0;
		if(this.neighbours.north != null && this.neighbours.north.state == true){
			counter++;
		}
		if(this.neighbours.east != null && this.neighbours.east.state == true){
			counter++;
		}
		if(this.neighbours.south != null && this.neighbours.south.state == true){
			counter++;
		}
		if(this.neighbours.west != null && this.neighbours.west.state == true){
			counter++;
		}
		if(this.neighbours.northEast != null && this.neighbours.northEast.state == true){
			counter++;
		}
		if(this.neighbours.southEast != null && this.neighbours.southEast.state == true){
			counter++;
		}
		if(this.neighbours.southWest != null && this.neighbours.southWest.state == true){
			counter++;
		}
		if(this.neighbours.northWest != null && this.neighbours.northWest.state == true){
			counter++;
		}
		
		this.neighboursAlive = counter;
	}
	//Rules:
	//1: Live && n<2 = dead
	//2: Live && n>3 = dead
	//3: Live && n==2||n==3 = live
	//4: Dead && n==3 = live
	this.runRules = function(){
		if((this.state == true && this.neighboursAlive < 2)
			|| (this.state == true && this.neighboursAlive > 3)){
			this.nextState = false; //Dead
		}
		if((this.state == true && (this.neighboursAlive == 3 || this.neighboursAlive == 2))
			|| (this.state == false && this.neighboursAlive == 3)){
			this.nextState = true; //Alive
		}
	}
	this.update = function(){
		this.previousState = this.state;
		this.state = this.nextState;
		this.nextState = false;
	}
	this.revert = function(){
		this.nextState = state;
		this.state = previous;
		this.previousState = false;
	}
}

function main(data){
	//Initialise the grid
	grid = new Array();
	for(var x = 0; x < resolution; x++){
		grid[x] = new Array();
		for(var y = 0; y < resolution; y++){
			if(data != null){
				var temp = new Pixel(x, y);
				try{
					temp.state = data[x][y];
					grid[x][y] = temp;
				} catch(e){
					grid[x][y] = temp;
				}
			} else {
				grid[x][y] = new Pixel(x, y);
			}
		}
	}
	//Set the neighbours after everything is initialised
	for(var x = 0; x < resolution; x++){
		for(var y = 0; y < resolution; y++){
			grid[x][y].setNeighbours();
		}
	}
	setScreen();
}

function changeValue(){
	if(stateLock == 0){
		var x = document.getElementById("x").value;
		var y = document.getElementById("y").value;
		grid[y][x].state = !(grid[y][x].state);
		if(grid[y][x].state == true){
			document.getElementById(x+","+y).className = "alive";
		} else {
			document.getElementById(x+","+y).className = "dead";
		}
	}
}

function gridToText(){
	var builder = "";
	for(var x = 0; x < grid.length; x++){
		for(var y = 0; y < grid.length; y++){
			if(grid[x][y].state == true){
				//builder += "<span id="+x+","+y+" onclick=\"clickValue(this)\">&#9930</span>";
				builder += "<span id="+x+","+y+" onmousedown=\"setFlag(1)\" onmouseup=\"setFlag(0)\" onclick=\"clickValue(this)\" onmouseover=\"dragOver(this)\" class=\"alive\"></span>";
			} else {
				//builder += "<span id="+x+","+y+" onclick=\"clickValue(this)\">&#9929</span>";
				builder += "<span id="+x+","+y+" onmousedown=\"setFlag(1)\" onmouseup=\"setFlag(0)\"onclick=\"clickValue(this)\" onmouseover=\"dragOver(this)\" class=\"dead\"></span>";
			}
		}
		builder += "\n";
	}
	return builder;
}

function gridToVals(){
	var builder = "";
	for(var x = 0; x < grid.length; x++){
		for(var y = 0; y < grid.length; y++){
			grid[x][y].checkNeighbours();
			builder += grid[x][y].neighboursAlive + " ";
		}
		builder += "\n";
	}
	return builder;
}

function gridToBinary(){
	var builder = "";
	for(var x = 0; x < grid.length; x++){
		for(var y = 0; y < grid.length; y++){
			if(grid[x][y].state == true){
				builder += "1";
			} else {
				builder += "0";
			}
		}
		builder += "\r\n";
	}
	return builder;
}

function setScreen(){
	var gridText = gridToText();
	document.getElementById("grid").innerHTML = gridText;
}

function nextGeneration(){
	for(var x = 0; x < grid.length; x++){
		for(var y = 0; y < grid.length; y++){
			grid[x][y].checkNeighbours();
			grid[x][y].runRules();
		}
	}
	for(var x = 0; x < grid.length; x++){
		for(var y = 0; y < grid.length; y++){
			grid[x][y].update();
			if(grid[x][y].state == true){
				document.getElementById(x+","+y).className = "alive";
			} else {
				document.getElementById(x+","+y).className = "dead";
			}
			
		}
	}
}

function clickValue(chosen){
	if(stateLock == 0){
		var id = chosen.id;
		var coords = chosen.id.split(",");
		var x = coords[1];
		var y = coords[0];
		grid[y][x].state = !(grid[y][x].state);
		chosen.className = "alive";
	}
}

function fileSubmitted(inp){
	var file = inp.files[0];
	readFileIntoMemory(file, function(filedata){
		main(parseFile(filedata.content));
	});
	document.getElementById("clear").reset();
}

function parseFile(data){
	var values = new Array();
	values[0] = new Array();
	var xpos = 0;
	var ypos = 0;
	for(var i = 0; i < data.length; i++){
		if(data[i] == 13){//13 = new line;
			xpos = 0;
			ypos++;
			values[ypos] = new Array();
		} else if(data[i] == 48){
			values[ypos][xpos] = false;
			xpos++;
		} else if(data[i] == 49){
			values[ypos][xpos] = true;
			xpos++;
		}
	}
	return values;
}

function binaryToBool(data){
	var values = new Array();
	values[0] = new Array();
	var xpos = 0;
	var ypos = 0;
	for(var i = 0; i < data.length; i++){
		if(data[i] == "\n"){
			xpos = 0;
			ypos++;
			values[ypos] = new Array();
		} else if(data[i] == 0){
			values[ypos][xpos] = false;
			xpos++;
		} else if(data[i] == 1){
			values[ypos][xpos] = true;
			xpos++;
		}
	}
	return values;
}

function readFileIntoMemory (file, callback) {
    var reader = new FileReader();
    reader.onload = function () {
        callback({
            name: file.name,
            size: file.size,
            type: file.type,
            content: new Uint8Array(this.result)
         });
    };
    reader.readAsArrayBuffer(file);
}

function countSize(){
	var counter = 0;
	for(var x = 0; x < resolution; x++){
		for(var y = 0; y < resolution; y++){
			if(grid[x][y].state == true){
				counter ++;
			}
		}
	}
	return counter;
}

function genAndDownload(){
	var today = new Date();
	var date = today.getDate() + "" + (today.getMonth() + 1) + "" + today.getFullYear();
	var filename = date + ".txt"//get date + .txt
	var text = gridToBinary();
	var element = document.createElement('a');
    element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    element.setAttribute('download', filename);
    element.style.display = 'none';
    document.body.appendChild(element);
	if(countSize() > 1){
		element.click();
	} else {
		alert("Make some modifications");
	}
    document.body.removeChild(element);
}

function changeGridSize(slider){
	//save grid state to be copied
	var state = binaryToBool(gridToBinary());
	resolution = slider.value;
	main(state);
	document.getElementById("res").InnerHTML = resolution;
}

function setFlag(val){
	flag = val;
}

function dragOver(chosen){
	if(flag == 1 && stateLock == 0){ //Mouse down and not blocked
		var id = chosen.id;
		var coords = chosen.id.split(",");
		var x = coords[1];
		var y = coords[0];
		grid[y][x].state = !grid[y][x].state;
		if(grid[y][x].state == true){
			chosen.className = "alive";
		} else {
			chosen.className = "dead";
		}
	}
}

function start(counter){
	if(counter > 0){
		stateLock = 1;
		document.getElementById("simulate").disabled = true;
		document.getElementById("runsim").disabled = true;
		setTimeout(function(){
			counter--;
			nextGeneration();
			start(counter);
		}, 100);
	} else {
		stateLock = 0;
		document.getElementById("simulate").disabled = false;
		document.getElementById("runsim").disabled = false;
	}
}

function runMultiple(){
	//One frame per millisecond hopefully
	var amount = document.getElementById("simulate").value;
	start(amount);
}