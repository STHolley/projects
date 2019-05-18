
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

import java.util.ArrayList;

public class HotelTest {
	public static void main(String[] args) {
		/*
		 * Creates a new dummy hotel with 4 rooms (Predefined) Along with the
		 * beds for each room which is done using array lists
		 * 
		 * A report is then generated after the facts have been updates about
		 * the hotel
		 */
		Hotel myHotel = new Hotel("Sandman", 4);
		ArrayList<Bed> bedList1 = new ArrayList<Bed>();
		bedList1.add(new Bed("SINGLE"));
		myHotel.addRoom(bedList1, true);
		ArrayList<Bed> bedList2 = new ArrayList<Bed>();
		bedList2.add(new Bed("SINGLE"));
		bedList2.add(new Bed("DOUBLE"));
		myHotel.addRoom(bedList2, false);
		ArrayList<Bed> bedList3 = new ArrayList<Bed>();
		bedList3.add(new Bed("SINGLE"));
		bedList3.add(new Bed("DOUBLE"));
		bedList3.add(new Bed("SINGLE"));
		myHotel.addRoom(bedList3, false);
		ArrayList<Bed> bedList4 = new ArrayList<Bed>();
		bedList4.add(new Bed("DOUBLE"));
		bedList4.add(new Bed("DOUBLE"));
		bedList4.add(new Bed("SINGLE"));
		bedList4.add(new Bed("SINGLE"));
		myHotel.addRoom(bedList4, true);
		myHotel.updateFacts();
		HotelReport report = new HotelReport(myHotel);
		report.writeReport();
	}
}