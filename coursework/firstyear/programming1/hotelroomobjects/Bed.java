
/*
 * 	--	Sam Holley	--
 * 	--	 07/12/17	--
 * 	--	170340421	--
 */

public class Bed {
	/*
	 * Defines a bed as either single or double and gives a size based of the
	 * type of bed with single being 1 and double being 2
	 */
	public Bed(String type) {
		setType(type);
		if (type.equals("SINGLE")) {
			setBedSize(1);
		} else if (type.equals("DOUBLE")) {
			setBedSize(2);
		}
	}

	private String type; // Stores the named type of bed, double or single

	private int bedSize; // Stores the size of the bed with single being 1 and
							// double being 2

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setBedSize(int bedSize) {
		this.bedSize = bedSize;
	}

	public int getBedSize() {
		return bedSize;
	}

}
