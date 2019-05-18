package main;
import java.util.Date;
public class SmartCardNumber {
	private String number;
	private String fInit;
	private String sInit;
	
	public SmartCardNumber(Name name, int unique) {
		setFInit(name.getForeInitial());
		setSInit(name.getSurInitial());
		Date today = new Date();
		setNumber(today, unique);
	}

	public String getNumber() {
		return number;
	}
	public void setNumber(Date today, int unique) {
		String p1 = getFInit() + getSInit();
		String p2 = "" + today.toString();
		String[] dateSplit = p2.split(" ");
		p2 = dateSplit[dateSplit.length - 1];
		number = p1 + "-" + p2 + "-" + String.format("%02d", unique);
	}
	public String getFInit() {
		return fInit;
	}
	public void setFInit(String fInit) {
		this.fInit = fInit;
	}
	public String getSInit() {
		return sInit;
	}
	public void setSInit(String sInit) {
		this.sInit = sInit;
	}
	
}
