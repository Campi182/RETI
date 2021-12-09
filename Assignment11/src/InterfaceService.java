import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceService extends Remote{
	
	boolean register(String speaker, int day, int intervento) throws RemoteException;
	
	ArrayList<ProgrammaCongresso> getProgram() throws RemoteException;
}
