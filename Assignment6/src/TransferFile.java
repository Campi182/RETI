import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class TransferFile implements Runnable{

	private Socket socket;
	public TransferFile(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		System.out.println("Connected: "+socket);
		
		try(Scanner in = new Scanner(socket.getInputStream());
			OutputStream out = socket.getOutputStream()
			){
			String PathFile = in.nextLine().split(" ")[1].substring(1);
			System.out.println("Requested: " + PathFile);
			try {
				File FileToSend = new File(PathFile);
				byte[] fileByte = null;
				fileByte = Files.readAllBytes(Paths.get(PathFile));
				
				//response
				out.write(fileByte);
				out.flush();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}//try
		catch (IOException e) {

			e.printStackTrace();
		}
	}//run
}
