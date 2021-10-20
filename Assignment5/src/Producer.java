import java.io.File;
import java.io.IOException;

public class Producer implements Runnable{

	private File dir;
	private Monitor monitor;
	
	public Producer(File dir, Monitor monitor) {
		this.dir = dir;
		this.monitor = monitor;
	}
	
	public void run() {
		
		try {
			RecVisitDir(dir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			monitor.addQueue(dir.getCanonicalPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	private void RecVisitDir(File dir) throws IOException {
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isDirectory()) {
				RecVisitDir(file);
				monitor.addQueue(file.getCanonicalPath());
			}
		}
		
	}
	
}
