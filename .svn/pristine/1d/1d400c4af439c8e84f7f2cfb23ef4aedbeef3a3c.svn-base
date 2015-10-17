package Controller_events;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.Controller_Interface;
import Model.Giocatore;

public class showRimossiEvent extends HorseFeverEventController implements Serializable {

	ArrayList<Giocatore> rimossi;
	
	
	public showRimossiEvent(Controller_Interface cont,ArrayList<Giocatore> rimos) {
		super(showRimossiEvent.class);
		super.controller_Interface=cont;
		rimossi=rimos;
	}
	public ArrayList<Giocatore> getRimossi() {
		return rimossi;
	}

}
