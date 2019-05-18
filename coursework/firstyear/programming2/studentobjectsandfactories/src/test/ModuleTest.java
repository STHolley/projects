package test;
import java.util.Calendar;

import static org.junit.Assert.*;
import org.junit.Test;

import main.*;
public class ModuleTest {
	@Test
	public void testCode() {
		String code = "CSC1000";
		Module test = new Module(code, "Test", 10);
		assertEquals(code, test.getCode());
	}
	@Test
	public void testName() {
		String name = "Test";
		Module test = new Module("CSC1000", name, 10);
		assertEquals(name, test.getName());
	}
	@Test
	public void testCredits() {
		int creds = 10;
		Module test = new Module("CSC1000", "Test", creds);
		assertEquals(creds, test.getCredits());
	}
	@Test
	public void testToString() {
		String code = "CSC1000";
		String name = "Test";
		int creds = 10;
		String outTest = code + ": " + name + ", Credits: " + creds;
		Module test = new Module(code, name, creds);
		assertEquals(outTest, test.toString());
	}
}
