import java.util.Date;

enum Causali {Bonifico, Accredito, Bolettino, F24, Pagobancomat}

public class Movement {

	private Date date;
	private Causali causale;
	
	public Movement(Date date, Causali causale) {
		this.date = date;
		this.causale = causale;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Causali getCausale() {
		return causale;
	}
}
