import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Server {
	
	public static final int timeout = 120000;
	public static final int bufSize = 1024;
	
	public static int port;
	public static long seed;
	
	public static void main(String[] args) throws InterruptedException {
		DatagramSocket serverSocket = null;
		Random rand = new Random();
		
		try {
			port = Integer.parseInt(args[0]);
		}catch(NumberFormatException e) {
			System.err.println("Usage: <port>");
			System.exit(1);
		}
		
		if(args.length == 2) {
		seed = Integer.parseInt(args[1]);
		rand.setSeed(seed);
		}
		
		try {
			serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(timeout);
		} catch(SocketException e) {
			System.err.println("Error initializing server");
			System.exit(1);
		}
		
		byte[] buf = new byte[bufSize];
		
		try {
			while(true) {
				//Mi metto in attesa di richieste
				DatagramPacket rp = new DatagramPacket(buf, buf.length);
				serverSocket.receive(rp);
				String msg = new String(rp.getData(), 0, rp.getLength(), StandardCharsets.US_ASCII);
				
				//decido se inviare o no il pack
				boolean val = rand.nextInt(4) == 0; //4 perchè probabilità 25%
				if(val) {
					int delay = rand.nextInt(1000);
					System.out.println(rp.getAddress().toString()+":"+rp.getPort()+"> "+msg+" ACTION: delayed "+ delay+" ms");
					
					//Preparo la risposta
					InetAddress addr = rp.getAddress();
					port = rp.getPort();
					byte[] reply = new String(rp.getData()).getBytes();
					//System.out.println(new String(reply));
					DatagramPacket sp = new DatagramPacket(reply, reply.length, addr, port);
					Thread.sleep(delay);
					serverSocket.send(sp);
					//System.out.println("Risposta inviata");
				} else {
					System.out.println(rp.getAddress().toString()+":"+rp.getPort()+"> "+msg+" ACTION: not sent ");
				}

			}
		} catch(SocketTimeoutException e) {
			System.out.println("Terminazione del server....");
		} catch(IOException e) {
			System.err.println("Errore di I/O: "+ e.getMessage());
		} finally {
			serverSocket.close();
		}

	}

}
