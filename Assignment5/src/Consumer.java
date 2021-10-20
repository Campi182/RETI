import java.io.File;
import java.io.IOException;

public class Consumer implements Runnable{

	private Monitor monitor;
	
	public Consumer(Monitor monitor) {
		this.monitor = monitor;
	}
	
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			String pathname = monitor.removeQueue();
			if(pathname == null)
				return;
			else {
				File readPath = new File(pathname);
				printContent(readPath);
			}
		}
		
	}

	private void printContent(File readPath) {
		File[]files = readPath.listFiles();
		if(files != null) {
			for(File file : files) {
				try {
					if(file.isFile())
						System.out.println(Thread.currentThread().getName()+": file - "+file.getName()+"\t\t - in dir: "+readPath.getCanonicalPath());
					if(file.isDirectory())
						System.out.println(Thread.currentThread().getName()+": dir - "+file.getName()+"\t\t\t - in dir: "+readPath.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
}
