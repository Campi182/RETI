import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ContoCorrente {

	
	private String name;
	
	private String surname;
	
	private int ID;
	private List<Movement> list;
	
	public ContoCorrente(String name, String surname, int ID, List<Movement> list) {
		this.name = name;
		this.surname = surname;
		this.ID = ID;
		this.list = list;
	}
	
	public String getName() {
		return this.name;
	}
}
