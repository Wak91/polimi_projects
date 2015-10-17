package Model;

import java.io.Serializable;


/**
 * Rappresenta una scommessa fatta da un giocatore
 */

public class Scommessa implements Serializable {
	
	private final int denaro;
	private final String colore;
	private final String tipo; 
	
	
	public Scommessa(int money,String color,String type) {
		this.denaro=money;
		this.colore=color; 
		this.tipo=type;
	}

	/**
	 * Ritorna il denaro puntato
	 * @return
	 */
	public int getDenaro() {
		return denaro;
	}
	
	/**
	 * ritorna su che scuderia e� stata fatta la scommessa
	 * @return
	 */
	public String get_colore() {
		return this.colore;		
	}
	
	/**
	 * Ritorna se e� piazzata/vincente
	 * @return
	 */
	public String get_tipo() {
		return this.tipo;		
	}
	
    public void show_scommessa() {
        System.out.println("\n-->Denaro: "+this.denaro+"\n-->Colore: "+this.colore+"\n-->Tipo: "+this.tipo);
    }
	
	
	@Override
	public String toString() {
		return("\n-->Denaro: "+this.denaro+"\n-->Colore: "+this.colore+"\n-->Tipo: "+this.tipo);
	}
	

}
