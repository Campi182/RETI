import java.util.Scanner;
import java.util.Random;

public class Main extends Thread{

	public static int numStudenti;
	public static int numTesisti;
	public static int numProfessori;
	public static int PcTesisti;
	final static int posti = 20;
	public static int totUsers;
	
	private static Random random = new Random();
	
	public static void main(String[] args) throws InterruptedException {
		
			Scanner input = new Scanner(System.in);
			System.out.println("Inserisci numero studenti: ");
			numStudenti = input.nextInt();
			System.out.println("Inserisci numero tesisti: ");
			numTesisti = input.nextInt();
			System.out.println("Inserisci numero professori: ");
			numProfessori = input.nextInt();
			input.close();
			totUsers = numStudenti + numProfessori + numTesisti;
			PcTesisti = random.nextInt(posti);
			
			Laboratorio lab = new Laboratorio(posti, PcTesisti);
			User[] users = new User[totUsers];
			Thread[] ThreadUsers = new Thread[totUsers];
			
			int curr = 0;
			int pc = -1;

			System.out.println("PC TESISTI: "+PcTesisti);
			
			for(int i = 0; i < numProfessori; i++) {
				users[curr]= new User(lab,"Professore", pc);
				ThreadUsers[curr] = new Thread(users[curr]);
				curr++;
			}	
			
			for(int i = 0; i < numTesisti; i++) {
				users[curr] = new User(lab, "Tesista", PcTesisti);
				ThreadUsers[curr] = new Thread(users[curr]);
				curr++;
			}
			
			for(int i = 0; i < numStudenti; i++) {
				users[curr] = new User(lab,"Studente", pc);
				ThreadUsers[curr] = new Thread(users[curr]);
				curr++;
			}
			
			for(int i = 0; i<totUsers; i++)
				ThreadUsers[i].start();
			
			
			//!!!ASPETTO CHE TERMINANO I THREAD STUDENTI
			for(int i = 0; i<totUsers-1; i++) {
				try {
					ThreadUsers[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
	}

}
