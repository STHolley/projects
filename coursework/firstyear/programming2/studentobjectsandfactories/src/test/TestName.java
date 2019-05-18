package test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import main.*;

public class TestName {
	@Test
	public void testForename() {
		String fName = "Sam";
		String sName = "Holley";
		Name test = new Name(fName, sName);
		assertEquals(fName, test.getForeName());
	}
	@Test
	public void testSurname() {
		String fName = "Sam";
		String sName = "Holley";
		Name test = new Name(fName, sName);
		assertEquals(sName, test.getSurName());
	}
	@Test
	public void testForeInit() {
		String fName = "Sam";
		String fInit = "S";
		String sName = "Holley";
		String sInit = "H";
		Name test = new Name(fName, sName);
		assertEquals(fInit, test.getForeInitial());
	}
	@Test
	public void testSurInit() {
		String fName = "Sam";
		String fInit = "S";
		String sName = "Holley";
		String sInit = "H";
		Name test = new Name(fName, sName);
		assertEquals(sInit, test.getSurInitial());
	}
	@Test
	public void testToString() {
		String fName = "Sam";
		String sName = "Holley";
		String name = "Sam Holley";
		Name test = new Name(fName, sName);
		assertEquals(name, test.toString());
	}
}
