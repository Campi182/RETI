import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Server {
	
	public static final int timeout = 120000;
	public static final int bufSize = 1024;
	
	public static int port;
	public static long seed;
	
	public static void main(String[] args) {
		DatagramSocket serverSocket = null;
		
		try {
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			System.err.println("Usage: <port>");
			System.exit(1);
		}
		
		try {
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(timeout);
		} catch(SocketException e) {
			System.err.println("Error initializing server");
			System.exit(1);
		}
		
		byte[] buf = new byte[bufSize];
		System.out.printf("Server pronto sulla porta %s\n", port);
		
		try {
			while(true) {
				Arrays.fill(buf, (byte)0);
				//Mi metto in attesa di richieste
				DatagramPacket rp = new DatagramPacket(buf, buf.length);
				serverSocket.receive(rp);
				String msg = new String(rp.getData(), 0, rp.getLength(), StandardCharsets.US_ASCII);
				System.out.println(rp.getAddress().toString()+":> "+rp.getPort()+" "+msg+" ACTION: accepted");
				//System.out.printf("Richiesta ricevuta: %s\n", new String(rp.getData()));
				//Preparo la risposta
				InetAddress addr = rp.getAddress();
				byte[] reply = new String(rp.getData()).getBytes();
				//System.out.println(new String(reply));
				DatagramPacket sp = new DatagramPacket(reply, reply.length, addr, port);
				serverSocket.send(sp);
				System.out.println("Risposta inviata");
			}
		} catch(SocketTimeoutException e) {
			System.out.println("Terminazione del server....");
		} catch(IOException e) {
			System.err.println("Errore di I/O: "+ e.getMessage());
		} finally {
			serverSocket.close();
			System.out.println("Server termianto");
		}

	}

}
