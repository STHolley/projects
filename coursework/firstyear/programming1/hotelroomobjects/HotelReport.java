
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

public class HotelReport {

	private Hotel myHotel; // Stores the passed in hotel object

	public HotelReport(Hotel myHotel) {
		/*
		 * Constructor method which sets the hotel to be reported
		 */
		setHotel(myHotel);
	}

	/*
	 * Getter and setter methods for the hotel to be reported on
	 */

	private void setHotel(Hotel myHotel) {
		this.myHotel = myHotel;
	}

	private Hotel getHotel() {
		return myHotel;
	}

	public void writeReport() {
		/*
		 * Creates a header bar which contains the hotel name in the centre
		 * Along with a border around it. All text is printed in block capitals
		 */
		int gridSize = 55; // Total character spaces between the borders
		print("+-------------------------------------------------------+");
		print(String.format("|%s|", centerName(gridSize)));
		print("+---------------+---------------------------------------+");
		/*
		 * Loops through each room in the hotel and prints out the relevant
		 * information on a new line And draws southern, eastern and western
		 * borders to allow it to connect up to the main table.
		 */
		for (int i = 0; i < myHotel.getSize(); i++) {
			print(formatRooms(i).toUpperCase());
			print(formatAmount(i).toUpperCase());
			print(formatSleepAmount(i).toUpperCase());
			print(formatOccupied(i).toUpperCase());
			print("+---------------+---------------------------------------+");
		}
		/*
		 * After every hotel room's information has been displayed The overall
		 * hotel information is displayed such as total occupancy, if there is a
		 * vacancy. And if there is a vacancy, the total amount of free rooms
		 * and beds.
		 */
		print(String.format("| Hotel\t\t|\tTotal Occupancy:\t%s\t|", getHotel().getTotalOccupancy()).toUpperCase());
		print(String.format("| information:\t|\tHas Vacancies:\t\t%s\t|", convertBoolean(getHotel().getVacancies()))
				.toUpperCase());
		if (getHotel().getVacancies()) {
			print(String.format("|\t\t|\tVacant Rooms:\t\t%s\t|", getHotel().getEmptyRooms()).toUpperCase());
			print(String.format("|\t\t|\tVacant Beds:\t\t%s\t|", getHotel().getTotalVacancy()).toUpperCase());
		}
		/*
		 * Draws a final southern border to close the table
		 */
		print("+---------------+---------------------------------------+");
	}

	private String centerName(int totalLength) {
		/*
		 * Centres the hotel name based off the length of the available space
		 * using the length passed in as a parameter
		 */
		String name = getHotel().getName().toUpperCase();
		int spaces = totalLength - name.length();
		if (!(spaces % 2 == 1)) {
			spaces--;
			name += " ";
		}
		for (int i = 0; i < spaces / 2; i++) {
			name = " " + name + " ";
		}
		return " " + name;
	}

	private String convertBoolean(boolean booToConv) {
		/*
		 * Converts boolean into text True becomes Yes False becomes No
		 */
		String convText = "";
		if (booToConv) {
			convText = "Yes";
		} else {
			convText = "No";
		}
		return convText;
	}

	private String formatRooms(int roomNum) {
		/*
		 * Prints out the bed types in a room whilst keeping it formatted and
		 * having border walls on the east, west and south.
		 */
		String bedString = "";
		for (int i = 0; i < getHotel().getRoom(roomNum).getTotalBeds(); i++) {
			if (((i) % 3 == 0) && (i != 0)) { // 3 represents the number of
												// words per line
				bedString += "\t|\n|\t\t|\t";
			}
			bedString += getHotel().getRoom(roomNum).getBed(i).getType() + "\t";
		}
		if (getHotel().getRoom(roomNum).getTotalBeds() % 3 == 0) {
			bedString += "\t";
		} else {
			/*
			 * Gets the space remaining for the end of the table and fills it
			 * with tab spaces
			 */
			for (int i = (4 - (getHotel().getRoom(roomNum).getTotalBeds() % 3)); i > 0; i--) {
				bedString += "\t";
			}
		}
		bedString += "|";
		return String.format("| Room %s Beds:\t|\t%s", roomNum + 1, bedString);
	}

	private String formatAmount(int roomNum) {
		/*
		 * Prints the total number of beds in a room
		 */
		int total = getHotel().getRoom(roomNum).getTotalBeds();
		return String.format("| Total Beds:\t|\t%s\t\t\t\t|", total);
	}

	private String formatSleepAmount(int roomNum) {
		/*
		 * Prints the number for the amount of people who can sleep in the room
		 */
		return String.format("| Can Sleep:\t|\t%s\t\t\t\t|", getHotel().getRoom(roomNum).getCanSleep());
	}

	private String formatOccupied(int roomNum) {
		/*
		 * prints whether or not the room is occupied
		 */
		return String.format("| Occupied:\t|\t%s\t\t\t\t|", convertBoolean(getHotel().getRoom(roomNum).isBooked()));
	}

	private void print(String text) {
		/*
		 * Shortens the print statement
		 */
		System.out.println(text);
	}
}