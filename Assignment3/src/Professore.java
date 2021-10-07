import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.*;


public class Professore implements Runnable{

	private int numAccessi;
	private Laboratorio lab;
	private String user;
	private int access;
	
	private Lock lock;
	private Condition PcFree;
	
	
	public Professore(Laboratorio lab, String user) {
		this.numAccessi = ThreadLocalRandom.current().nextInt(1, 3);
		this.lab = lab;
		this.user = user;
		this.access = 0;
		
		this.lock = new ReentrantLock();
		this.PcFree = lock.newCondition();
	}
	
	public void run() {
		for(int i = 0; i<numAccessi; i++) {
			lab.insert("Professore", this);
			
			//Devo aspettare che vengo assegnato ad un Pc per poter proseguire
			//altrimenti il prof continua l'esecuzione senza avere un pc, mentre è in coda
			
			lock.lock();
			
			try {
				while(this.access == 0)
					PcFree.await();
			this.access = 0;
			
			
			//Simulo l'uso del computer dell'utente
			int tmp = (int)(Math.random()*1000);
			System.out.println(Thread.currentThread().getName() + ": " + user + " lavoro per " + tmp + " millisecondi");
			try {
					Thread.sleep(tmp);
			} catch(InterruptedException e) {
					e.printStackTrace();
				}
			
			System.out.println(Thread.currentThread().getName() + ": " + user + " terminato l'utilizzo del pc.");
			lab.finish("Professore", this);
			
			} catch(InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			
			//sleep prima di rimettersi in coda
			try{
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(Thread.currentThread().getName() + ": " + user + " se ne va");
	}
	
	public void work() {
		this.access = 1;
		lock.lock();
		PcFree.signal();
		lock.unlock();
	}
	
}
	

