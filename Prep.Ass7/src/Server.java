import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server {

	public static final int port = 9999;
	public static final int timeout = 20000;
	public static final int bufSize = 1024;
	
	public static void main(String[] args) {

		DatagramSocket serverSocket = null;
		
		try {
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(timeout);
		} catch(SocketException e) {
			System.err.print("Error initializing socket");
			System.exit(1);
		}
		
		byte[] buf = new byte[bufSize];
		System.out.printf("Server pronto sulla porta %s\n", port);
		
		try {
			while(true) {
				//Mi metto in attesa di richieste
				DatagramPacket rp = new DatagramPacket(buf, buf.length);
				serverSocket.receive(rp);
				System.out.printf("Richiesta ricevuta: %s\n", new String(rp.getData()));
				
				//Preparo la risposta
				InetAddress addr = rp.getAddress();
				int port = rp.getPort();
				byte[] reply = new String("PONG").getBytes();
				
				DatagramPacket sp = new DatagramPacket(reply, reply.length, addr, port);
				serverSocket.send(sp);
				System.out.println("Risposta inviata");
			}
		} catch(SocketTimeoutException e) {
			System.out.println("Terminazione del server...");
		}
		catch(IOException e) {
			System.err.println("Errore di I/O: "+e.getMessage());
		}
		finally {
			serverSocket.close();
			System.out.println("Server terminato.");
		}

	}

}
