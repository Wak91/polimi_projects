package Model;

import java.io.Serializable;

/**
 * Associa una quotazione ad un movimento, serve per fare in modo che ogni carta movimento
 * contenga una struttura che indichi per ognuna delle 6 quotazioni il numero di caselle per lo spostamento.
 * Questa classe viene utilizzata solo in fase di inizializzazione del gioco per creare le carte movimento.
 *
 */
public class Movimento implements Serializable {

	private final int quote; // Corrispondenze --> 0=1_2  | 1=1_3  | 2=1_4  | 3=1_5  | 4=1_6  | 5 = 1_7
	private final int step; // di quanto si muove
	
	public Movimento(int q , int s)
	{
	this.quote = q;
	this.step = s;
	}

	public int getQuote() {
		return quote;
	}

	public int getStep() {
		return step;
	}
	
	public String visualizza()
	{
		return "\nquotazione :  " + quote + "step :  " + step;
	}	
}
