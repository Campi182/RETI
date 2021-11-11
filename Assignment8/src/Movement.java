import java.time.LocalDate;

enum Causali {Bonifico, Accredito, Bolettino, F24, Pagobancomat}

public class Movement {

	private LocalDate date;
	private Causali causale;
	
	public Movement(LocalDate date, Causali causale) {
		this.date = date;
		this.causale = causale;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public Causali getCausale() {
		return causale;
	}
}
