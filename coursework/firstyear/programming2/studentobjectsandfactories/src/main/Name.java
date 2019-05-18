package main;

public class Name{

	private String foreName;
	
	private String surName;
	
	public Name(String foreName, String surName){
		setForeName(foreName);
		setSurName(surName);
	}
	
	public void setForeName(String foreName){
		this.foreName = foreName;
	}
	
	public void setSurName(String surName){
		this.surName = surName;
	}
	
	public String getForeName(){
		return foreName;
	}
	
	public String getSurName(){
		return surName;
	}
	
	public String toString() {
		return foreName + " " + surName;
	}
	public String getForeInitial() {
		String[] asCharArr = getForeName().split("");
		return asCharArr[0];
	}
	public String getSurInitial() {
		String[] asCharArr = getSurName().split("");
		return asCharArr[0];
	}
}
