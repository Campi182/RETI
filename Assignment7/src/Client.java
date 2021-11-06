import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Timestamp;


public class Client {
	
	public static int port;
	public static String address;
	public static InetAddress addr;
	
	public static final int timeout = 40000;
	public static final int bufSize = 1024;

	public static void main(String[] args) {
		
		if(args.length != 2) {
			System.err.println("Usage: <address> <port>");
			System.exit(1);
		}
		
		//salvo il port number
		try {
		port = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			System.err.println("ERR -arg 2");
			System.exit(1);
		}
		
		try {
		addr = InetAddress.getByName(address);
		}catch (UnknownHostException e) {
			System.out.println("ERR -arg 1");
		}
		
		DatagramSocket clientSocket = null;
		byte[] content = null;
		byte[] buf = new byte[bufSize];
		
		try {
			clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(timeout);
		} catch(SocketException e) {
			System.out.println("Nessuna risposta ricevuta");
			return;
		}
		
		try {
			for(int i = 0; i < 10; i++) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				content = new String("PING "+i+" "+timestamp).getBytes();
				//System.out.println(new String(content));
				DatagramPacket sp = new DatagramPacket(content, content.length, addr, port);
				clientSocket.send(sp);
				System.out.println("Richiesta inviata");
				
				//Attendo una risposta
				DatagramPacket rp = new DatagramPacket(buf, buf.length);
				clientSocket.receive(rp);
				System.out.printf("Risposta ricevuta: %s\n", new String(rp.getData()));
				
				Thread.sleep(2000);
			}
		} catch(Exception e) {
			System.err.println("Errore: "+ e.getMessage());
		} finally {
			clientSocket.close();
			System.out.println("Client terminato");
		}
		
	}//MAIN

}
