package test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.ArrayList;
import org.junit.Test;

import main.*;

public class PostGraduateTest {
	@Test
	public void testSmartCard() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		StudentID id = StudentIDFactory.getInstance(name);
		PostGraduate test = StudentTypeFactory.getPGInstance(name, dob, id);
		test.issueSC(name, dob, id);
		assertEquals("Smart Card info: SH-2018-", test.getSC().toString().substring(0,test.getSC().toString().length() - 2));
	}
	@Test
	public void testCredits() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		StudentID id = StudentIDFactory.getInstance(name);
		PostGraduate test = StudentTypeFactory.getPGInstance(name, dob, id);
		test.addModule("CSC4021");
		test.addModule("CSC4022");
		System.out.println(test.getCurrCreds());
		assertEquals(50, test.getCurrCreds());
	}
	@Test
	public void testModules() throws Exception {
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		StudentID id = StudentIDFactory.getInstance(name);
		PostGraduate test = StudentTypeFactory.getPGInstance(name, dob, id);
		test.addModule("CSC4021");
		ArrayList<Module> comp = new ArrayList<Module>();
		comp.add(new Module("CSC4021", "Learning to use Scratch", 30));
		assertEquals(comp.toString(), test.getModules().toString());
	}
}
