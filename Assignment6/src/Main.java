import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) throws Exception{
		
		try(ServerSocket listener = new ServerSocket(6789)){ 
			System.out.println("Server is running...");
			ExecutorService pool = Executors.newFixedThreadPool(10);
			
			while(true)
				pool.execute(new TransferFile(listener.accept()));
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("Server failure");
			System.exit(-1);
		}
	}

}
