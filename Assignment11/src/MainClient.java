import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MainClient {

	public static void main(String[] args) {
		if(args.length != 1){
			System.out.println("Usage: needs 1 argument <Port>");
			System.exit(1);
		}
		
		try {
			Registry registry = LocateRegistry.getRegistry(Integer.parseInt(args[0]));
			InterfaceService stub = (InterfaceService) registry.lookup("Server-Congresso");
			
			while(true) {
				System.out.println("Cosa vuoi fare?");
				System.out.println("- add (add speaker)\n- get (get program)\n-quit\n");
				Scanner in = new Scanner(System.in);
				String str = in.nextLine();
				
				if(str.equalsIgnoreCase("quit")) break;
				
				if(str.equalsIgnoreCase("add"))
					addSpeaker(stub, in);
				
				if(str.equalsIgnoreCase("get"))
					getProgram(stub);
			}	
		}catch(Exception e) {
			System.err.println("Exception in client: " + e.toString());
			e.printStackTrace();
		}

	}
	
	
	public static void addSpeaker(InterfaceService stub, Scanner in) throws RemoteException {
		int day, session;
		System.out.println("Insert day: ");
		day = Integer.parseInt(in.nextLine());
		System.out.println("Insert session number: ");
		session = Integer.parseInt(in.nextLine());
		System.out.println("Insert speaker name: ");
		String speaker = in.nextLine();
		
		boolean op = stub.register(speaker, day, session);
		if(op)
			System.out.println("Speaker registered");
		else System.out.println("Error during registration");
	}
	
	public static void getProgram(InterfaceService stub) throws RemoteException{
		int giorno = 1;
		ArrayList<ProgrammaCongresso> program = stub.getProgram();
		
		for(ProgrammaCongresso day : program) {
			System.out.println("Giorno: " + giorno);
			if(day.getProgram() != null)
				System.out.println(Arrays.toString(day.getProgram().entrySet().toArray()));
			giorno++;
		}
	}


}
