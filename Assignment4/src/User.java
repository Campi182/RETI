import java.util.concurrent.ThreadLocalRandom;

public class User implements Runnable{

	private int numAccessi;
	private Laboratorio lab;
	private String user;
	private int UsingPc;
	private int access;
	
	
	public User(Laboratorio lab, String user) {
		this.numAccessi = ThreadLocalRandom.current().nextInt(1,4);
		this.lab = lab;
		this.user = user;
		this.access = 0;
		this.UsingPc = -1;
	}
	
	public void run() {
		for(int i = 0; i < numAccessi; i++) {
			lab.insert("Studente", this);
			
		}
	}

	public void setPc(int i) {
		this.UsingPc = 1;
	}
	
	public int getPc() {
		return this.UsingPc;
	}
	
	public void work() {
		this.access = 1;
	}
}
