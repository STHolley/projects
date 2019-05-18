package main;
import java.util.Calendar;
import java.util.Date;
public class SmartCard {
	private Name name;
	private StudentID id;
	private Calendar dob;
	private SmartCardNumber cardNum;
	private String dateOfIssue;
	
	public SmartCard(Name name, StudentID id, Calendar dob) {
		setName(name);
		setID(id);
		setCardNum(name);
		setDOB(dob);
		setDOI();
	}
	public void setName(Name name) {
		this.name = name;
	}
	public void setID(StudentID id) {
		this.id = id;
	}
	public void setDOB(Calendar dob) {
		this.dob = dob;
	}
	public void setDOI() {
		Date now = new Date();
		Calendar current = Calendar.getInstance();
		current.setTime(now);
		String concat = current.get(Calendar.DATE) +
				"/" + current.get(Calendar.MONTH)  +
				"/" + current.get(Calendar.YEAR);
		dateOfIssue = concat;
	}
	public void setCardNum(Name name) {
		cardNum = SmartCardFactory.SCLIST.get(id);
	}
	public Name getName() {
		return name;
	}
	public StudentID getID() {
		return id;
	}
	public Calendar getDOB() {
		return dob;
	}
	public String getCardNum() {
		return cardNum.getNumber();
	}
	public String getDOI() {
		return dateOfIssue;
	}
	public String toString() {
		return "Smart Card info: " + getCardNum() + 
				"\nDate of issue: " + getDOI();
	}
}
