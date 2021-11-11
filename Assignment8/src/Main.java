import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java.nio.*;

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
			List<Movement> AliceMovements;
			ContoCorrente Alice = new ContoCorrente("Alice", "Rossi", 1, AliceMovements);
			
		} catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Error of I/O: " + e.getMessage());
			System.exit(1);
		}

	}
	
	public List<Movement> createListofMovements(List<Movement> list) {
		Date date = new Date();
		date.s
		Causali causale;
		
		for(int i = 0; i < 10; i++) {
			list = Arrays.asList(new Movement(null, null))
		}
	}

}
