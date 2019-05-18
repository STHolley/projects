
public class testStudent {

	public static void main(String[] args) {
		int moduleMarks[][] = new int[6][2]; //Empty array with 6 rows, one for each module, and 2 columns, one for coursework and exam marks respectively 
		MarkCalculator marks = new MarkCalculator(); //Creates a new markCalculator object
		moduleMarks = marks.inpMarks(moduleMarks); //populates the moduleMarks 2D array with the returned data from the method for inputting marks
		double markArray[] = marks.computeMarks(moduleMarks); //Creates a 1D array that is populated with the module results from the computeMarks method
		String passType = marks.computeResult(markArray); //Stores the result of the stage as a string that comes from the computeResults method
		StudentChart sc = new StudentChart(); //Creates a new studentChart object for giving a summary
		sc.printSummary(moduleMarks, passType, markArray); //Prints out the whole summary
		sc.drawBars(markArray); //Draws a bar graph of the summary
	}

}
