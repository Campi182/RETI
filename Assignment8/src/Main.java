import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.*;
import java.nio.ByteBuffer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;



public class Main {

	public static final String file = "ContiCorrenti.txt";
	
	public static final int bufSize = 8192;
	
	public static void main(String[] args) {
		
		try(
			FileOutputStream os = new FileOutputStream(file);
			FileChannel oc = os.getChannel();
			){
			ByteBuffer buf = ByteBuffer.allocate(bufSize); //buffer dove scrivo il conteuto serializzato da andare a scrivere nel file
			
			//preparo gli oggetti contocorrente da andare a scrivere nel file
			List<Movement> AliceMovements = new ArrayList<>();
			createListofMovements(AliceMovements);
			ContoCorrente alice = new ContoCorrente("Alice", "Rossi", 1, AliceMovements);
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String aliceJson = gson.toJson(alice);
			buf.clear();
			buf.put(aliceJson.getBytes());
			buf.flip();
			while(buf.hasRemaining())oc.write(buf);
			//ho scritto nel file gli oggetti
			
			//creo thread che legge i conti corrente e li passa al threadpool
			Dispatcher dispatcher = new Dispatcher(file);
			Thread threadDeserializer = new Thread(dispatcher);
			threadDeserializer.start();
			
			

			
		} catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Error of I/O: " + e.getMessage());
			System.exit(1);
		}

	}
	
	public static List<Movement> createListofMovements(List<Movement> list) {
		Random rand = new Random();
		for(int i = 0; i < 5; i++) {
			int month = rand.nextInt(12)+1;
			int year = rand.nextInt(2)+2019;
			int day = rand.nextInt(28)+1;
			LocalDate date = LocalDate.of(year, month, day);
			list.add(new Movement(date, Causali.Accredito));
		}
		return list;
	}

}
