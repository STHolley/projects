package test;
import java.io.*;
import java.util.Scanner;
import main.*;
import java.util.Calendar;
public class TestClass {
	
	static StudentID[] listOfCreated = new StudentID[6];
	
	//0 = type, 1 = Surname, 2 = Forename, 3 = DOB, 4 = Modules (or svs, 5 = svf)
	public static void main(String[] args) throws Exception {
		UniManager manage = new UniManager();
		Scanner sc = new Scanner(new FileReader("src/test/TestData.txt"));
		System.out.println("Undergrads: " + manage.noOfStudents("ug"));
		System.out.println("Postgrads: " + manage.noOfStudents("pg"));
		System.out.println("Research: " + manage.noOfStudents("pgr"));
		while(sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] bits = line.split(",");
			if(bits[0].equals("ug")) {
				Name name = new Name(bits[1], bits[2]);
				String[] date = bits[3].split("/");
				Calendar dob = Calendar.getInstance();
				dob.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
				StudentID id = StudentIDFactory.getInstance(name);
				UnderGraduate ug = StudentTypeFactory.getUGInstance(name, dob, id);
				String[] modList = bits[4].split("/");
				for(int i = 0; i < modList.length; i++) {
					manage.amendStudentData(id, true, modList[i]);
				}
				System.out.println(ug);
				for(int i = 0; i < listOfCreated.length; i++) {
					if(listOfCreated[i] == null) {
						listOfCreated[i] = id;
						break;
					}
				}
			} else if(bits[0].equals("pg")) {
				Name name = new Name(bits[1], bits[2]);
				String[] date = bits[3].split("/");
				Calendar dob = Calendar.getInstance();
				dob.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
				StudentID id = StudentIDFactory.getInstance(name);
				PostGraduate pg = StudentTypeFactory.getPGInstance(name, dob, id);
				String[] modList = bits[4].split("/");
				for(int i = 0; i < modList.length; i++) {
					manage.amendStudentData(id, true, modList[i]);
				}
				System.out.println(pg);
				for(int i = 0; i < listOfCreated.length; i++) {
					if(listOfCreated[i] == null) {
						listOfCreated[i] = id;
						break;
					}
				}
			} else {
				Name name = new Name(bits[1], bits[2]);
				String[] date = bits[3].split("/");
				Calendar dob = Calendar.getInstance();
				dob.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
				StudentID id = StudentIDFactory.getInstance(name);
				Name sup = new Name(bits[5], bits[4]);
				PostGraduateResearch pgr = StudentTypeFactory.getPGRInstance(name, dob, id, sup);
				System.out.println(pgr);
				for(int i = 0; i < listOfCreated.length; i++) {
					if(listOfCreated[i] == null) {
						listOfCreated[i] = id;
						break;
					}
				}
			}
			System.out.println("\n");
		}
		System.out.println("Undergrads: " + manage.noOfStudents("ug"));
		System.out.println("Postgrads: " + manage.noOfStudents("pg"));
		System.out.println("Research: " + manage.noOfStudents("pgr"));
		
		manage.amendStudentData(listOfCreated[2], false, "CSC1022");
		System.out.println(StudentTypeFactory.UGLIST.get(listOfCreated[2]));
		
		Calendar x = Calendar.getInstance();
		x.set(1992, 3, 4);
		manage.amendStudentData(listOfCreated[1], x);
		System.out.println(StudentTypeFactory.UGLIST.get(listOfCreated[1]));
		
		Name name = new Name("John", "Boy");
		manage.amendStudentData(listOfCreated[3], name);
		System.out.println(StudentTypeFactory.PGLIST.get(listOfCreated[3]));
		
		name = new Name("Tony", "Stark");
		manage.amendStudentData(name, listOfCreated[5]);
		System.out.println(StudentTypeFactory.PGRLIST.get(listOfCreated[5]));
		
		manage.terminateStudent(listOfCreated[2]);
		System.out.println("Undergrads: " + manage.noOfStudents("ug"));
		System.out.println("Postgrads: " + manage.noOfStudents("pg"));
		System.out.println("Research: " + manage.noOfStudents("pgr"));
		
	}

}
