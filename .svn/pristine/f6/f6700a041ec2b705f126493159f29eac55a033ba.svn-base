package Controller_events;

import Controller.Controller_Interface;
import Model.Giocatore;

public class askIfSecondaScommessa extends HorseFeverEventController {

	boolean enough_soldi;
	Giocatore player;
	
	public askIfSecondaScommessa(Controller_Interface cont,boolean enough_money,Giocatore gioc) {
		super(askIfSecondaScommessa.class);
		super.controller_Interface=cont;
		enough_soldi=enough_money;
		player=gioc;
	}
	
	public boolean isEnough_soldi() {
		return enough_soldi;
	}
	public Giocatore get_giocatore() {
		return this.player;
	}

}
