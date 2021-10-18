public class Laboratorio{

	private final int[] computers;
	private int posti;
	private int PcInUse = 0;
	private int PcTesisti;
	private int qProfessori = 0; //numero professori attulmente in coda
	private int qTesisti = 0;	//numero tesisti attualmente in coda
	
	public Laboratorio(int posti, int PcTesisti) {		
		this.posti = posti;
		this.PcTesisti = PcTesisti;
		computers = new int[this.posti];
		for(int i = 0; i < posti; i++)
			computers[i] = 0;
	}
	
	public synchronized void setPcProfessore(){
		qProfessori++;
		//se c'è qualche Pc occupato devo aspettare
		while(PcInUse > 0)
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		qProfessori--;
		for(int i = 0; i < posti; i++) {
			PcInUse++;
			computers[i] = 1;
		}
		System.out.println("Laboratorio occupato da un professore");
	}
	
	public synchronized void setPcTesista(int i){
		qTesisti++;
		//se il PcTesisti è occupato oppure c'è almeno un prof in coda devo aspettare
		while(computers[i] == 1 || qProfessori > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		qTesisti--;
		computers[i] = 1;
		PcInUse++;
		System.out.println("Occupato computer tesisti: " + i);
	}
	
	public synchronized int setPcStudent(){
		int i;
		//se tutti i pc sono occupati oppure c'è almeno un prof in coda oppure (almeno un
		//tesista in coda e il PcTesisti è libero) devo aspettare
		while(PcInUse == posti || qProfessori > 0 || (qTesisti>0 && computers[PcTesisti]==0))
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		for(i = 0; i < posti; i++) {
			if(computers[i] == 0) {
				PcInUse++;
				computers[i] = 1;
				break;
			}
		}
		System.out.println("Occupato computer " + i + " da studente");
		return i;
	}
	
	public synchronized void freeLab(){
		for(int i = 0; i < posti; i++) {
			computers[i] = 0;
			PcInUse--;
		}
		this.notifyAll();
		//System.out.println("Liberato tutto il laboratorio");
	}
	
	public synchronized void freePc(int i){
		computers[i] = 0;
		PcInUse--;
		this.notifyAll();
		System.out.println("Liberato computer " + i);
	}
}