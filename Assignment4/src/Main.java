import java.util.Scanner;
import java.util.Random;

public class Main {

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
			
			Laboratorio lab = new Laboratorio(posti);
			Tutor tutor = new Tutor(lab);
			//Thread ThreadTutor = new Thread(tutor);
					
			int pc = -1;
			PcTesisti = random.nextInt(posti-1);
			
			for(int i = 0; i < numStudenti; i++) {
				User u = new User(lab, tutor, "Studente", i, pc);
				tutor.InsertQueue(u);
			}
			
			for(int i = 0; i < numProfessori; i++) {
				User u = new User(lab, tutor, "Professore", i, pc);
				tutor.InsertQueue(u);
			}
			
			for(int i = 0; i < numTesisti; i++) {
				User u = new User(lab, tutor, "Tesista", i, PcTesisti);
				tutor.InsertQueue(u);
			}
			
			
			tutor.start();
			
	}

}
