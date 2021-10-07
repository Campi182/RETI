import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.*;

public class Studente implements Runnable{

		private int numAccessi;
		private Laboratorio lab;
		private String user;
		private int UsingPc;
		
		private int access; //variabile che mi segnala quando posso entrare nel lab
		private Lock lock;
		private Condition PcFree;
		
	public Studente(Laboratorio lab, String user) {
			this.numAccessi = ThreadLocalRandom.current().nextInt(1, 4);
			this.lab = lab;
			this.user = user;
			this.access = 0;
			this.UsingPc = -1;
			
			this.lock = new ReentrantLock();
			this.PcFree = lock.newCondition();
	}
	
	public void run() {
		for(int i = 0; i< numAccessi; i++) {
			lab.insert("Studente", this);
			
			lock.lock();
			try {
				while(this.access == 0)
					PcFree.await();
				this.access = 0;
				
				//lavoro per un tempo casuale
				int tmp = (int)(Math.random()*1000);
				System.out.println(Thread.currentThread().getName() + ": " + user + " lavoro al Pc " + UsingPc + " per " + tmp + " millisecondi");
				try {
					Thread.sleep(tmp);
			} catch(InterruptedException e) {
					e.printStackTrace();
				}
				
			System.out.println(Thread.currentThread().getName() + ": " + user + " terminato l'utilizzo del pc.");
			lab.finish("Studente", this);
			
			
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
	
	//Risveglio il thread in wait. Work viene chiamata da 'Laboratorio' quando l'utente è pronto per lavorare.
	//access viene settato ad 1 e il thread si risveglia dalla await
	public void work() {
		this.access = 1;
		lock.lock();
		PcFree.signal();
		lock.unlock();
	}
	
	//restituisco il Pc assegnato allo studente
	public int getPc() {
		return this.UsingPc;
	}
	
	//setto il pc assegnato allo studente
	public void setPc(int i) {
		this.UsingPc = i;
	}
	
}
