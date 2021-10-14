public class Laboratorio{

	private final int[] computers;
	private int posti;
	private int PcInUse = 0;
	
	public Laboratorio(int posti) {		
		this.posti = posti;
		computers = new int[this.posti];
		for(int i = 0; i < posti; i++)
			computers[i] = 0;
		
	}
	
	public synchronized void setLabBusy() throws InterruptedException{
		//se c'è qualche PC occupato devo aspettare
		while(PcInUse > 0)
			this.wait();
		
		for(int i = 0; i < posti; i++) {
			PcInUse++;
			computers[i] = 1;
		}
		//System.out.println("Settati tutti i pc da professore");
		this.notify();
	}
	
	public synchronized void setPc(int i) throws InterruptedException {
		while(computers[i] == 1) {
			this.wait();
		}
		
		computers[i] = 1;
		PcInUse++;
		//System.out.println("Occupato computer tesisti: " + i);
		this.notify();
	}
	
	public synchronized int setPc() throws InterruptedException{
		int i;
		
		while(PcInUse == posti)
			this.wait();
		
		for(i = 0; i < posti; i++) {
			if(computers[i] == 0) {
				PcInUse++;
				computers[i] = 1;
				break;
			}
		}
		this.notify();
		//System.out.println("Occupato computer " + i);
		return i;
	}
	
	public synchronized void freeLab() throws InterruptedException{
		for(int i = 0; i < posti; i++) {
			computers[i] = 0;
			PcInUse--;
		}
		this.notify();
		//System.out.println("Liberato tutto il laboratorio");
	}
	
	public synchronized void freePc(int i) throws InterruptedException{
		computers[i] = 0;
		PcInUse--;
		this.notify();
		//System.out.println("Liberato computer " + i);
	}
	
}