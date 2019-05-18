package main;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

public class PostGraduateResearch extends AbstractStudent{

	private Supervisor sv;
	private SmartCard sc;
	
	public PostGraduateResearch(Name name, StudentID id, Calendar dob, Name sv) throws FileNotFoundException, Exception {
		super(name, id, dob);
		checkAge(dob);
		issueSC(name, dob, id);
		Supervisor supervis = new Supervisor(sv);
		setSV(supervis);
	}

	public Supervisor getSV() {
		return sv;
	}

	public void setSV(Supervisor sv) {
		this.sv = sv;
	}
	
	public void checkAge(Calendar dob) throws Exception{
		Date current = new Date();
		String dateAsString = current.toString();
		String[] splitFormat = dateAsString.split(" ");
		String currMonth = splitFormat[1];
		int currDate = Integer.parseInt(splitFormat[2]);
		int currYear = Integer.parseInt(splitFormat[5]);
		String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		int currMonthPos = 0;
		for(int i = 0; i < months.length; i++) {
			if(months[i].equals(currMonth)) {
				currMonthPos = i;
			}
		}
		int dobMonth = dob.get(Calendar.MONTH);
		int dobDate = dob.get(Calendar.DATE);
		int dobYear = dob.get(Calendar.YEAR);
		
		int compDate = currDate - dobDate;
		int compMonth = currMonthPos - dobMonth;
		int compYear = currYear - dobYear;
		if(compDate < 0) {
			compMonth -= 1;
		}
		if(compMonth < 0) {
			compYear -= 1;
		}
		if(compYear < 20) {
			throw new Exception("Student is too young to be a postgraduate");
		}
	}
	public void issueSC(Name name, Calendar dob, StudentID id) throws Exception {
		SmartCardFactory.getInstance(id, name);
		sc = new SmartCard(name, id, dob);
	}
	public SmartCard getSC() {
		return sc;
	}
	
	public String toString() {
		String toReturn = this.getName().toString() + "(" + this.getID() + ")\n" +
		this.getDOB().get(Calendar.DATE) + "/" + (this.getDOB().get(Calendar.MONTH)+1) + "/" + this.getDOB().get(Calendar.YEAR) + 
		"\n--------------------\nSupervisor: " + sv.getSupervis().toString() + "\n--------------------\n" + sc.toString();
		return toReturn;
	}
	
}
