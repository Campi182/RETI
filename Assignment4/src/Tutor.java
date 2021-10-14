import java.util.PriorityQueue;

public class Tutor{

	private Laboratorio lab;
	private PriorityQueue<User> q;
	
	public Tutor(Laboratorio lab) {
		this.lab = lab;
		q = new PriorityQueue<User>();
	}
	
	public void start() throws InterruptedException{
		User u;
		while(q.size()>0 ) {
			u = q.remove();
			if(u.getWho().equals("Professore")) {
				lab.setLabBusy();
				newUserThread(u);
			} else if (u.getWho().equals("Tesista")) {
				lab.setPc(u.getUsingPc());
				newUserThread(u);
			} else if (u.getWho().equals("Studente")) {
				u.assignPc(lab.setPc());
				newUserThread(u);
			}
		}
	}
	
	private void newUserThread(User u) {
		Thread ThreadUser = new Thread(u);
		ThreadUser.start();
	}
	
	public void InsertQueue(User u) {
		q.add(u);
	}
	
}
