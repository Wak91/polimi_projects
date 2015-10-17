package Model;

import java.io.Serializable;

/**
 * la classe quotazione associa una scuderia con la corrispondete quotazione ( index )
 * le quotazioni vengono fatte corrispondere a numeri interi
 * 0 = 1_2
 * 1 = 1_3
 * 2 = 1_4
 * [ ... ] 
 * il numero di classi quotazione � limitato a 6 ( una per ogni scuderia ) 
 * @author degrigis
 *
 */

public class Quotazione implements Serializable{
	
    private int index;
    private String colore;
	
    /**
     * costruttore di una quotazione, richiede in ingresso:
     * @param inde: quotazione associata alla scuderia 
     * @param color: colore della scuderia 
     */
	public Quotazione(int inde, String color)
	{
	this.index = inde;
	this.colore = color;
	}
	
	/**
	 * ritorna il colore dell'oggetto corrente 
	 * @return
	 */
	public String getColore()
	{
    return this.colore;
	}
	
	/**
	 * ritorna l'indice ( o quotazione ) dell'oggetto corrente 
	 * @return
	 */
	public int getIndex()
	{
	return this.index;
	}
	
	/**
	 * Utilizzato per cambiare la quotazione ad una scuderia 
	 * se l'indice passato e' < 0 metto la quotazione massima 
	 * se e' >5 metto la minima
	 * @param new_index: la nuova quotazione della scuderia 
	 */
	public void setIndex(int new_index)
	{
	if( new_index < 0)
	  {this.index = 0;}
	else
		if( new_index > 5)
		   {  this.index = 5; }
		else
	       {
	         this.index = new_index;	
	       }
	}
	
	public void visualizza()
	{
	System.out.println("quotaz: " + from_index_to_quot(index) + " Scuderia: " +colore);	
	}
	
	/**
	 * permette conversione da indice a quotazione espressa come 1:2 1:3 considerando che a 0 corrisponda 1:2 e a 1->1:3...
	 * @param index
	 * @return
	 */
	private String from_index_to_quot(int index){
		String quotazione;
		quotazione="1:"+(index+2);
		return quotazione;
	}
}
