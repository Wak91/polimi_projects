package Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * mazzo contiene un array di 'Carta' , il costruttore riceve in ingresso il tipo di mazzo che
 * si vuole costruire ( Azione, Movimento , ... ) e il file di testo dove si trovano le carte per 
 * costruire il mazzo.  
 */

public class Mazzo 

 {	
	private final ArrayList <Carta> deck;  // il reference ad un deck non cambia mai! 
	private int index; // indice che serve per capire dove sono arrivato a pescare , 0=inizio deck.size=fine
	
	/**
	 * costruisce un mazzo vuoto
	 */
	public Mazzo()
	    {
		deck = new ArrayList <Carta>();
		this.index = 0;
		}
	
	/**
	 * visualizza tutte le carte in un mazzo richiamando il metodo visualizza di carta
	 */
	public void show_mazzo(){
		for (Carta card : deck) {
		card.visualizza();
			
		}
	}
	
	/**
	 * Questo metodo aggiunge una carta al mazzo che lo chiama ( serve dato che creiamo i mazzi in modo statico )
	 * @param card : la carta da aggiungere e deck e l'array list di carte del mazzo che chiama il metodo 
	 */
	public void addCard(Carta card)
	{
	this.deck.add(card);	
	}
	
	
	/**
	 * questo metodo serve per pescare una carta da un oggetto mazzo istanziato 
	 * se sono arrivato alla fine del mazzo, ritorno la carta e mi riposiziono
	 * con index sulla prima.
	 * @return
	 */
	public Carta pesca()
	{
	int ind = this.index;
	if(ind==(deck.size()-1)){
		 this.index=0;
	}
	
	else
        { this.index++; } // mi posiziono sulla nuova carta 
	return deck.get(ind);	 // e ritorno quella vecchia!
	}
	
	
	/**
	 * Mischia un istanza di mazzo riportando l'index all'inizio
	 */
	public void mischia()
	{
	Collections.shuffle(this.deck); // metodo di collections che mischia il mazzo 
	index=0;
	}
}
