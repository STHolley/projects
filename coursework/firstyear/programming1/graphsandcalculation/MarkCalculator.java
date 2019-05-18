import java.util.Scanner;
public class MarkCalculator {
	int[] EXWEIGHTINGS = { 50, 60, 0, 50, 80, 65 }; // The percentage of which exam marks count to the grades per module
	int[] CWWEIGHTINGS = { 50, 40, 100, 50, 20, 35 }; // The percentage of which coursework marks count to the grades per module
	private static int MODULES = 6;
	int moduleMarks[][] = new int[6][2]; // Empty array with 6 rows, one for each module, and 2 columns, one for coursework and exam marksrespectively
	double finalMarks[] = new double[6]; // An empty array to store the final marks for each module
	boolean coreModules[] = { false, true, false, false, false, false };
	public int[][] inpMarks(int moduleMarks[][]) {
		for(int i = 0; i < MODULES; i++) { //Loops through every module
			if(CWWEIGHTINGS[i] != 0){
				Scanner scan = new Scanner(System.in); //Allowing for input of text
				System.out.println("Input the coursework marks for module 102" + (i+1) + ": ");
				if(scan.hasNextInt()) {
					int inp = scan.nextInt();
					if(inp > 100 || inp < 0) { //Check if the marks are in the range of 0 to 100
						System.out.println("Marks must be between 0 and 100"); //inform the user of the mistake
						i -= 1; //Re-run the loop at the same position
					} else {
						//Success!
						moduleMarks[i][0] = inp; //Sets the column for exam marks to the input
					}
				} else {
					System.out.println("Please enter a valid integer"); //inform the user of the mistake
					i -= 1;//Re-run the loop at the same position
				}
			}
		}
		for(int i = 0; i < MODULES; i++){
			if(EXWEIGHTINGS[i] != 0){
				Scanner scan = new Scanner(System.in); //Allowing for input of text
				System.out.println("Input the exam marks for module 102" + (i+1) + ": ");
				if(scan.hasNextInt()) {
					int inp = scan.nextInt();
					if(inp > 100 || inp < 0) { //Check if the marks are in the range of 0 to 100
						System.out.println("Marks must be between 0 and 100"); //inform the user of the mistake
						i -= 1; //Re-run the loop at the same position
					} else {
						//Success!
						moduleMarks[i][1] = inp; //Sets the column for coursework to the input
					}
				} else {
					System.out.println("Please enter a valid integer"); //inform the user of the mistake
					i -= 1;//Re-run the loop at the same position
				}
			}
		}
		for(int i = 0; i < MODULES; i++){ //Checks for if any coursework or exam marks were missed
			if(CWWEIGHTINGS[i] == 0){ //And populates it with the marks of the other type so the average comes out to the input score
				moduleMarks[i][0] = moduleMarks[i][1];
			} else if(EXWEIGHTINGS[i] == 0){
				moduleMarks[i][1] = moduleMarks[i][0];
			}
		}
		return moduleMarks; //Sends back a 2D array for the marks
	}
	public double[] computeMarks(int moduleMarks[][]) {
		for (int i = 0; i < MODULES; i++) { // Loops through every module
			int cWMark = moduleMarks[i][0]; // temp variable for the module marks
			int eXMark = moduleMarks[i][1]; // temp variable for the exam marks
			if (cWMark < 35 ^ eXMark < 35) { // if either of the module marks are below 35
				if (cWMark < eXMark) { // Set the mark to the lowest mark
					finalMarks[i] = cWMark;
				} else {
					finalMarks[i] = eXMark;
				}
			} else { // if neither of the module marks are below 35
				finalMarks[i] = ((cWMark * CWWEIGHTINGS[i]) + (eXMark * EXWEIGHTINGS[i])) / 100.0; // Compute the module marks
			}
		}
		return finalMarks;
	}
	public String computeResult(double[] markArray) {
		boolean noComp = true; //Checks if there are no passes by compensation
		boolean oneFail = false; //Checks if no modules are failed
		int noOfComps = 0; //Counts the number of passes by compensation
		double avg = 0; //Calculates the average score mark
		for(int i = 0; i < MODULES; i++){ //Loops through all the module marks
			if(markArray[i] < 40){ //If anything is less than a pass
				noComp = false; //Not all the modules are a pass
				if(markArray[i] > 35 && coreModules[i]){ //If anything is a pass by compensation and not core
					noOfComps++; //Increase the number of passes by compensation by one
				} else { //If something is a fail
					oneFail = true; //There is at least one fail
				}
			}
			avg += markArray[i]; //Increases the count by the score for finding the average
		}
		avg /= MODULES; //Divides the total score by the number of modules to give the average
		if(noComp){ //If all the modules are passes
			return "Pass"; //The overall result is a pass
		} else if(avg >= 40 && noOfComps <= 2 && !oneFail){ //If the criteria for a Compensative fail are met
			return "Compensative Fail"; //The overall result is a compensative fail
		} else {
			return "Fail"; //otherwise it is just a fail
		}
	}
}
