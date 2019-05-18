
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

import java.util.ArrayList;
import java.util.Scanner;

public class HotelConfigure {
	public static void main(String[] args) {
		/*
		 * Starts off by initiating the name and size of the hotel and starts
		 * iterating through each room in the hotel based off the size input
		 */
		Scanner sc = new Scanner(System.in);
		HotelConfigure h = new HotelConfigure();
		h.print("What is the name of the hotel?");
		String hotelName = sc.next();
		while (hotelName.length() > 53) {
			h.print("Please shorten the name:");
			hotelName = sc.next();
		}
		h.print("How many rooms in this hotel?");
		int roomNum = h.validInt();
		Hotel myHotel = new Hotel(hotelName, roomNum);
		for (int i = 0; i < roomNum; i++) {
			h.print(String.format(
					"Enter the bed types for room %s separated by a comma (Bed types are 'Single (s)' or 'Double (d))':",
					i + 1));
			h.createRoom(myHotel);
		}
		myHotel.updateFacts();
		HotelReport report = new HotelReport(myHotel);
		report.writeReport();
		sc.close();
	}

	public void createRoom(Hotel myHotel) {
		/*
		 * Creates each room by taking valid inputs and creating new objects
		 */
		String[] allBedArr = checkBeds();
		ArrayList<Bed> bedList = new ArrayList<Bed>();
		for (int i = 0; i < allBedArr.length; i++) {
			bedList.add(new Bed(allBedArr[i]));
		}
		print("Is this room occupied? Yes(y)/No(n)");
		String str = checkYN();
		if (str.equals("y")) {
			myHotel.addRoom(bedList, true);
		} else {
			myHotel.addRoom(bedList, false);
		}
	}

	public int validInt() {
		/*
		 * Checks to see if the input is actually an integer first, then checks
		 * to see if it is above 0 for it to be valid, using a recursive method
		 */
		Scanner sc = new Scanner(System.in);
		if (!sc.hasNextInt()) {
			print("Please enter an integer:");
			return validInt();
		}
		int test = sc.nextInt();
		if (test < 1) {
			print("Please enter a value above 0:");
			return validInt();
		}
		return test;
	}

	public String checkYN() {
		/*
		 * Checks to see if a string input is of certain text, yes or no, y or
		 * n, and returns it if it is valid. Also uses a recursive method
		 */
		Scanner sc = new Scanner(System.in);
		String str = sc.nextLine().toLowerCase();
		if (str.equals("no")) {
			str = "n";
		} else if (str.equals("yes")) {
			str = "y";
		}
		if (!(str.equals("n") || str.equals("y"))) {
			print("Please enter \'Yes(y)\' or \'No(n)\':");
			return checkYN();
		}
		return str;
	}

	public String[] checkBeds() {
		/*
		 * Checks an input of comma separated entities to see if all match set
		 * words or characters and returns the string as an array of entities
		 * split by commas with all whitespace removed.
		 */
		Scanner sc = new Scanner(System.in);
		String allBeds = sc.nextLine().toUpperCase();
		allBeds = allBeds.replaceAll(" ", "");
		String[] allBedArr = allBeds.split(",");
		for (int i = 0; i < allBedArr.length; i++) {
			if (allBedArr[i].equals("")) {
				print("Minimum beds per room is 1. Try again:");
				return checkBeds();
			}
			if (allBedArr[i].equals("S")) {
				allBedArr[i] = "SINGLE";
			} else if (allBedArr[i].equals("D")) {
				allBedArr[i] = "DOUBLE";
			}
			if (!(allBedArr[i].equals("SINGLE") || allBedArr[i].equals("DOUBLE"))) {
				print("One or more beds were spelt incorrectly. Bed types are: \'Single (s)\' or \'Double (d)\'. Try again:");
				return checkBeds();
			}
		}
		return allBedArr;
	}

	public void print(String text) {
		/*
		 * Shortens the print statement
		 */
		System.out.println(text);
	}
}
