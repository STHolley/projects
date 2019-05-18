package main;

import java.util.HashMap;
import java.util.Map;

public class StudentIDFactory {
	public static final Map<StudentID, Name> IDLIST = new HashMap<StudentID, Name>();
	public final static int possibilities = 10000;
	public static StudentID getInstance(Name name) throws Exception {
		if((IDLIST.size() >= possibilities * 26) ) {
			throw new Exception("Out of possible numbers");
		}
		int unique = (int)Math.floor(Math.random() * possibilities);
		StudentID test = new StudentID(unique);
		if(!IDLIST.containsKey(test)) {
			if(!IDLIST.containsValue(name)) {
				IDLIST.put(test, name);
				return test;
			}
			getInstance(name);
		}
		getInstance(name);
		return null;
	}
	
	public static void terminateStudent(StudentID id) {
		System.out.println("out");
		if(IDLIST.containsKey(id)) {
			IDLIST.remove(id);
		}
	}
}
