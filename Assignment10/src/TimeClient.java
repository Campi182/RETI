import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class TimeClient {

	public static int PORT = 6789;
	
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("Usage: needs 1 argument <IP>");
			System.exit(1);
		}
		
		MulticastSocket ms = null;
		InetAddress dategroup = null;
		
		try {
			dategroup = InetAddress.getByName(args[0]);
			ms = new MulticastSocket(PORT);
			ms.joinGroup(dategroup);
			byte[] buffer = new byte[1024];
			DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
			
			for(int i = 0; i < 10; i++) {
				ms.receive(dp);
				String s = new String(dp.getData());
				System.out.println(s);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		
		if(ms != null) {
			try {
				ms.leaveGroup(dategroup);
				ms.close();
			}catch(IOException e) {}
		}

	}

}
