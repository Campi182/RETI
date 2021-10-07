import java.util.Scanner;

public class Main {
	public static int numStudenti;
	public static int numTesisti;
	public static int numProfessori;
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Inserisci numero studenti: ");
		numStudenti = input.nextInt();
		System.out.println("Inserisci numero tesisti: ");
		numTesisti = input.nextInt();
		System.out.println("Inserisci numero professori: ");
		numProfessori = input.nextInt();
		input.close();
		
		Laboratorio lab = new Laboratorio(20);
		Thread ThreadLab = new Thread(lab);
		ThreadLab.start();
		
		
		//creo un array per ogni tipo di utente ed un array Thread(per ogni tipo di utente) dove ogni utente è un thread
		
		Studente[] studenti = new Studente[numStudenti];
		Thread[] ThreadStudenti = new Thread[numStudenti];
		
		Tesisti[] tesisti = new Tesisti[numTesisti];
		Thread[] ThreadTesisti = new Thread[numTesisti];
		
		Professore[] professori = new Professore[numProfessori];
		Thread[] ThreadProfessori = new Thread[numProfessori];
		
		//avvio tutti i thread
		for(int i = 0; i<numProfessori; i++) {
			professori[i] = new Professore(lab, "Professore " +i);
			ThreadProfessori[i] = new Thread(professori[i]);
			ThreadProfessori[i].start();
		}

		for(int i = 0; i<numTesisti; i++) {
			tesisti[i] = new Tesisti(lab, "Tesista " + i);
			ThreadTesisti[i] = new Thread(tesisti[i]);
			ThreadTesisti[i].start();
		}
		
		for(int i = 0; i < numStudenti; i++) {
			studenti[i] = new Studente(lab, "Studente " + i);
			ThreadStudenti[i] = new Thread(studenti[i]);
			ThreadStudenti[i].start();
		}
				
		
		//devo fare la join su tutti i thread
		
		for(int i = 0; i < numStudenti; i++) {
			try{
				ThreadStudenti[i].join();
			} catch(InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		for(int i = 0; i < numTesisti; i++) {
			try{
				ThreadTesisti[i].join();
			} catch(InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		for(int i = 0; i < numProfessori; i++) {
			try{
				ThreadProfessori[i].join();
			} catch(InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		System.out.println("Chiusura del laboratorio\n");
		ThreadLab.interrupt();
		
		try{
			ThreadLab.join();
		} catch(InterruptedException e) {
			//e.printStackTrace();
			System.exit(1);
		}
	}
}
