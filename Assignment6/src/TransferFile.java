import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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
			OutputStream out = socket.getOutputStream();
			){
			
			String path = in.nextLine().split(" ")[1].substring(1);	//substring perchè tolgo il / all'inizio del path
			String extension;
			StringBuilder headers;
			byte[] BytesFile = null;
			
			/*if(path.equals("HTTP")) {	//!!!!!NON VA BENE!!!!!!//
				//redirect to qualcosa
				System.out.println("Richiesta nulla");
			} else System.out.println("Richiesta: "+path);*/
			
			
			try {
			File RequestedFile = new File(path);
			BytesFile = Files.readAllBytes(Paths.get(path));
			} catch(NoSuchFileException e) {
				System.out.println("404 File not found\n");
				out.write(fileNotFoundMessage());
			}
			
			int index = path.indexOf(".");
			extension = path.substring(index);
			System.out.println("Extension: "+extension);
			
			//!!!!INIZIALIZZAZIONE DEGLI HEADERS!!!!//
			headers = new StringBuilder("HTTP/1.1 200 OK\n" + "Server: TransferFileServer\n");
			
			switch(extension) {
			case ".txt":
				headers.append("Content-Type: text/plain\n");
				break;
			case ".jpg":
				
				break;
			case ".gif":
				
				break;
			default:
				break;
			}
				
				
			//aggiungo allo stream di uscita un 'a capo' per segnalare la fine degli headers
			headers.append("\n");
			
			out.write(headers.toString().getBytes());
			out.write(BytesFile);
			out.flush();
			
		} catch(Exception e) {
			System.out.println("Error "+ socket);
		}
	}//run
	
	//invoke when 404 File Not Found
	private byte[] fileNotFoundMessage(){
		String mes = "HTTP/1.1 404 Not Found\n"+"Server: TransferFileSever\n"+"Content-Type: text/html\n\n"+"<h1>ERROR 404&nbsp;</h1>\n"+"<p>Not Found</p>";
		return mes.getBytes();
	}
}
