 import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;


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
				ContoCorrente contoCorrente = new Gson().fromJson(reader, ContoCorrente.class);
				System.out.println(contoCorrente);
			}
			//reader.endObject();
		}catch(FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Error of I/O: "+ e.getMessage());
			System.exit(1);
		}
	}
}
