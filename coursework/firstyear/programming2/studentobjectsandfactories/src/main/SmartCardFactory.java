package main;

import java.util.HashMap;
import java.util.Map;

public class SmartCardFactory {
	public static final Map<StudentID, SmartCardNumber> SCLIST = new HashMap<StudentID, SmartCardNumber>();
	public final static int possibilities = 100;
	public static void getInstance(StudentID id, Name name) throws Exception {
		if((SCLIST.size() >= possibilities) ) {
			throw new Exception("Out of possible numbers");
		}
		int unique = (int)Math.floor(Math.random() * possibilities);
		SmartCardNumber test = new SmartCardNumber(name, unique);
		if(!SCLIST.containsKey(id)) {
			if(!SCLIST.containsValue(test)) {
				SCLIST.put(id, test);
			}
			getInstance(id, name);
		}
	}
}
