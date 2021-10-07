import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.*;

public class Tesisti implements Runnable{
	private int numAccessi;
	private Laboratorio lab;
	private String user;
	private int access;
	
	private Lock lock;
	private Condition PcFree;
	
	public Tesisti(Laboratorio lab, String user) {
		this.numAccessi = ThreadLocalRandom.current().nextInt(1, 3);
		this.lab = lab;
		this.user = user;
		this.access = 0;
		
		this.lock = new ReentrantLock();
		this.PcFree = lock.newCondition();
	}
	
	public void run() {
		for(int i = 0; i<numAccessi; i++) {
			lab.insert("Tesista", this);
			
			lock.lock();
			try {
				while(this.access == 0) {
					PcFree.await();
				}
				this.access = 0;
				
				//Simulo l'uso del computer dell'utente
				int tmp = (int)(Math.random()*1000);
				System.out.println(Thread.currentThread().getName() + ": " + user + " lavora per " + tmp + " millisecondi");
				try {
						Thread.sleep(tmp);
				} catch(InterruptedException e) {
						e.printStackTrace();
					}
				
				System.out.println(Thread.currentThread().getName() + ": " + user + " terminato l'utilizzo del pc.");
				lab.finish("Tesista", this);
				
				
			} catch(InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			
			//aspetto 1s prima di rimettermi in coda
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
