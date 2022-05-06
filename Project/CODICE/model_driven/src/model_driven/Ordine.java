package model_driven;


public class Ordine {
	@Override
	public String toString() {
		return "Ordine [partenza=" + partenza + ", arrivo=" + arrivo + ", quantita=" + quantita + "]";
	}


	private int partenza;
	private int arrivo;
	private int quantita;
	
	
	public Ordine (int partenza, int arrivo, int quantita)
	{
		this.partenza = partenza;
		this.arrivo = arrivo;
		this.quantita = quantita;
	}


	public int getPartenza() {
		return partenza;
	}


	public void setPartenza(char partenza) {
		this.partenza = partenza;
	}


	public int getArrivo() {
		return arrivo;
	}


	public void setArrivo(char arrivo) {
		this.arrivo = arrivo;
	}


	public int getQuantita() {
		return quantita;
	}


	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	
	

}

