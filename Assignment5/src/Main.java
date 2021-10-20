import java.io.File;

public class Main {
	private static final int NumConsumers = 4;
	
	public static void main(String[] args) throws InterruptedException {
		
		File dir;
		if(args.length == 0)
			dir = null;
		else dir = new File(args[0]);
		
		if(dir != null && dir.isDirectory()) {
			System.out.println(dir);
	
			Monitor monitor = new Monitor();
			
			Thread producer = new Thread(new Producer(dir, monitor));
			producer.start();
			
			Thread[] consumer = new Thread[NumConsumers];
			for(int i = 0; i < NumConsumers; i++) {
				consumer[i] = new Thread(new Consumer(monitor));
				consumer[i].start();
			}
			
			producer.join();
			
			for(int i = 0; i<NumConsumers; i++) {
				consumer[i].interrupt();
			}
			
			for(int i = 0; i<NumConsumers; i++) {
				consumer[i].join();
			}
			
			
		}
		else {
			System.out.println("Arg is not a directory");
			System.exit(1);
		}
		
	}
}
