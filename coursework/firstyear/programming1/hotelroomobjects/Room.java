
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

import java.util.ArrayList;

public class Room {
	/*
	 * Defines a room which has the values determining the amount of beds, a
	 * list of those beds, if the room has been booked and how many people can
	 * sleep in the room.
	 */
	public Room(ArrayList<Bed> bedList, boolean booked) {
		setTotalBeds(bedList.size());
		setBed(bedList);
		setBooked(booked);
		setCanSleep();
	}

	private boolean isBooked; // Stores whether the room is booked or not

	private ArrayList<Bed> beds = new ArrayList<Bed>(); // Stores the list of
														// beds in the room

	private int totalBeds; // Stores the total amount of beds

	private int canSleep; // Stores the amount of people who can sleep in the
							// room based off the beds

	/*
	 * Getter and setter functions for the variables above
	 */

	public int getTotalBeds() {
		return totalBeds;
	}

	public void setTotalBeds(int totalBeds) {
		this.totalBeds = totalBeds;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public Bed getBed(int index) {
		/*
		 * Using the index as a parameter allows for easier use of getting the
		 * entities in the list
		 */
		return beds.get(index);
	}

	public void setBed(ArrayList<Bed> beds) {
		this.beds = beds;
	}

	public int getCanSleep() {
		return canSleep;
	}

	public void setCanSleep() {
		/*
		 * Calculates how many people the room can sleep based on the sizes of
		 * all the beds in the room
		 */
		int canSleep = 0;
		for (int i = 0; i < getTotalBeds(); i++) {
			canSleep += getBed(i).getBedSize();
		}
		this.canSleep = canSleep;
	}
}
