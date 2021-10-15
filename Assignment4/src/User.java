
public class User implements Runnable, Comparable<User>{
	
	static final int STUDENTE = 1;
	static final int TESISTA = 2;
	static final int PROFESSORE = 3;
	
	private Laboratorio lab;
	private String user;
	private int UsingPc;
	private int id;
	private int priority;
	private int active;
	
	public User(Laboratorio lab, String user, int id, int UsingPc) {
		this.lab = lab;
		this.user = user;
		this.id = id;
		this.UsingPc = UsingPc;
		active = 0;
		if(user.equals("Studente"))
			this.priority = 1;
		else if(user.equals("Tesista"))
			this.priority = 2;
		else if(user.equals("Professore"))
			this.priority = 3;
	}
	
	public void run() {
			active = 1;
			int sleep = (int)(Math.random()*1000);
			System.out.println(user + " " + id + " lavora per " + sleep + "millisecondi");
			try {
				Thread.sleep(sleep);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			
			//finito di lavorare, se è prof deve liberare tutto altrimenti solo il pc utilizzato
			try {
			if(user.equals("Professore")) {
				lab.freeLab();
				active = 0;
				//System.out.println("Professore "+id+" libera il lab");
			}
			else {
				//System.out.println(user + id + " libera il computer");
				lab.freePc(UsingPc);
				active = 0;
			}
			} catch(InterruptedException ignored) {
				;
			}
	}

	public int isActive() {
		return this.active;
	}
	
	public int getPriority() {
		return this.priority;
	}
	
	public String getWho() {
		return this.user;
	}
	
	public int getUsingPc() {
		return this.UsingPc;
	}
	
	public void assignPc(int i) {
		this.UsingPc = i;
	}
	
	public int compareTo(User u) {
		return u.getPriority() - this.priority;
	}
}