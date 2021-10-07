import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.*;

public class Laboratorio implements Runnable{

	public int PcTesisti;
	private int[] computers;	//computers[i]=0 se postazione è libera
								//			  =1 se postazione occupata	
	
	private int posti;
	private LinkedBlockingQueue<Runnable> QueueProfessori, QueueStudenti, QueueTesisti;
	
	private Lock ComputersLock, QueueLock; //QueueLock => lock per tutte e 3 le code
	private Condition ComputersCondition, QueueCondition;
	
	public Laboratorio(int posti) {
		this.PcTesisti = ThreadLocalRandom.current().nextInt(0, posti-1);
		this.posti = posti;
		computers = new int[this.posti];
		QueueProfessori = new LinkedBlockingQueue<Runnable>();
		QueueStudenti = new LinkedBlockingQueue<Runnable>();
		QueueTesisti = new LinkedBlockingQueue<Runnable>();
		
		ComputersLock = new ReentrantLock();
		QueueLock = new ReentrantLock();
		
		ComputersCondition = ComputersLock.newCondition();
		QueueCondition = QueueLock.newCondition(); 
		
		for(int i = 0; i<posti; i++)
			computers[i] = 0;	//setto libera ogni postazione
	}
	
	
	//Inserisco la persona <user> nella rispettiva coda
	//parameters: <user>: Tipo di utente
	//		Object <tmp>: l'oggetto che inserisco in coda
	public void insert(String user, Object tmp){
		QueueLock.lock();
		 
		if(user.equals("Professore")) {
			Professore elem = (Professore) tmp;
			try {
				QueueProfessori.put(elem);
			}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
			QueueCondition.signal();
		}
		
		if(user.equals("Studente")) {
			Studente elem = (Studente) tmp;
			try {
				QueueStudenti.put(elem);
			}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
			QueueCondition.signal();
		}
		
		if(user.equals("Tesista")) {
			Tesisti elem = (Tesisti) tmp;
			try {
				QueueTesisti.put(elem);
			}
					catch(InterruptedException e) {
						e.printStackTrace();
					}
			QueueCondition.signal();
		}
		
		QueueLock.unlock();
	}
	
	
	//Avvio del Laboratorio
	public void run() {
		System.out.println("Pc assegnato ai tesisti: " + PcTesisti);
		while(!Thread.currentThread().isInterrupted()) {
			QueueLock.lock();
			
			//se tutte le code son vuote aspetto
			try {
				while(QueueProfessori.isEmpty() && QueueTesisti.isEmpty() && QueueStudenti.isEmpty())
					QueueCondition.await();
			} catch(InterruptedException e) {
				return;
			} finally {
				QueueLock.unlock();
			}
			
			

			if(!QueueProfessori.isEmpty()) {
				ComputersLock.lock();
				
				//aspetto che tutti i Pc siano liberi
				try {
					while(!checkLabFree())
						ComputersCondition.await();
					Professore el = (Professore) QueueProfessori.remove();
					//System.out.println(Thread.currentThread().getName() + ": Entra nel laboratorio un professore ");
					for(int i = 0; i<this.posti; i++)
						computers[i] = 1;
					//sveglio il thread del professore che entra
					el.work();
					
				} catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					ComputersLock.unlock();
				}
			}
			else if(!QueueTesisti.isEmpty()) {
				ComputersLock.lock();
				try {
				//aspetto che il Pc dei tesisti sia libero
				while(computers[PcTesisti] == 1)
					ComputersCondition.await();
				
				Tesisti tmp = (Tesisti) QueueTesisti.remove();
				computers[PcTesisti] = 1;
				//risveglio il thread del tesista entrato nel lab
				tmp.work();
				}catch(InterruptedException e) {
					e.printStackTrace();
				}finally {
					ComputersLock.unlock();
				}
			} else if(!QueueStudenti.isEmpty()) {
				ComputersLock.lock();
				int i = 0;
				try {
					//aspetto che almeno un Pc sia libero
					while((i = findPc()) == -1) 
						ComputersCondition.await();
					
					
					//System.out.println("Pc assegnato: " + i);
					Studente tmp = (Studente) QueueStudenti.remove();
					tmp.setPc(i);
					computers[i] = 1;
					tmp.work();
					
				} catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					ComputersLock.unlock();
				}
			}
			
			
				
			
		}
	
	}
	
	//Setta a 'libera' la postazione liberata e quindi segnala che almeno un utente può entrare nel lab
	//<user>: il tipo di utente che ha finito il suo lavoro
	//<tmp> : l'utente che ha finito
	public void finish(String user, Object tmp) {
		ComputersLock.lock();
		if(user.equals("Professore")) {
			for(int i = 0; i < posti; i++)
				computers[i] = 0;
			ComputersCondition.signal();
		}
		
		if(user.equals("Tesista")) {
			computers[PcTesisti] = 0;
			ComputersCondition.signal();
		}
		
		if(user.equals("Studente")) {
			Studente st = (Studente) tmp;
			computers[st.getPc()] = 0;
			ComputersCondition.signal();
		}
		
		ComputersLock.unlock();
	}
	
	//Controlla che tutti i Pc siano liberi
	public boolean checkLabFree() {
		ComputersLock.lock();
		for(int i = 0; i < this.posti; i++) {
			if(computers[i] == 1) {
				ComputersLock.unlock();
				return false;
			}
		}
		ComputersLock.unlock();
		return true;
	}
	
	//Trova un Pc libero cercando in ordine crescente nell'array computers
	public int findPc() {
		ComputersLock.lock();
		int i = 0;
		while(i < this.posti) {
			if(computers[i] == 0) {
				ComputersLock.unlock();
				return i;
			}
			i++;
		}
		ComputersLock.unlock();
		return -1;
	}
}
