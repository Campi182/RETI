import java.io.Serializable;
import java.util.*;

public class ProgrammaCongresso implements Serializable{

	private TreeMap<String, ArrayList<String>> program;
	
	public ProgrammaCongresso() {
		this.program = new TreeMap<String, ArrayList<String>>(); //program: <Session, ArrayList of speakers>
		for(int i = 1; i < 13; i++) {
			program.put("S"+i, new ArrayList<String>());
		}
	}
	
	public ArrayList<String> getSession(String session){
		return this.program.get(session);
	}
	
	public TreeMap<String, ArrayList<String>> getProgram(){
		return this.program;
	}
}
