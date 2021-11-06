import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class Client {
	
	public static final int port = 9999;
	public static final int bufSize = 1024;
	public static final int timeout = 20000;
	
	public static void main(String[] args) {
		DatagramSocket socketClient = null;
		byte[] content = new String("PING").getBytes();
		byte[] buf = new byte[bufSize];
		
		try {
			socketClient = new DatagramSocket();
			socketClient.setSoTimeout(timeout);
			
			InetAddress addr = InetAddress.getByName("localhost");
			DatagramPacket sp = new DatagramPacket(content, content.length, addr, port);
			socketClient.send(sp);
			System.out.println("Richiesta inviata");
			
			//Attendo una risposta
			DatagramPacket rp = new DatagramPacket(buf, buf.length);
			socketClient.receive(rp);
			System.out.printf("Risposta ricevuta: %s\n", new String(rp.getData()));
		} catch(SocketTimeoutException e) {
			System.out.println("Nessuna risposta ricevuta");
		}catch(Exception e) {
			System.err.println("Errore: "+e.getMessage());
		}
		finally {
			socketClient.close();
			System.out.println("Client terminato");
		}
	}

}
