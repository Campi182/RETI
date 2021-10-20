import java.util.LinkedList;

public class Monitor {

	private LinkedList<String> queue = new LinkedList<String>();
	
	public Monitor() {
		
	}
	
	public synchronized void addQueue(String dirname) {
		queue.add(dirname);
		notifyAll();
	}
	
	public synchronized String removeQueue() {
		while(queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				return null;
			}
		}
		
		String dirname = queue.remove();
		return dirname;
	}
	
}
