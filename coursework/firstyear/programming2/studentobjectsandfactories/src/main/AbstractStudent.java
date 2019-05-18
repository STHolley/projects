package main;
import java.util.Calendar;
public class AbstractStudent implements StudentInterface{
	private Name name;
	private StudentID id;
	private Calendar dob;
	
	public AbstractStudent(Name name, StudentID id, Calendar dob){
		setName(name);
		setID(id);
		setDOB(dob);
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
	public Name getName() {
		return name;
	}
	public StudentID getID() {
		return id;
	}
	public Calendar getDOB() {
		return dob;
	}
}
