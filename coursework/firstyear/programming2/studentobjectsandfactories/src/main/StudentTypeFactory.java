package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;

public class StudentTypeFactory {
	public static final Map<StudentID, UnderGraduate> UGLIST = new HashMap<StudentID, UnderGraduate>();
	public static final Map<StudentID, PostGraduate> PGLIST = new HashMap<StudentID, PostGraduate>();
	public static final Map<StudentID, PostGraduateResearch> PGRLIST = new HashMap<StudentID, PostGraduateResearch>();
	
	public static UnderGraduate getUGInstance(Name name, Calendar dob, StudentID id) throws Exception {
		if(!UGLIST.containsKey(id)) {
			UGLIST.put(id, new UnderGraduate(name, id, dob));
			return UGLIST.get(id);
		} else {
			throw new Exception("Student already exists");
		}
	}
	public static UnderGraduate getUGInstance(Name name, Calendar dob, StudentID id, ArrayList<Module> mod) throws Exception {
		if(!UGLIST.containsKey(id)) {
			UGLIST.put(id, new UnderGraduate(name, id, dob, mod));
			return UGLIST.get(id);
		} else {
			throw new Exception("Student already exists");
		}
	}
	public static void terminateUGStudent(StudentID id) {
		if(UGLIST.containsKey(id)) {
			UGLIST.remove(id);
		}
	}
	public static PostGraduate getPGInstance(Name name, Calendar dob, StudentID id) throws Exception {
		if(!PGLIST.containsKey(id)) {
			PGLIST.put(id, new PostGraduate(name, id, dob));
			return PGLIST.get(id);
		} else {
			throw new Exception("Student already exists");
		}
	}
	public static PostGraduate getPGInstance(Name name, Calendar dob, StudentID id, ArrayList<Module> mod) throws Exception {
		if(!PGLIST.containsKey(id)) {
			PGLIST.put(id, new PostGraduate(name, id, dob, mod));
			return PGLIST.get(id);
		} else {
			throw new Exception("Student already exists");
		}
	}
	public static void terminatePGStudent(StudentID id) {
		if(UGLIST.containsKey(id)) {
			UGLIST.remove(id);
		}
	}
	public static PostGraduateResearch getPGRInstance(Name name, Calendar dob, StudentID id, Name supervisor) throws Exception {
		if(!PGRLIST.containsKey(id)) {
			PGRLIST.put(id, new PostGraduateResearch(name, id, dob, supervisor));
			return PGRLIST.get(id);
		} else {
			throw new Exception("Student already exists");
		}
	}
	public static void terminatePGRStudent(StudentID id) {
		if(PGRLIST.containsKey(id)) {
			PGRLIST.remove(id);
		}
	}
}
