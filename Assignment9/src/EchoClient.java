import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EchoClient{
	
	private static int PORT = 6789;
	
	public static void main(String args[]) {
		SocketChannel socketChannel;
		
		try {
			socketChannel = SocketChannel.open();
			socketChannel.connect(new InetSocketAddress("localhost", PORT));
			//bloccante, si blocca finchè non si connette
			while(true) {
				Scanner input = new Scanner(System.in);
				String str = input.nextLine();
				socketChannel.write(ByteBuffer.wrap(str.getBytes(StandardCharsets.UTF_8)));
				
				if(str.equalsIgnoreCase("quit"))	break;
				
				ByteBuffer buffer = ByteBuffer.allocate(str.getBytes(StandardCharsets.UTF_8).length + (" - echoed by server".getBytes(StandardCharsets.UTF_8)).length); //creo il buffer che contiene la risposta
				socketChannel.read(buffer);
				buffer.flip();
				String response = new String(buffer.array()).trim();
				System.out.println(response + " - echoed by server");
				buffer.clear();
			}
			socketChannel.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}