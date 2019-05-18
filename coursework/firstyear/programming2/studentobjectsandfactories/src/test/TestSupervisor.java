package test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import org.junit.Test;

import main.*;

public class TestSupervisor {
	@Test
	public void testSupervisor() throws Exception{
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		Name supervis = new Name("Peter", "Quill");
		StudentID id = StudentIDFactory.getInstance(name);
		PostGraduateResearch test = StudentTypeFactory.getPGRInstance(name, dob, id, supervis);
		assertEquals(supervis, test.getSV().getSupervis());
	}
}
