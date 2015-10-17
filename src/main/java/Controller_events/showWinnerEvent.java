package Controller_events;

import java.io.Serializable;

import Controller.Controller_Interface;

public class showWinnerEvent extends HorseFeverEventController implements Serializable{
	
	String giocatore;

	public showWinnerEvent(Controller_Interface cont,String nomeVincitore) {
		super(showWinnerEvent.class);
		super.controller_Interface=cont;
		giocatore=nomeVincitore;
	}

	public String getGiocatore() {
		return giocatore;
	}
	

}
