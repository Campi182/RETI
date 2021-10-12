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
			
			
			User[] studenti = new User[numStudenti];
			Thread[] ThreadStudenti = new Thread[numStudenti];
			
			User[] tesisti = new User[numTesisti];
			Thread[] ThreadTesisti = new Thread[numTesisti];
			
			User[] professori = new User[numProfessori];
			Thread[] ThreadProfessori = new Thread[numProfessori];
		
			//avvio tutti i thread
			for(int i = 0; i<numProfessori; i++) {
				professori[i] = new User(lab, "Professore " +i);
				ThreadProfessori[i] = new Thread(professori[i]);
				ThreadProfessori[i].start();
			}

			for(int i = 0; i<numTesisti; i++) {
				tesisti[i] = new User(lab, "Tesista " + i);
				ThreadTesisti[i] = new Thread(tesisti[i]);
				ThreadTesisti[i].start();
			}
			
			for(int i = 0; i < numStudenti; i++) {
				studenti[i] = new User(lab, "Studente " + i);
				ThreadStudenti[i] = new Thread(studenti[i]);
				ThreadStudenti[i].start();
			}
					
			ThreadLab.start();
			
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
