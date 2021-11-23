import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;


public class EchoServer {

	private static int PORT = 6789;
	private static Selector selector = null;
	
	public static void main(String[] args) {
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
			serverSocketChannel.configureBlocking(false);
			
			//Selector
			selector = Selector.open();
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		System.out.println("SERVER IS ON");
		
		while(true) {
			try {
				selector.select();
			} catch(IOException e) {
				e.printStackTrace();
				break;
			}
		
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				
				try {
					if(key.isAcceptable()) {	//richieste di connessione
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						client.configureBlocking(false);
						System.out.println("Accepted connection from " + client);
						SelectionKey clientKey = client.register(selector, SelectionKey.OP_READ);
					}
					else if(key.isReadable()) {	//Richieste di lettura
						SocketChannel client = (SocketChannel) key.channel();
						int n_read = client.read(buffer);
						buffer.flip();
						String str_received = new String(buffer.array(), StandardCharsets.UTF_8).trim();	//reading
						if(str_received.equalsIgnoreCase("quit") || n_read == -1) {
							client.close();
							key.cancel();
							System.out.println("Quit: connection closed.");
						} else {
							key.interestOps(SelectionKey.OP_WRITE);
						}
					} else if(key.isWritable()) {
						SocketChannel client = (SocketChannel) key.channel();
						client.write(buffer);
						buffer.clear();
						key.interestOps(SelectionKey.OP_READ);
					}
				} catch(ClosedChannelException e) {
					e.printStackTrace();
				} catch(IOException e) {
					key.cancel();
					try {
						key.channel().close();
					} catch(IOException ex) {}
				}
			}//try
		}//while
		}catch(IOException e){
			e.printStackTrace();
		}
	}//main
}//class
