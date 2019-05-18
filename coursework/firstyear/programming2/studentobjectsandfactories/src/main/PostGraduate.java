package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Date;

public class PostGraduate extends AbstractStudent{
	private ArrayList<Module> modules = new ArrayList<Module>();
	private static final int CREDITS = 180;
	private int currCreds = 0;
	private SmartCard sc;
	private static final int PASSMARK = 50;
	
	public PostGraduate(Name name, StudentID id, Calendar dob) throws Exception{
		super(name, id, dob);
		checkAge(dob);
		issueSC(name, dob, id);
	}
	public PostGraduate(Name name, StudentID id, Calendar dob, ArrayList<Module> mod) throws Exception {
		super(name, id, dob);
		checkAge(dob);
		for(int i = 0; i < mod.size(); i++) {
			addModule(mod.get(i).getCode());
		}
		issueSC(name, dob, id);
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
	
	public void addModule(String modCode) throws FileNotFoundException, Exception {
		Scanner read = new Scanner(new FileReader("src/main/PostgradList.txt"));
		boolean isFound = false;
		if(getCurrCreds() < CREDITS) {
			while(read.hasNextLine()) {
				String currLine = read.nextLine();
				String[] modOpt = currLine.split(",");
				if(modCode.equals(modOpt[0])) {
					if(CREDITS >= (Integer.parseInt(modOpt[2]) + getCurrCreds())) {
						getModules().add(new Module(modOpt[0], modOpt[1], Integer.parseInt(modOpt[2])));
						isFound = true;
					} else {
						throw new Exception("Too many credits");
					}
				}
			}
			if(!isFound) {
				throw new Exception("Module not in list");
			}
		} else {
			throw new Exception("Total credits reached");
		}
		setCurrCreds();
	}
	public void removeModule(String code) {
		for(int i = 0; i < getModules().size(); i++) {
			if(getModules().get(i).getCode().equals(code)) {
				getModules().remove(i);
				break;
			}
		}
		setCurrCreds();
	}
	public ArrayList<Module> getModules(){
		return modules;
	}
	
	public void setCurrCreds() {
		int total = 0;
		for(int i = 0; i < getModules().size(); i++) {
			total += getModules().get(i).getCredits();
		}
		currCreds = total;
	}
	public int getCurrCreds() {
		return currCreds;
	}
	public int getPassMark() {
		return PASSMARK;
	}
	
	public String toString() {
		String modstring = "";
		for(int i = 0; i < modules.size(); i++) {
			modstring += modules.get(i).getCode() + ": " + modules.get(i).getName() + "\n";
		}
		String toReturn = this.getName().toString() + "(" + this.getID() + ")\n" +
		this.getDOB().get(Calendar.DATE) + "/" + (this.getDOB().get(Calendar.MONTH)+1) + "/" + this.getDOB().get(Calendar.YEAR) + 
		"\n--------------------\n" + modstring + "\nTotal Credits: " + getCurrCreds() + "\n--------------------\n" + getSC().toString();
		return toReturn;
	}
}
