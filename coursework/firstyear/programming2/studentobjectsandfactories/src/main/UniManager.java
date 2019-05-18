package main;
import java.util.Calendar;
import java.util.ArrayList;
public class UniManager {
	
	public int noOfStudents(String studyType) throws Exception {
		if(studyType.toLowerCase().equals("ug")) {
			return StudentTypeFactory.UGLIST.size();
		} else if(studyType.toLowerCase().equals("pg")) {
			return StudentTypeFactory.PGLIST.size();
		} else if(studyType.toLowerCase().equals("pgr")){
			return StudentTypeFactory.PGRLIST.size();
		} else {
			throw new Exception("Unknown study type");
		}
	}
	public void registerStudent(String typeOfStudent, Name name, Calendar dob, Name supervisor) throws Exception{
		StudentID id = StudentIDFactory.getInstance(name);
		if(typeOfStudent.toLowerCase().equals("ug")) {
			if(supervisor == null) {
				StudentTypeFactory.getUGInstance(name, dob, id);
			} else {
				throw new Exception("Undergraduates don't need a supervisor");
			}
		} else if(typeOfStudent.toLowerCase().equals("pg")) {
			if(supervisor == null) {
				StudentTypeFactory.getPGInstance(name, dob, id);
			} else {
				throw new Exception("Postgraduates don't need a supervisor");
			}
		} else if(typeOfStudent.toLowerCase().equals("pgr")){
			if(supervisor != null) {
				StudentTypeFactory.getPGRInstance(name, dob, id, supervisor);
			} else {
				throw new Exception("Research students need a supervisor");
			}
		} else {
			throw new Exception("No such student type");
		}
	}
    public void amendStudentData(StudentID id, Name name) throws Exception {
    	if(StudentTypeFactory.UGLIST.containsKey(id)) {
    		StudentIDFactory.IDLIST.remove(id);
    		StudentIDFactory.IDLIST.put(id, name);
    		UnderGraduate temp = StudentTypeFactory.UGLIST.get(id);
    		ArrayList<Module> tempMod = temp.getModules();
    		UnderGraduate update = new UnderGraduate(name, id, temp.getDOB(), tempMod);
    		StudentTypeFactory.UGLIST.remove(id);
    		StudentTypeFactory.UGLIST.put(id, update);
    	}else if(StudentTypeFactory.PGLIST.containsKey(id)) {
    		StudentIDFactory.IDLIST.remove(id);
    		StudentIDFactory.IDLIST.put(id, name);
    		PostGraduate temp = StudentTypeFactory.PGLIST.get(id);
    		ArrayList<Module> tempMod = temp.getModules();
    		PostGraduate update = new PostGraduate(name, id, temp.getDOB(), tempMod);
    		StudentTypeFactory.PGLIST.remove(id);
    		StudentTypeFactory.PGLIST.put(id, update);
    	}else if(StudentTypeFactory.PGRLIST.containsKey(id)) {
    		StudentIDFactory.IDLIST.remove(id);
    		StudentIDFactory.IDLIST.put(id, name);
    		PostGraduateResearch temp = StudentTypeFactory.PGRLIST.get(id);
    		PostGraduateResearch update = new PostGraduateResearch(name, id, temp.getDOB(), temp.getSV().getSupervis());
    		StudentTypeFactory.PGRLIST.remove(id);
    		StudentTypeFactory.PGRLIST.put(id, update);
    	} else {
    		throw new Exception("No such ID");
    	}
	}
    public void amendStudentData(StudentID id, Calendar dob) throws Exception {
    	if(StudentTypeFactory.UGLIST.containsKey(id)) {
    		UnderGraduate temp = StudentTypeFactory.UGLIST.get(id);
    		ArrayList<Module> tempMod = temp.getModules();
    		UnderGraduate update = new UnderGraduate(temp.getName(), id, dob, tempMod);
    		StudentTypeFactory.UGLIST.remove(id);
    		StudentTypeFactory.UGLIST.put(id, update);
    	}else if(StudentTypeFactory.PGLIST.containsKey(id)) {
    		PostGraduate temp = StudentTypeFactory.PGLIST.get(id);
    		ArrayList<Module> tempMod = temp.getModules();
    		PostGraduate update = new PostGraduate(temp.getName(), id, dob, tempMod);
    		StudentTypeFactory.PGLIST.remove(id);
    		StudentTypeFactory.PGLIST.put(id, update);
    	}else if(StudentTypeFactory.PGRLIST.containsKey(id)) {
    		PostGraduateResearch temp = StudentTypeFactory.PGRLIST.get(id);
    		PostGraduateResearch update = new PostGraduateResearch(temp.getName(), id, dob, temp.getSV().getSupervis());
    		StudentTypeFactory.PGRLIST.remove(id);
    		StudentTypeFactory.PGRLIST.put(id, update);
    	} else {
    		throw new Exception("No such ID");
    	}
	}
    public void amendStudentData(StudentID id, boolean add,String module) throws Exception {
    	if(StudentTypeFactory.UGLIST.containsKey(id)) {
    		if(add) {
    			StudentTypeFactory.UGLIST.get(id).addModule(module);
    		} else {
    			StudentTypeFactory.UGLIST.get(id).removeModule(module);
    		}
    	}else if(StudentTypeFactory.PGLIST.containsKey(id)) {
    		if(add) {
    			StudentTypeFactory.PGLIST.get(id).addModule(module);
    		} else {
    			StudentTypeFactory.PGLIST.get(id).removeModule(module);
    		}
    	}else if(StudentTypeFactory.PGRLIST.containsKey(id)) {
    		throw new Exception("Research students do not take modules");
    	} else {
    		throw new Exception("No such ID");
    	}
	}
    public void amendStudentData(Name supervisor, StudentID id) throws Exception {
    	if(StudentTypeFactory.PGRLIST.containsKey(id)) {
    		PostGraduateResearch temp = StudentTypeFactory.PGRLIST.get(id);
    		StudentTypeFactory.PGRLIST.remove(id);
    		StudentTypeFactory.getPGRInstance(temp.getName(), temp.getDOB(), temp.getID(), supervisor);
    	} else {
    		throw new Exception("Only research students can have supervisors updated");
    	}
    }
    public void terminateStudent(StudentID id) throws Exception {
    	if(StudentTypeFactory.UGLIST.containsKey(id)) {
    		StudentTypeFactory.terminateUGStudent(id);
    	}else if(StudentTypeFactory.PGLIST.containsKey(id)) {
    		StudentTypeFactory.terminatePGStudent(id);
    	}else if(StudentTypeFactory.PGRLIST.containsKey(id)) {
    		StudentTypeFactory.terminatePGRStudent(id);
    	} else {
    		throw new Exception("No such ID");
    	}
		StudentIDFactory.terminateStudent(id);
	}
}
