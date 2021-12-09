import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MainServer implements InterfaceService{

	public final ArrayList<ProgrammaCongresso> giorni;
	
	public MainServer() {
		this.giorni = new ArrayList<ProgrammaCongresso>();
		for(int i = 0; i < 3; i++)
			giorni.add(new ProgrammaCongresso());
	}
	
	//implemento i metodi dell'interfaccia (metodi chiamati dal client tramite stub)
	public boolean register(String speaker, int day, int intervento) throws RemoteException{
		if( (day>0 && day<=3) && (intervento>0 && intervento <= 12)) {
			ArrayList<String> speakers = giorni.get(day-1).getSession("S"+intervento);
			if(speakers.size()<=4) {
				System.out.println("Speaker aggiunto con successo");
				speakers.add(speaker);
				return true;
			}
			else {
				System.out.println("ERROR: Slot speaker pieni. Provare un'altra sessione");
				return false;
			}
		} else {
			System.out.println("ERROR: Giornata non esistente");
			return false;
		}
	}
	
	public ArrayList<ProgrammaCongresso> getProgram() throws RemoteException{
		return this.giorni;
	}
	
	
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Usage: nedds 1 argument <Port>");
			System.exit(1);
		}
		
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			System.out.println("Not a number");
			System.exit(0);
		}
		
		
		//Setup server
		MainServer server = new MainServer();
		
		try {
			InterfaceService stub = (InterfaceService) UnicastRemoteObject.exportObject(server, 0);
			LocateRegistry.createRegistry(port);
			Registry registry = LocateRegistry.getRegistry(port);
			registry.rebind("Server-Congresso", stub);
			System.out.println("Server pronto");
		}catch(RemoteException e) {
			System.out.println("Communication error: " + e.toString());
		}
	}

}
