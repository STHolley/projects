package main;
import java.io.*;
import java.util.Scanner;
public class Supervisor {
	
	private Name supervis;
	
	public Supervisor(Name name) throws FileNotFoundException, Exception {
		setSupervis(name);
	}

	public Name getSupervis() {
		return supervis;
	}

	public void setSupervis(Name supervis) throws FileNotFoundException, Exception {
		Scanner read = new Scanner(new FileReader("src/main/SupervisorList.txt"));
		boolean validSV = false;
		while(read.hasNextLine()) {
			String line = read.nextLine();
			String[] name = line.split(",");
			String sName = name[0];
			String fName = name[1];
			if((fName.equals(supervis.getForeName()))&&(sName.equals(supervis.getSurName()))) {
				validSV = true;
			}
		}
		if(!validSV) {
			throw new Exception("Invalid Supervisor");
		}
		this.supervis = supervis;
	}
}
