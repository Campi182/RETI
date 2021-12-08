import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServer {

	public static int PORT = 6789;
	
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("Usage: need 1 argument1 <IP>");
			System.exit(1);
		}
		
		try {
			InetAddress dategroup = InetAddress.getByName(args[0]);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DatagramSocket ms = new DatagramSocket();

			while(true) {
				Date date = new Date(System.currentTimeMillis());
				byte[] data = (formatter.format(date)).getBytes();
				DatagramPacket dp = new DatagramPacket(data, data.length, dategroup, PORT);
				System.out.println("Sending date and time: " + new String(data));
				ms.send(dp);
				Thread.sleep(1000);
			}
		}catch(Exception e) {
			System.out.println(e);
		}

	}

}
