import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
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
			
			String path = in.nextLine().split(" ")[1].substring(1);	//substring perchè tolgo il '/' all'inizio del path
			String extension;
			StringBuilder headers;
			byte[] BytesFile;
			//System.out.println(path);
			
			
			File RequestedFile = new File(path);
			FileInputStream file;
			try {
				file = new FileInputStream(RequestedFile);
			} catch(FileNotFoundException e) {
				System.out.println("404 File not found\n");
				out.write(fileNotFoundMessage());
				return;
			}
			
			int index = path.indexOf(".");
			extension = path.substring(index);
			
			
			headers = new StringBuilder("HTTP/1.1 200 OK\n" + "Server: TransferFileServer\n");
			
			switch(extension) {
			case ".txt":
				headers.append("Content-Type: text/plain\n");
				break;
			case ".jpg":
				headers.append("Content-Type: image/jpg\n");
				break;
			case ".gif":
				headers.append("Content-Type: image/gif\n");
				break;
			default:
				break;
			}
				
				
			//aggiungo allo stream di uscita un 'a capo' per segnalare la fine degli headers ??
			headers.append("\n");
			
			
			try {
				BytesFile = new byte[(int)RequestedFile.length()];
				if(file.read(BytesFile) == -1) {
					System.out.println("Failed reading file");
					file.close();
					out.close();
					return;
				}
				file.close();
				
				//outstream da inviare
				out.write(headers.toString().getBytes());	//headers 
				out.write(BytesFile);	//dati
				out.flush();
				
			} catch(IOException e) {
				e.printStackTrace();
			}
			
		} catch(Exception e) {
			System.out.println("Error "+ socket);
		}
	}//run
	
	//invoke quando 404 File Not Found
	private byte[] fileNotFoundMessage(){
		String mes = "HTTP/1.1 404 Not Found\n"+"Server: TransferFileSever\n";
		return mes.getBytes();
	}
}
