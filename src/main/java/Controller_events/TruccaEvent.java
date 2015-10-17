package Controller_events;

import java.io.Serializable;

import Controller.Controller_Interface;
import Model.Giocatore;
import Model.Lavagna;

public class TruccaEvent  extends HorseFeverEventController implements Serializable
{
	private Giocatore player;
	private Lavagna lavagna;
			
	public TruccaEvent (Controller_Interface cont , Giocatore player,Lavagna lav) {
		super(TruccaEvent.class);
		super.controller_Interface=cont;
	    this.player = player;
	    lavagna=lav;
	    

}

	
	public Lavagna getLavagna() {
		return lavagna;
	}


	public Giocatore getGiocatore(){
		return this.player;
	}
			
}
