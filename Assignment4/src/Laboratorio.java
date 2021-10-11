import java.awt.desktop.UserSessionEvent;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class Laboratorio implements Runnable{

	private static Random rand = new Random();
	private int PcTesisti;
	private final int[] computers;
	private int posti;
	private LinkedBlockingQueue<Runnable> QueueProfessori, QueueTesisti, QueueStudenti;
	
	Object synchQueue = new Object();
	Object synchComputers = new Object();
	
	public Laboratorio(int posti) {
		this.PcTesisti = rand.nextInt(posti);
		this.posti = posti;
		computers = new int[this.posti];
		
		QueueProfessori  = new LinkedBlockingQueue<Runnable>();
		QueueStudenti = new LinkedBlockingQueue<Runnable>();
		QueueTesisti = new LinkedBlockingQueue<Runnable>();
		
		
		
		for(int i = 0; i < posti; i++)
			computers[i] = 0;
	}
	
	public void run() {
		System.out.println("Pc assegnato ai tesisti: " + PcTesisti);
		while(!Thread.currentThread().isInterrupted()) {
			synchronized(synchQueue) {
				while(QueueProfessori.isEmpty() && QueueStudenti.isEmpty() && QueueTesisti.isEmpty()) {
					try {
						this.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				if(QueueProfessori.isEmpty()) {
					synchronized(synchComputers) {
						while(!checkLabFree()) {
							try {
								this.wait();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						
						User user = (User) QueueProfessori.remove();
						//setto occupati tutti i pc
						user.work();
					}
				}
				else if(!QueueTesisti.isEmpty()){
						//synchronized(synchComputers) {
				//manca il try
							while(computers[PcTesisti] == 1)
								this.wait();
							
							User u = (User) QueueTesisti.remove();
							computers[PcTesisti] = 1;
							u.work();
						//}
					
				}
				else if(!QueueStudenti.isEmpty()) {
					int i = -1;
					while((i= findPc()) == -1)
						this.wait();
					
					User u = QueueStudenti.remove();
					u.setpc(i);
					computers[i] = 1;
					u.work();
				}
			}
			
			
		}
	}
	
	public void insert(String user, Object tmp) {
		synchronized(synchQueue) {
			User u = (User) tmp;
			
			if(user.equals("Professore")) {
				try {
					QueueProfessori.put(u);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(user.equals("Studente")) {
				try {
					QueueStudenti.put(u);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(user.equals("Tesista")) {
				try {
					QueueTesisti.put(u);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized boolean checkLabFree() {
		for(int i = 0; i < posti; i++) {
			if(computers[i] == -1)
				return false;
		}
		return true;
	}
	// profsetlab
}
