//Lets the background loop through 4 images with fade between them
var imgNum = 1;
var opacity = 1;
function fadeIn(){
	opacity += 0.01;
	document.getElementById('fade1').style.opacity = opacity;
	if(opacity >= 0.99){
		var temp = imgNum + 1;
		if(temp > 4){
			temp = 1;
		}
		document.getElementById("fade2").style.backgroundImage = "url(./img/" + temp + ".jpg)";//Background stays fixed 
		document.getElementById("fade2").style.backgroundRepeat = "no-repeat";
		setTimeout(fadeOut, 10000); //Changes the background image every 10 seconds
	} else {
		setTimeout(fadeIn, 10); //Pseed at which the opacity wichange.
	}
}
function fadeOut(){
	opacity -= 0.01;
	document.getElementById('fade1').style.opacity = opacity;
	if(opacity <= 0.01){
		imgNum += 1;
		if(imgNum > 4){
			imgNum = 1;
		}
		document.getElementById("fade1").style.backgroundImage = "url(./img/" + imgNum + ".jpg)";
		document.getElementById("fade1").style.backgroundRepeat = "no-repeat";
		fadeIn();
	} else {
		setTimeout(fadeOut, 10);
	}
}


//Arrays for name conversion
var id2css =	[
					["B-Top", "border-top-width"],
					["B-Right", "border-right-width"],
					["B-Bot", "border-bottom-width"],
					["B-Left", "border-left-width"],
					["S-Top", "border-top-style"],
					["S-Right", "border-right-style"],
					["S-Bot", "border-bottom-style"],
					["S-Left", "border-left-style"],
					["borderCol", "border-color"],
					["backgroundCol", "background-color"],
					["boxShadow", "box-shadow"],
					["B-Radius", "border-radius"],
					["M-Top", "margin-top"],
					["M-Right", "margin-right"],
					["M-Bot", "margin-bottom"],
					["M-Left", "margin-left"],
					["P-Top", "padding-top"],
					["P-Right", "padding-right"],
					["P-Bot", "padding-bottom"],
					["P-Left", "padding-left"],
					["boxWidth", "width"],
					["boxHeight", "height"],
					["Text-Font", "font-family"],
					["Text-Align", "text-align"],
					["Text-Size", "font-size"],
					["Text-Colour", "color"],
					["img-src", "background-image"]
				];
var tag2css = 	[
					["Table", "table"],
					["Bullet", "ul"],
					["Number", "ol"],
					["Bar", "input[type='text']"],
					["Box", "textarea"],
					["Button", "button"],
					["Check", "input[type='checkbox']"],
					["Date", "input[type='date']"],
					["Radio", "input[type='radio']"],
					["Heading", "h1"],
					["Paragraph", "p"],
					["Image", "img"],
					["Divide", "div"]
				];
var dropdown = 	[
					["Table", "bm", "ts", "fs"],
					["Bullet", "bm", "fs"],
					["Number", "bm", "fs"],
					["Bar", "bm", "fs"],
					["Box", "bm", "fs"],
					["Button", "bm", "fs"],
					["Check", "bm", "fs"],
					["Date", "bm", "fs"],
					["Radio", "bm", "fs"],
					["Heading", "bm", "fs"],
					["Paragraph", "bm", "fs"],
					["Image", "bm", "is"],
					["Divide", "bm", "fs"]
				];
var savedData = []; //[[Tag, [[style1, data], [style2, data], [style3, data]]], [Tag, [[style1, data], [style2, data]]]]; example of saved data
var savedValue = []; //saves all the input data in an array for each tag
var imgSrc = ""; //Stores the url for the image tag
var currData = ""; //Stores the last input CSS data into the iframe
var currHTML = ""; //Stores the last input HTML data into the iframe

//Populates the first items in the arrays with the names of every tag
function setup(){
	var tagList = new Array();
	tagList = document.getElementsByClassName("tag");
	for(var i = 0; i < tagList.length; i++){
		savedData[i] = [tagList[i].innerHTML, []];
		savedValue[i] = [tagList[i].innerHTML, ""];
	}
}

//For showing and hiding the dropdown buttons
function toggleChild(id){
	var newID = id + "Child";
	var pressed = document.getElementById(newID);
	if (pressed.style.display === "none"){
		pressed.style.display = "inline-block";
	} else {
		pressed.style.display = "none";
	}	
}
//Gives the dropdown button an underline when dropdown shown
function toggleUnderline(id){
	var buttonEl = document.getElementById(id)
	if (buttonEl.style.textDecoration === "none"){
		buttonEl.style.textDecoration = "underline";
	} else {
		buttonEl.style.textDecoration = "none";
	}	
}
//Selecting a tag from the tag panel. It works out what tag you chose and proceeds to update the rest of the screen accordingly
function injectTag(id){
	var selEl = document.getElementById(id);
	var tagName = selEl.id;
	if(tagName.substring(0, 5) == "input"){
		tagName = tagName.substring(0,5) + " " + tagName.substring(5, tagName.length)
	}
	var classList = selEl.className.split(" ");
	var isSelected = false;
	for(i = 0; i < classList.length; i++){
		if(classList[i] === 'selected'){
			isSelected = true;
		}
	}
	//Making sure a tag has been previously selected
	if(!isSelected){
		var prevSelect = document.getElementsByClassName("selected");
		if(typeof prevSelect[0] != 'undefined'){
			prevSelect[0].classList.remove("selected");
		}
		selEl.classList.add("selected");
	}
	//Picking the style options to show/hide
	var tagText = selEl.innerHTML;
	document.getElementById("bm").style.display = "none";
	document.getElementById("ts").style.display = "none";
	document.getElementById("fs").style.display = "none";
	document.getElementById("is").style.display = "none";
	for(i = 0; i < dropdown.length; i++){
		for(j = 1; j < dropdown[i].length; j++){
			if(dropdown[i][0] == selEl.innerHTML){
				document.getElementById(dropdown[i][j]).style.display = "inline-block";
			}
		}
	}
	load(tagText);
	updateValues();
}
//For when the data in the text area changes, it inputs it into the iframe straight away
function insertValue(id){
	var tb = document.getElementById(id);
	var data = tb.value;
	var selected = document.getElementsByClassName("selected")[0].innerHTML;

	for(i = 0; i < savedValue.length; i++){
		if(savedValue[i][0] == selected){
			savedValue[i][1] = data;
		}
	}
	load(selected)
}
//Stores the data in the format stated above
function save(tagType, styleTag, data2Save){
	var load = savedData;
	for(findTag = 0; findTag < load.length; findTag++){
		if(load[findTag][0] == tagType){
			for(findStyle = 0; findStyle < load[findTag][1].length; findStyle++){
				//Adds style if it is not in the list, amends it if it is.
				var styleFound = false;
				if(load[findTag][1][findStyle][0] == styleTag){
					load[findTag][1][findStyle][1] = data2Save;
					styleFound = true;
					return;
				}
			}
			if(!styleFound){
				load[findTag][1][load[findTag][1].length] = [styleTag, data2Save];
				return;
			}
			if(load[findTag][1].length == 0){
				load[findTag][1][0] = [styleTag, data2Save];
				return;
			}
			break;
		}
	}
}
//Loads the data and converts it into suitable css
function load(tagType){
	var load = savedData;
	var locate;
	var inData = false;
	for(i = 0; i < load.length; i++){
		if(load[i][0] == tagType){
			inData = true;
			locate = i;
		}
	}
	var cssMaster;
	for(i = 0; i < tag2css.length; i++){
		if(tagType == tag2css[i][0]){
			cssMaster = tag2css[i][1];
		}
	}
	var styleApp = cssMaster + "{";
	if(inData){
		for(j = 0; j < load[locate][1].length; j++){
			for(i = 0; i < id2css.length; i++){
				if(id2css[i][0] == load[locate][1][j][0]){
					styleApp += id2css[i][1] + ": "+ load[locate][1][j][1] + "; ";
				}
			}
		}
	}
	styleApp += "}";
	currData = styleApp;
	updateScreen(tagType, cssMaster, styleApp);
}
//Updates the values in text boxes etc when a new tag is selected
function updateValues(){
	//sets all to 0 or empty
	var allNum = document.getElementsByClassName("inputArea num");
	for(i = 0; i < allNum.length; i++){
		allNum[i].value = "0";
	}
	var allColour = document.getElementsByClassName("inputArea colour");
	for(i = 0; i < allColour.length; i++){
		allColour[i].value = "FFFFFF";
		setBorderColour(allColour[i].id);
	}
	var allDrop = document.getElementsByClassName("inputArea drop");
	for(i = 0; i < allDrop.length; i++){
		allDrop[i].value = "";
	}
	var allText = document.getElementsByClassName("inputArea text");
	for(i = 0; i < allText.length; i++){
		allText[i].value = "";
	}
	//Populates with stored data
	var load = savedData;
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	for(i = 0; i < load.length; i++){
		if(load[i][0] == selected){
			for(j = 0; j < load[i][1].length; j++){
				var styleTag = load[i][1][j][0];
				var data = load[i][1][j][1];
				if(styleTag == "boxShadow"){
					var newData = data.split(" ");
					document.getElementById("Z-Horizontal").value = newData[0].substring(0,newData[0].length-2);
					document.getElementById("Z-Vertical").value = newData[1].substring(0,newData[1].length-2);
					document.getElementById("Z-Colour").value = newData[3].substring(1,newData[3].length);
					document.getElementById("Z-Blur").value = newData[2].substring(0,newData[2].length-2);
					break;
				} else {
					document.getElementById(styleTag).value = data;
				}
				if(styleTag.substring(0,2) === styleTag.substring(0,1) + "-"){
					//is a child of a main
					if(!((styleTag.substring(0,1) == "R") || (styleTag.substring(0,1) == "Z"))){
						//is not radius
						var temp = styleTag.substring(0,2);
						var l = document.getElementById(temp + "Left");
						var r = document.getElementById(temp + "Right");
						var t = document.getElementById(temp + "Top");
						var b = document.getElementById(temp + "Bot");
						if(t.value == r.value && r.value == b.value && b.value == l.value){
							document.getElementById(styleTag.substring(0,1) + "-All").value = data;
						} else {
							document.getElementById(styleTag.substring(0,1) + "-All").value = "CUSTOM";
						}
					}
				}
			}
		}
	}
	//Updating the textarea
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	for(i = 0; i < savedValue.length; i++){
		if(savedValue[i][0] == selected){
			document.getElementById("customData").value = savedValue[i][1];
			break
		} else {
			document.getElementById("customData").value = "";
		}
	}
}

function updateScreen(tagType, cssMaster, styleApp){
	var buttons = document.getElementsByClassName("tag");
	for(i = 0; i < buttons.length; i++){
		if(buttons[i].innerHTML == tagType){
			tagType = buttons[i].id;
			break;
		}
	}
	if(tagType.substring(0, 5) == "input"){
		tagType = tagType.substring(0,5) + " " + tagType.substring(5, tagType.length)
	}
	var body = $('#mainFrame').contents().find("body");
	var head = $('#mainFrame').contents().find("head");
	body.empty();
	head.empty();
	var data = "";
	var toDisplay = "";
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	for(i = 0; i < savedValue.length; i++){
		if(savedValue[i][0] == selected){
			data = savedValue[i][1];
		}
	}
	if(tagType == "Table"){
	//Data structure: Comma separated. 
		var csd = data.split(",");
		data = ">";
		toDisplay = "&gt;"
		var sliderCol = document.getElementById("Tbl-X");
		var sliderRow = document.getElementById("Tbl-Y");
		for(i = 0; i < sliderRow.value; i++){
			data += "<tr>";
			toDisplay += "&lt;tr&gt;"
			for(j = 0; j < sliderCol.value; j++){
				if(((i * sliderCol.value) + j) < csd.length){
					data += "<td>";
					toDisplay += "&lt;td&gt;"
					data += csd[(i * sliderRow.value) + j];
					toDisplay += csd[(i * sliderRow.value) + j];
					data += "</td>";
					toDisplay += "&lt;/td&gt;"
				} else {
					data += "<td>";
					toDisplay += "&lt;td&gt;"
					data += "</td>";
					toDisplay += "&lt;/td&gt;"
				}
			}
			data += "</tr>";
			toDisplay += "&lt;/tr&gt;"
		}
		data += "</";
		toDisplay += "&lt;/"
	} else if(tagType == "img"){
		data = " src='" + imgSrc + "'>" + data + "</";
		toDisplay = " src='" + imgSrc + "'&gt;" + data + "&lt;/";
	} else if(tagType == "ul"){
		var csd = data.split(",");
		data = ">";
		toDisplay = "&gt;"
		for(i = 0; i < csd.length; i++){
			data += "<li>" + csd[i] + "</li>";
			toDisplay += "&lt;li&gt;" + csd[i] + "&lt;/li&gt;";
		}
		data += "<";
		toDisplay += "&gt;"
	} else if(tagType == "ol"){
		var csd = data.split(",");
		data = ">";
		toDisplay = "&gt;"
		for(i = 0; i < csd.length; i++){
			data += "<li>" + csd[i] + "</li>";
			toDisplay += "&lt;li&gt;" + csd[i] + "&lt;/li&gt;";
		}
		data += "<";
		toDisplay += "&lt;";
	} else {
		data = ">" + data + "</";
		toDisplay = "&gt;" + data + "&lt;/";
	}
	var bodyText = "<section class='wrapper'><" + tagType + data + tagType + "></section>";
	currHTML = "&lt;" + tagType + toDisplay + tagType + "&gt";
	body.append(bodyText);
	styleText = "<style>" + styleApp + "</style>";
	head.append(styleText);
	var iFrameDOM = $("#mainFrame").contents();
	if(tagType.includes("input")){cssMaster = "input"}
	var hei = iFrameDOM.find(cssMaster).height();
	var wid = iFrameDOM.find(cssMaster).width();
	//hei += (iFrameDOM.find(cssMaster).marginTop + iFrameDOM.find(cssMaster).marginBottom) / 2;
	var framewid = $('#mainFrame').width()
	var framehei = $('#mainFrame').height()
	var widdiff = (framewid - wid) / 2;
	var heidiff = (framehei - hei) / 2;
	var styleText = "<style> body{margin: 0; padding: 0} .wrapper{position: absolute; width: " + wid + "; left: " + widdiff + "px; top: " + heidiff + "px;}</style>";
	head.append(styleText);
}
function sizeAdj(id){ //onkeyup
	//remove letters
	var input = document.getElementById(id);
	var getEdit = id.substring(0,1);
	var arrInput = input.value.split('');
	var rebuild = "";
	for(i = 0; i < arrInput.length; i++){
		for(j = 0; j < 10; j++){
			if(arrInput[i] == String(j)){
				rebuild += arrInput[i];
			}
		}
	}
	if(rebuild.length == 0){rebuild = 0};
	input.value = parseInt(rebuild);
	//update boxes
	var whole = document.getElementById(getEdit + "-All");
	var t = document.getElementById(getEdit + "-Top");
	var r = document.getElementById(getEdit + "-Right");
	var b = document.getElementById(getEdit + "-Bot");
	var l = document.getElementById(getEdit + "-Left");
	var changed = document.getElementById(id);
	if(changed == whole){
		t.value = whole.value
		r.value = whole.value
		b.value = whole.value
		l.value = whole.value
	}else{
		if(t.value == r.value && r.value == b.value && b.value == l.value){
			whole.value = changed.value
		} else {
			whole.value = "CUSTOM";
		}
	}
	
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	
	if(id.substring(0,1) == "R"){ //If setting the border radius
		styleopt = document.getElementById("R-Top").value + "px "
		styleopt += document.getElementById("R-Right").value + "px "
		styleopt += document.getElementById("R-Bot").value + "px "
		styleopt += document.getElementById("R-Left").value + "px"
		save(selected, "B-Radius", styleopt);
	} else {
		save(selected, t.id, t.value);
		save(selected, r.id, r.value);
		save(selected, b.id, b.value);
		save(selected, l.id, l.value);
	}
	
	
	
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
function colourInp(id){
	//Only allows hex input. After 3 or 6 characters have been input, the colour changes and the font changes to stand out
	var input = document.getElementById(id);
	var arrInput = input.value.split('');
	var rebuild = "";
	for(i = 0; i < arrInput.length; i++){
		for(j = 0; j < 16; j++){
			var check = j;
			if(j == 10){check = "a"};
			if(j == 11){check = "b"};
			if(j == 12){check = "c"};
			if(j == 13){check = "d"};
			if(j == 14){check = "e"};
			if(j == 15){check = "f"};
			var inp = arrInput[i].toLowerCase();
			if(inp == String(check)){
				rebuild += inp.toUpperCase();
			}
		}
	}
	input.value = rebuild;
	if(input.value.length == 3 || input.value.length == 6){
		var styleopt = "";
		var selected = document.getElementsByClassName("selected")[0].innerHTML;
		if(id == "Z-Colour"){ //For box shadow
			styleopt = document.getElementById("Z-Horizontal").value + "px "
			styleopt += document.getElementById("Z-Vertical").value + "px "
			styleopt += document.getElementById("Z-Blur").value + "px "
			styleopt += "#" + document.getElementById("Z-Colour").value
			id = "boxShadow";
			save(selected, id, styleopt);
		} else {
			var col = "#" + input.value
			save(selected, id, col);
		}
	}	
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
function dropAdj(id){
	//Updates all child text boxes, and child text boxes influence the parent text box
	var getEdit = id.substring(0, 1);
	var changed = document.getElementById(id);
	var whole = document.getElementById(getEdit + "-All");
	var t = document.getElementById(getEdit + "-Top");
	var r = document.getElementById(getEdit + "-Right");
	var b = document.getElementById(getEdit + "-Bot");
	var l = document.getElementById(getEdit + "-Left");
	if(changed == whole){
		whole.style.backgroundColor = "white";
		t.value = whole.value
		r.value = whole.value
		b.value = whole.value
		l.value = whole.value
	}
	changed.blur();
	
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	if(id != "Text-Align"){
		save(selected, t.id, t.value);
		save(selected, r.id, r.value);
		save(selected, b.id, b.value);
		save(selected, l.id, l.value);
	} else {
		save(selected, id, changed.value);
	}
	
	
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
function clearMe(id){
	//Clears the selectedf
	document.getElementById(id).value = "";
}
function setBorderColour(id){
	//Only allows hex input, hence formats text 
	var picker = document.getElementById(id);
	var test = document.getElementById(id + "Test");
	var colour = picker.value;
	if(colour.length == 3 || colour.length == 6){
		test.style.backgroundColor = "#"+colour;
		picker.style.backgroundColor = "#"+colour;
		var colParts = colour.split("");
		var rebuild = "";
		for(i = 0; i < colour.length; i++){
			for(j = 0; j < 16; j++){
				var check = j;
				if(j == 10){check = "A"};
				if(j == 11){check = "B"};
				if(j == 12){check = "C"};
				if(j == 13){check = "D"};
				if(j == 14){check = "E"};
				if(j == 15){check = "F"};
				var inp = colParts[i];
				if(inp == String(check)){
					var rev = j + 8;
					if(rev > 16){rev-=15};
					if(rev == 10){rev = "A"};
					if(rev == 11){rev = "B"};
					if(rev == 12){rev = "C"};
					if(rev == 13){rev = "D"};
					if(rev == 14){rev = "E"};
					if(rev == 15){rev = "F"};
					rebuild += rev
				}
			}
		}
		//Set the picker fot collour so they can sss how well that stands pit 
		test.style.color = "#"+rebuild;
		picker.style.color = "#"+rebuild;
	}else if(colour.length == 0){
		test.style.backgroundColor = "#FFFFFF";
		picker.style.backgroundColor = "#FFFFFF";
		test.style.color = "#"+"00000";
		picker.style.color = "#"+"000000";
	}
}
function numOnly(id){
	//For number only inputs. Formats to fit this case. 
	var input = document.getElementById(id);
	var arrInput = input.value.split('');
	var rebuild = "";
	if((arrInput[1] == "-") && (arrInput[0] == "0")){
		rebuild += arrInput[1];
		input.maxLength = 4;
	}else if(arrInput[0] == "-"){
		rebuild += arrInput[0];
		input.maxLength = 4;
	}else{
		input.maxLength = 3;
	}
	for(i = 0; i < arrInput.length; i++){
		for(j = 0; j < 10; j++){
			if(arrInput[i] == String(j)){
				if(!(i == 0 && arrInput[0] == "0")){
					rebuild += arrInput[i];
				}
			}
		}
	}
	if(rebuild.length == 0){rebuild = 0};
	input.value = parseInt(rebuild);
	if(input.value == "NaN"){
		input.value = rebuild;
	}
	
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	var styleopt = "";
	input = document.getElementById(id);
	if(input.id.substring(0,1) == "Z"){ //For box shadow
		styleopt = document.getElementById("Z-Horizontal").value + "px "
		styleopt += document.getElementById("Z-Vertical").value + "px "
		styleopt += document.getElementById("Z-Blur").value + "px "
		styleopt += "#" + document.getElementById("Z-Colour").value
		id = "boxShadow";
		save(selected, id, styleopt);
	}else if(input.id == "boxWidth"){
		save(selected, id, input.value);
	}else if(input.id == "boxHeight"){
		save(selected, id, input.value);
	}else if(input.id == "Text-Size"){
		save(selected, id, input.value);
	}
	
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
function sliderChange(id){
	var newID = id + "-Test";
	var slider = document.getElementById(id);
	var test = document.getElementById(newID);
	test.innerHTML = slider.value;
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	save(selected, id, slider.value);
	
	insertValue("customData");
	
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
function textInp(id){
	var data = document.getElementById(id).value;
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	save(selected, id, data);
	
	load(selected);
}
function srcInp(id){
	var data = document.getElementById(id).value;
	imgSrc = data;
	var tagName = document.getElementsByClassName("selected")[0].innerHTML;
	load(tagName);
}
//[[Tag, [[style1, data], [style2, data]]], [Tag, [[style1, data], [style2, data]]]];
function wipeData(){
	if(window.confirm("Are you sure you want to wipe your data for this element?")) {
		var selected = document.getElementsByClassName("selected");
		if(typeof selected[0] != 'undefined'){
			for(i = 0; i < savedData.length; i++){
				if(savedData[i][0] == selected[0].innerHTML){
					savedData[i][1] = [[],[]];
					load(selected[0].innerHTML);
					clearAll();
				}
			}
		}
    }
}
function clearAll(){
	var allNum = document.getElementsByClassName("inputArea num");
	for(i = 0; i < allNum.length; i++){
		allNum[i].value = "0";
	}
	var allColour = document.getElementsByClassName("inputArea colour");
	for(i = 0; i < allColour.length; i++){
		allColour[i].value = "FFFFFF";
		setBorderColour(allColour[i].id);
	}
	var allDrop = document.getElementsByClassName("inputArea drop");
	for(i = 0; i < allDrop.length; i++){
		allDrop[i].value = "";
	}
	var allText = document.getElementsByClassName("inputArea text");
	for(i = 0; i < allText.length; i++){
		allText[i].value = "";
	}
	var selected = document.getElementsByClassName("selected")[0].innerHTML;
	for(i = 0; i < savedValue.length; i++){
		if(savedValue[i][0] == selected){
			savedValue[i][1] == "";
			document.getElementById("customData").value = "";
		}
	}
}
function generateCode(){
	document.getElementById("backOff").style.display = "block";
	var back = document.getElementById("backOff");
}
function hideOverlay(id){
	document.getElementById(id).style.display = "none";
}

var butt = document.getElementsByTagName("button");
//Data for all the tags in the tag panel along with examples
var Table = ["Table", ["&lt;table&gt;", "A simple method of data storage and visual representation of said storage. Tables are split into columns and rows.<br/><br/> <style>table,td,tr{margin: auto; border: 1px solid black; border-collapse: collapse;}</style><table><tr><td>(0, 0)</td><td>(0, 1)</td></tr><tr><td>(1, 0)</td><td>(1, 1)</td></tr></table> <br/><i><small>To input data into this tag, use the text field in the bottom right and separate data with a comma ( , )</small></i>"]];
var Bullet = ["Bullet", ["&lt;ul&gt;", "A bullet point style list. Like a table it is a simple store of data along with display, however the data only flows down, not across. This can, however, be changed with more in-depth css which sadly is not offered here but suitable tutorials can be found on the <a href='https://www.w3schools.com'>W3 Schools Website</a>. <br/><ul><li>1</li><li>2</li><li>3</li></ul><i><small>You can change the point style in the CSS section to the right under 'List Options'<br/>List items are separated by a comma ( , ) in the text box in the bottom right</small></i>"]];
var Num = ["Number", ["&lt;ol&gt;", "See Bullet, Number lists are exactly the same however the one defining difference is that they are numbered. Much like a list of instructions, you make the reader go from start to finish in the order specified.<br/><ol><li>1</li><li>2</li><li>3</li></ol><i><small>You can change the counter style (eg.: A B C) in the CSS section to the right<br/>List items are separated by a comma ( , ) in the text box in the bottom right</small></i>"]];
var Bar = ["Bar", ["&lt;input type='text'&gt;", "A primitive form of input. Allows all characters and be of any length (unless specified). Most commonly used in forms as a method for sending data to a database / server. Input tags do not require a closing tag.<br/><br/><input type='text' style='width:80%; height:auto' placeholder='Type here...'><br/><br/><i><small>There are methods for changing the style of the borders when the box is active but these methods are not available on this website :(</small></i>"]];
var Box = ["Box", ["&lt;textarea&gt;", "Text areas are like large, adjustable text boxes. They allow for much more data to be added along with wrapping capabilities to keep all the text you enter visible. They come with a dragable corner to increase or decrease size. <br/><br/><textarea style='width:80%; height:auto'></textarea><br/><br/><i><small>The dragabble corner can be disabled but this is not a feat achieved by css. It must be done directly in the tag</small></i>"]];
var But = ["Button", ["&lt;button&gt;", "The most classic form of input. Used in a veriety of ways from sending a message to posting a form to doing absolutely nothing. Buttons are an easy key to the use of JavaScript, a front-end language used by most websites, due to their simplistic way of initiating functions.<br/><br/><button style='width:80%; height:auto'>Click Me!</button><br/><br/><i><small>This website is for testing CSS only so custom functions with buttons is not achieveable here.</small></i>"]];
var Check = ["Check", ["&lt;input type='checkbox'&gt;", "On its own, a simple on / off switch. But when paired with others, a checkbox array can give an exponential amount of outputs. Check as many as you want or none at all. Who cares, it's your choice, right?<br/><br/><input type='checkbox'><input type='checkbox' checked><br/><br/><i><small>You can only create one checkbox at a time using this tool so keep that in mind. Sadly this means you can't paint a pixel version of the Mona Lisa</small></i>"]];
var Dat = ["Date", ["&lt;input type='date'&gt;", "Why bother relying on the user to type in their date of birth in the format you want when you can force you and them to use the same peice of data. <br/><br/><input type='date' style='width:80%; height:auto'><br/><br/><i><small>This tool only allows for the customisation of the bar itself, not the date picker attached. I'm not a wizard</small></i>"]];
var Radio = ["Radio", ["&lt;input type='radio'&gt;", "Much like their cousin, the checkbox, these nifty little fellas allow you to click them. But there's a catch. These bad boys never want to be on at the same time as their friends. To link radio buttons together, you need to give them all the same name, as if they were a family.<br/><br/><input type='radio' style='width:20%; height:auto'><br/><br/><i><small>There is no way to select a radio button through html so the value from the name family is extracted by the form they are used in.</small></i>"]];
var Heading = ["Heading", ["&lt;h1&gt; &lt;h2&gt; &lt;h3&gt;", "A larger font variant to the younger paragraph tag. Headings are mostly used for titles on webpages, or sub-titles withe the smaller variants. Although decieveing, H1 is the biggest of the tags.<br/><br/><h1>Hello World!</h1><br/><i><small>The text size for the headings is achieved by the 'large' text size. However we only allow numerical inputs only</small></i>"]];
var Paragraph = ["Paragraph", ["&lt;p&gt;", "Basic method of displaying text within a website. Paragraph tags start off with an initial margin above and below to act as line spacing and span the full length of the container they are in.<br/><br/><p>Hello World!</p><br/><i><small>Keep in mind the paragraphs have little side padding and removing this will stick the text to the edge of your container</small></i>"]];
var Image = ["Image", ["&lt;img&gt;", "A straight forward way of displaying an image of pretty much all image file types. Just create an image tag and give it a location to search, either local or over the internet, and out pops your image. <br/><br/><img src='./img/example.jpg' width='40%'/><bt/><br/><i><small>The image will default to the size of the image unless changed. Changing one value will keep the same ratio whereas changing 2 will completel yoverwrite the size and some information may be lost</small></i>"]];
var Divide = ["Divide", ["&lt;div&gt;", "Its main purpose is to split up your web page into manageable sections, however, provide this with some style properties and watch your website morph and mould as all children will inherit its style.<br/><br/><div style='margin: auto;height: 50px; width: 50px; background-color: #0b0; border: 1px solid black;'></div><br/><br/><i><small>The inheritance of a div can come at a cost as you may end up needing to include more style options to reverse changes made by a div. All in the name of style though</small></i>"]];
var tagList = [Table, Bullet, Num, Bar, Box, But, Check, Dat, Radio, Heading, Paragraph, Image, Divide];

//Adds event listeners for when the mouse goes on and off a button from the tag panel
for(i = 0; i < butt.length; i++){
	butt[i].addEventListener("mouseover", function(event){
		var popbox = document.getElementById("popbox");
		for(i = 0; i < tagList.length; i++){
			if(tagList[i][0] == event.path[0].innerHTML){
				popbox.style.display = "block";
				popbox.childNodes[0].innerHTML = tagList[i][1][0];
				popbox.childNodes[1].innerHTML = tagList[i][1][1];
				break;
			} else {
				popbox.style.display = "none";
			}
		}
	});
	butt[i].addEventListener("mouseout", function(event){
		var popbox = document.getElementById("popbox");
		popbox.style.display = "none";
	});
}
//Makes the info box follow the mouse at all times
window.addEventListener("mousemove", function(event){
	var x = event.clientX + 10;
	var y = event.clientY + 10;
	var popbox = document.getElementById("popbox");
	var windowHeight = window.innerHeight;
	if(y > windowHeight * 0.70){
		popbox.style.left = x + "px";
		popbox.style.bottom = 0 + "px";
	} else {
		popbox.style.bottom = null;
		popbox.style.left = x + "px";
		popbox.style.top = y + "px";
	}
});

//Makes the info box stick to the bottom so that it doesnt disappear at the bottom of the screen
document.getElementById("popbox").addEventListener("resize", function(){
	var botPos = document.getElementById("popbox").style.top + document.getElementById("popbox").style.height;
	var windowHeight = window.innerHeight;
});


//Stores all the data for the hoverover info box to display along with what tag it activates over.
var BGC = ["Background Colour", ["background-color"		, "Sets the colour for the background of the element"]];
var BWA = ["Border Width", ["border-width"				, "Adjusts the border on all 4 sides to be even<br/><i><small>Click to expand</small></i>"]];
var BWT = ["Border Top", ["border-width-top"			, "Only change the size of the top border"]];
var BWL = ["Border Left", ["border-width-left"			, "Only change the size of the left border"]];
var BWB = ["Border Bottom", ["border-width-bottom"		, "Only change the size of the bottom border"]];
var BWR = ["Border Right", ["border-width-right"		, "Only change the size of the right border"]];
var BSA = ["Border Style", ["border-style"				, "Sets the border style, which changes how the border looks<br/><i><small>Click to expand</small></i>"]];
var BST = ["Top Style", ["border-style-top"				, "Only change the style of the top border"]];
var BSL = ["Right Style", ["border-style-right"			, "Only change the style of the right border"]];
var BSB = ["Bottom Style", ["border-style-bottom"		, "Only change the style of the bottom border"]];
var BSR = ["Left Style", ["border-style-left"			, "Only change the style of the left border"]];
var BCL = ["Border Colour", ["border-colour"			, "Sets the colour for the whole border"]];
var BZA = ["Box Shadow", ["box-shadow"					, "Applys a drop shadow to the element. Takes up the same shape as said element<br/><i><small>Click to expand</small></i>"]];
var BZV = ["Vertical Position", ["Part of box-shadow"	, "Adjust the position vertically of the shadow.<i><small>Can be negative</small></i>"]];
var BZH = ["Horizontal Position", ["Part of box-shadow"	, "Adjust the position horizontally of the shadow.<i><small>Can be negative</small></i>"]];
var BZC = ["Colour", ["Part of box-shadow"				, "Sets the colour of the drop shadow"]];
var BZD = ["Dissolve", ["Part of box-shadow"			, "Gives the shadow a blur effect which increases as value increases"]];
var BRA = ["Border Radius", ["border-radius"			, "Rounds the edges of the border.<br/><i><small>Use this to make circles etc<br/>Click to expand</small></i>"]];
var BRT = ["Top Left", ["Part of border-radius"			, "Sets the curvature of the top right corner of the element"]];
var BRR = ["Top Right", ["Part of border-radius"		, "Sets the curvature of the top left corner of the element"]];
var BRL = ["Bottom Left", ["Part of border-radius"		, "Sets the curvature of the bottom right corner of the element"]];
var BRB = ["Bottom Right", ["Part of border-radius"		, "Sets the curvature of the bottom left corner of the element"]];
var MA = ["Margin", ["margin"							, "Spaces the element away from surrounding elements from outside the border<br/><i><small>Click to expand</small></i>"]];
var MT = ["Margin Top", ["margin-top"					, "Only sets the spacing for the top of the element"]];
var MR = ["Margin Right", ["margin-right"				, "Only sets the spacing for the right of the element"]];
var MB = ["Margin Bottom", ["margin-bottom"				, "Only sets the spacing for the bottom of the element"]];
var ML = ["Margin Left", ["margin-left"					, "Only sets the spacing for the left of the element"]];
var PA = ["Padding", ["padding"							, "Like margin, sets spacing but from inside the border to space the content away from it.<br/><i><small>Click to expand</small></i>"]];
var PT = ["Padding Top", ["padding-top"					, "Only sets the spacing for the top of the content"]];
var PR = ["Padding Right", ["padding-right"				, "Only sets the spacing for the right of the content"]];
var PB = ["Padding Bottom", ["padding-bottom"			, "Only sets the spacing for the bottom of the content"]];
var PL = ["Padding Left", ["padding-left"				, "Only sets the spacing for the left of the content"]];
var BW = ["Width", ["width"								, "Sets the overall width of the content of the element"]];
var BH = ["Height", ["height"							, "Sets the overall height of the content of the element"]];
var FF = ["Font", ["font-family"						, "Changes the font face for the element"]];
var FA = ["Align", ["text-align"						, "Shifts the text to the locations stated"]];
var FS = ["Font Size", ["font-size"						, "Increase or decrease the size of the text"]];
var FC = ["Font Colour", ["color"						, "Change the colour of the text"]];
var styleList = [BGC, BWA, BWT, BWL, BWB, BWR, BSA, BST, BSR, BSB, BSL, BCL, BZA, BZV, BZH, BZC, BZD, BRA, BRT, 
					BRL, BRB, MA, MT, MR, MB, ML, PA, PT, PR, PB, PL, BW, BH, FF, FA, FS, FC];


var labs = document.getElementsByClassName("styleLabel");
//Making move on and off events for the mouse going over the style tag labels
for(i = 0; i < labs.length; i++){
	labs[i].addEventListener("mouseover", function(event){
		var popbox = document.getElementById("popbox");
		for(i = 0; i < styleList.length; i++){
			if(styleList[i][0] == event.path[0].innerHTML){
				popbox.style.display = "block";
				popbox.childNodes[0].innerHTML = styleList[i][1][0];
				popbox.childNodes[1].innerHTML = styleList[i][1][1];
				break;
			} else {
				popbox.style.display = "none";
			}
		}
	});
	labs[i].addEventListener("mouseout", function(event){
		var popbox = document.getElementById("popbox");
		popbox.style.display = "none";
	});
}
