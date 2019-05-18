package test;
import java.util.Calendar;

import static org.junit.Assert.*;
import org.junit.Test;

import main.*;
public class AbstractStudentTest {
	@Test
	public void testName() {
		Calendar dob = Calendar.getInstance();
		dob.set(1999,0,30);
		Name x = new Name("Sam", "Holley");
		AbstractStudent test = new AbstractStudent(x, new StudentID(1), dob);
		assertEquals(x.toString(), test.getName().toString());
	}
	@Test
	public void testDOB() {
		Calendar dob = Calendar.getInstance();
		dob.set(1999,0,30);
		Name x = new Name("Sam", "Holley");
		AbstractStudent test = new AbstractStudent(x, new StudentID(1), dob);
		assertEquals(dob.toString(), test.getDOB().toString());
	}
	@Test
	public void testID() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1999,0,30);
		Name x = new Name("Sam", "Holley");
		StudentID id = StudentIDFactory.getInstance(x);
		AbstractStudent test = new AbstractStudent(x, id, dob);
		assertEquals(id.toString(), test.getID().toString());
	}
}
