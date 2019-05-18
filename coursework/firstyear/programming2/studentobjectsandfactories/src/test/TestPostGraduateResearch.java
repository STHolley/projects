package test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;

import main.*;

public class TestPostGraduateResearch {
	@Test
	public void testSupervisor() throws Exception{
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		Name sup = new Name("Tony", "Stark");
		StudentID id = StudentIDFactory.getInstance(name);
		PostGraduateResearch test = StudentTypeFactory.getPGRInstance(name, dob, id, sup);
		assertEquals(sup, test.getSV().getSupervis());
	}
}
