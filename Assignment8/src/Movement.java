import java.time.LocalDate;

enum Causali {Bonifico, Accredito, Bolettino, F24, Pagobancomat}

public class Movement {

	//private LocalDate date;
	private Causali causale;
	private int date;
	
	public Movement(int date, Causali causale) {
		this.date = date;
		this.causale = causale;
	}
	
	public int getDate() {
		return date;
	}
	
	public Causali getCausale() {
		return causale;
	}
}
