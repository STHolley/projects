package main;

import java.util.HashMap;
import java.util.Map;

public class StudentID{
	private String id;
	
	public StudentID(int uniqID) {
		setID(uniqID);
	}
	public void setID(int uniqID) {
		this.id = randChar() + String.format("%04d", uniqID);
	}
	public String getID() {
		return id;
	}
	public String randChar() {
		String[] alphabet = {"A","B","C","D","E","F","G","H","I","J",
		                     "K","L","M","N","O","P","Q","R","S","T",
		                     "U","V","W","X","Y","Z"};
		double rand = Math.random();
		rand *= 26;
		rand = Math.floor(rand);
		return alphabet[(int)rand];
	}
	public String toString() {
		return getID();
	}
}
