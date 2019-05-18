package test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

import main.*;

public class TestSmartCard {
	@Test
	public void testDateOfIssue() throws Exception{
		Calendar dob = Calendar.getInstance();
		dob.set(1998, 0, 30);
		Name name = new Name("Sam", "Holley");
		StudentID id = StudentIDFactory.getInstance(name);
		UnderGraduate test = StudentTypeFactory.getUGInstance(name, dob, id);
		Date testDate = new Date();
		Calendar testCal = Calendar.getInstance();
		testCal.setTime(testDate);
		String concat = testCal.get(Calendar.DATE) +
				"/" + testCal.get(Calendar.MONTH)  +
				"/" + testCal.get(Calendar.YEAR);
		assertEquals(concat, test.getSC().getDOI());
	}
}
