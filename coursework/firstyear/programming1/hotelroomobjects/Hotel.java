
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

import java.util.ArrayList;

public class Hotel {

	public Hotel(String name, int size) {
		setName(name);
		setSize(size);
	}

	public void updateFacts() {
		/*
		 * Once all the rooms are built, all the details of the hotel are
		 * updated
		 */
		setTotalOccupancy();
		setHasVacancies();
		setTotalVacancy();
		setEmptyRooms();
	}

	private String name; // Stores the hotel name

	private int size; // Stores the total amount of rooms

	private static ArrayList<Room> rooms = new ArrayList<Room>(); // Stores the
																	// room
																	// objects

	private int totalOccupancy; // Stores the total amount of occupants

	private boolean hasVacancies = false; // Stores whether there is a vacant
											// room or not

	private int totalVacancy; // Stores the total amount of vacant bed spaces

	private int emptyRooms; // Stores the amount of vacant rooms

	/*
	 * Getter and setter methods for all the variables
	 */

	private void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	private void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public Room getRoom(int index) {
		return rooms.get(index);
	}

	public void addRoom(ArrayList<Bed> bedList, boolean isOccupied) {
		/*
		 * Creates a new room passing in the parameters: a list of beds and
		 * whether the room is occupied or not. It then adds this to the room
		 * list
		 */
		rooms.add(new Room(bedList, isOccupied));
	}

	private void setTotalOccupancy() {
		/*
		 * Calculates the total amount of people who can stay in the hotel by
		 * getting every bed size and totalling it, giving the total occupancy
		 */
		int total = 0;
		for (int i = 0; i < getSize(); i++) {
			for (int j = 0; j < getRoom(i).getTotalBeds(); j++) {
				total += getRoom(i).getBed(j).getBedSize();
			}
		}
		totalOccupancy = total;
	}

	public int getTotalOccupancy() {
		return totalOccupancy;
	}

	private void setHasVacancies() {
		/*
		 * Checks whether or not there is a single vacancy in the hotel. Default
		 * set to false but if one room is free, it is set to true
		 */
		for (int i = 0; i < getSize(); i++) {
			if (!getRoom(i).isBooked()) {
				hasVacancies = true;
			}
		}
	}

	public boolean getVacancies() {
		return hasVacancies;
	}

	private void setTotalVacancy() {
		/*
		 * Gets the total amount of free beds by checking what rooms are booked
		 * and which aren't, then totals all the beds in the free rooms
		 */
		int total = 0;
		for (int i = 0; i < getSize(); i++) {
			if (!getRoom(i).isBooked()) {
				for (int j = 0; j < getRoom(i).getTotalBeds(); j++) {
					total += getRoom(i).getBed(j).getBedSize();
				}
			}
		}
		totalVacancy = total;
	}

	public int getTotalVacancy() {
		return totalVacancy;
	}

	public void setEmptyRooms() {
		/*
		 * Calculates how many free rooms there are based on whether they are
		 * occupied or not
		 */
		int total = 0;
		for (int i = 0; i < getSize(); i++) {
			if (!getRoom(i).isBooked()) {
				total++;
			}
		}
		emptyRooms = total;
	}

	public int getEmptyRooms() {
		return emptyRooms;
	}
}
