import java.util.Random;

public class User implements Runnable{
	
	
	private Laboratorio lab;
	private String user;
	private int UsingPc;
	private int numAccessi;
	private static Random random = new Random();
	final static int maxAccessiPerUser = 3;
	
	
	public User(Laboratorio lab, String user, int UsingPc) {
		this.lab = lab;
		this.user = user;
		this.UsingPc = UsingPc;
		numAccessi = 1 + random.nextInt(maxAccessiPerUser);
	}
	
	public void run() {
		for(int i = 0; i < numAccessi; i++) {
			if(user.equals("Studente"))
				UsingPc = lab.setPcStudent();
			else if(user.equals("Tesista"))
				lab.setPcTesista(UsingPc);
			else if(user.equals("Professore"))
				lab.setPcProfessore();
			
			//lavoro per 'sleep' msec
			int sleep = (int)(Math.random()*500);
			try {
				Thread.sleep(sleep);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
			//finito di lavorare, libero il Pc(o il laboratorio nel caso Prof)
			if(user.equals("Professore")) {
				lab.freeLab();
			}
			else {
				lab.freePc(UsingPc);
			}
			
			//aspetto 'wait' msec prima di rimettermi in coda
			int wait = (int)(Math.random()*1000);
			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}