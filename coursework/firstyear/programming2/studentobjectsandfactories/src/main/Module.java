package main;

public class Module{
	
	private String code;
	private String name;
	private int credits;
	
	public Module(String code, String name, int credits){
		setCode(code);
		setName(name);
		setCredits(credits);
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCredits(int credits) {
		this.credits = credits;
	}
	public String getCode() {
		return code;
	}
	public String getName() {
		return name;
	}
	public int getCredits() {
		return credits;
	}
	
	public String toString() {
		return code + ": " + name + ", Credits: " + credits;
	}
}
