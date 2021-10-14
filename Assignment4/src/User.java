import java.util.Random;

public class User implements Runnable, Comparable<User>{
	
	static final int STUDENTE = 1;
	static final int TESISTA = 2;
	static final int PROFESSORE = 3;
	
	private Laboratorio lab;
	private String user;
	private int UsingPc;
	private int id;
	private int numAccessi;
	private Tutor tutor;
	private int priority;
	
	private static Random random = new Random();
	final static int maxAccessiperUser = 1;
	
	public User(Laboratorio lab, Tutor tutor, String user, int id, int UsingPc) {
		this.lab = lab;
		this.user = user;
		this.tutor = tutor;
		this.id = id;
		this.UsingPc = UsingPc;
		numAccessi = random.nextInt(maxAccessiperUser) + 3; //+1 perche deve fare almeno un accesso
		if(user.equals("Studente"))
			this.priority = 1;
		else if(user.equals("Tesista"))
			this.priority = 2;
		else if(user.equals("Professore"))
			this.priority = 3;
	}
	
	public void run() {
		numAccessi--;
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
				//System.out.println("Professore "+id+" libera il lab");
			}
			else {
				//System.out.println(user + id + " libera il computer");
				lab.freePc(UsingPc);
			}
			} catch(InterruptedException ignored) {
				;
			}
			
			int wait = (int)(Math.random()*1000);
			try {
				Thread.sleep(wait);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			if(numAccessi != 0) {
				tutor.InsertQueue(this);
			}
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