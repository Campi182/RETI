import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class Client {
	
	public static int port;
	public static String address;
	public static InetAddress addr;
	
	public static final int timeout = 2000;
	public static final int bufSize = 500;

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
		addr = InetAddress.getByName(args[0]);
		}catch (UnknownHostException e) {
			System.err.println("ERR -arg 1");
			System.exit(1);
		}
		
		DatagramSocket clientSocket = null;
		int packetsReceived = 0;
		long min = Long.MAX_VALUE ,max = 0;
		double avg = 0;
		
		byte[] content = null;
		byte[] buf = new byte[bufSize];
		
		try {
			clientSocket = new DatagramSocket();
			clientSocket.setSoTimeout(timeout);	//attendo 2 secondi per una risposta - se non arriva -> pacchetto perso
		} catch(SocketException e) {
			System.out.println("Error: "+ e.getMessage());
			return;
		}
		
		for(int i = 0; i < 10; i++) {
			try {
				long start = System.currentTimeMillis();
				content = new String("PING "+i+" "+start).getBytes();
				DatagramPacket sp = new DatagramPacket(content, content.length, addr, port);
				clientSocket.send(sp);
				System.out.print("PING "+i+" "+start+" RTT: ");
				
				//Attendo una risposta
				DatagramPacket rp = new DatagramPacket(buf, buf.length);
				clientSocket.receive(rp);
				long end = System.currentTimeMillis();
				long rtt = end - start;
				System.out.println(rtt+" ms");
				packetsReceived++;
				
				avg+=rtt;
				if(rtt < min)	min = rtt;
				if(rtt > max)	max = rtt;
			} catch(Exception e) {
				System.out.println("*");
			}
		}
		
		
		clientSocket.close();
		System.out.println("\n----- PING statistics -----");
		System.out.printf("10 packets transmitted, %d packets received, %s%% packet loss\n", packetsReceived, (10-packetsReceived)*10);
		if(packetsReceived > 0)
			System.out.printf("round-trip (ms) min/avg/max = %d / %.2f / %d", min, avg/packetsReceived, max);
	}
}
