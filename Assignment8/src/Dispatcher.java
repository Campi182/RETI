 import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.reflect.TypeToken;



public class Dispatcher implements Runnable{

	public static final int bufSize = 8192;
	public static String file;
	
	public Dispatcher(String file) {
		Dispatcher.file = file;
	}
	
	public void run() {
		try(FileInputStream is = new FileInputStream(file)){
			JsonReader reader = new JsonReader(new InputStreamReader(is));
			//reader.beginObject();
			while(reader.hasNext()) {
				
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				ContoCorrente contoCorrente = gson.fromJson(reader, ContoCorrente.class);
				System.out.println(contoCorrente.getName());
			}
			//reader.endObject();
			
			//lasciando i reader.beginObject, reader.endObject mi da errore:
			// Expected BEGIN_OBJECT but was END_DOCUMENT
			//togliendo i reader.beginObject, reader.endObject mi da errore:
			//Expected BEGIN_OBJECT but was END_DOCUMENT at line 47 column 2 path $
			//come se il JSON l ha letto correttamente
			
			
			//!!!così funziona!! commentando da reader.begin fino a reader.end
			//Gson gson = new GsonBuilder().setPrettyPrinting().create();
			//ContoCorrente contoCorrente = gson.fromJson(reader, ContoCorrente.class);
			//System.out.println(contoCorrente.getName());
			
		}catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Error of I/O: "+ e.getMessage());
			System.exit(1);
		}
	}
}
