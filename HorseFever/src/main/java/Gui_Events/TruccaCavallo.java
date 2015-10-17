package Gui_Events;

import Model.Carta_azione;


/**
 * Evento passato dalla gui al controller che gli indica il colore della scuderia da taroccare
 * e la carta azione giocata 
 * @author Fabio
 *
 */
public class TruccaCavallo extends HorseFeverEventGui  {

	private String colore;
	private Carta_azione carta_giocata;
	
	public TruccaCavallo(String colore , Carta_azione carta_giocata)
	{
		super(TruccaCavallo.class);
		this.colore = colore;
		this.carta_giocata = carta_giocata; 
	}
	
	
	public String getColore(){
		return this.colore;
	}
	
	public Carta_azione getCarta()
	{
		return carta_giocata;
	}
	
}
