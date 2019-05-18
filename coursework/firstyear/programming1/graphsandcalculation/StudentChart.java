
public class StudentChart {

	public void drawBars(double[] marks) {
		int location = 5; //Define the starting coordinate of the first bar
		//The Axes
		Bar y = new Bar(); //Create an X axis
		Bar x = new Bar(); //Create a Y axis
		x.changeSize(150, 5); //Set the size to 150 across 
		y.changeSize(5, 100); //Set the size to a length of 100, which is the size limit
		x.makeVisible(); //Display the axes
		y.makeVisible();
		x.changeColour(Colour.BLACK); //Set them both to black
		y.changeColour(Colour.BLACK);
		x.moveVertical(100); //Translate the x axis down so it is below the bars created
		//The Bars
		Bar moduleBars[] = new Bar[6]; //Create a bar array for each bar
		for(int i = 0; i < 6; i++) { //Go through the bar array
			moduleBars[i] = new Bar(); //Create a new bar object at each point
			if(marks[i] >= 70) { //If the marks for the respective bar are 70 or above (Which is a first class pass)
				moduleBars[i].changeColour(Colour.MAGENTA); //Set the colour to magenta
			} else if(marks[i] >= 40) { //Else if the score is above 40 but below 70
				moduleBars[i].changeColour(Colour.GREEN); //set the colour to green
			} else if(marks[i] >= 35) { //If the score is a pass by compensation
				moduleBars[i].changeColour(Colour.YELLOW); //Set the colour to yellow
			} else { //If they fail
				moduleBars[i].changeColour(Colour.RED); //Set the colour to red
			}
			moduleBars[i].changeSize(10, (int)marks[i]); //Set the size of the bars to the score
			moduleBars[i].moveHorizontal(location); //Translate across the x axis to split the bars out by a set amount
			moduleBars[i].moveVertical(100 - (int)marks[i]); //Translate the bars down based off the score so they originate from the bottom
			moduleBars[i].makeVisible();//Display the bars
			location += 15; //Translate the next bars across by 15
		}
	}

	public void printSummary(int[][] moduleMarks, String passType, double[] marks) {
		for(int i = 0; i < 6; i++) { //Goes through every module
			String extraEX = ""; //Creates an empty string to store the text extentions for the output for exam and coursework marks
			String extraCW = ""; 
			if(moduleMarks[i][0] >= 70) { //If the marks are above 70
				extraCW = ": First class pass"; //Set the string extension to the text 
			} else if(moduleMarks[i][0] >= 40) {
				extraCW = ": Pass"; 
			} else if(moduleMarks[i][0] > 35) {
				extraCW = ": Pass with compensation"; 
			} else {
				extraCW = ": Fail";
			}
			if(moduleMarks[i][0] >= 70) {
				extraEX = ": First class pass";
			} else if(moduleMarks[i][0] >= 40) {
				extraEX = ": Pass";
			} else if(moduleMarks[i][0] > 35) {
				extraEX = ": Pass with compensation";
			} else {
				extraEX = ": Fail";
			}
			//Formatted output to print out the information input and extensions 
			System.out.println("Your Coursework / Exam marks for the module CSC 102" + (i+1) + " are: "); 
			System.out.println(moduleMarks[i][0] + extraCW + " / " + moduleMarks[i][1] + extraEX);
			System.out.println("And your module mark was: " + marks[i] + "\n");
		}
		System.out.println("Overall you achieved a: " + passType); //Displays the overall result
	}
}
