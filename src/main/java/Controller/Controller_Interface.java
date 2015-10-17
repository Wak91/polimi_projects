package Controller;


import java.io.Serializable;

import Gui_Events.GoRaceEvent;
import Gui_Events.TruccaCavallo;
import Gui_Events.AnswerSecondaScommessaEvent;
import Gui_Events.letsPayEvent;
import Gui_Events.setNomeEvent;
import Gui_Events.setNumPlayerEvent;
import Gui_Events.setScommessaEvent;
import Gui_Events.sistemaEvent;
import Gui_Events.sistemaEventUI;

/**
 * Intrerfaccia implementata dal controller del gioco.
 */
public interface Controller_Interface  extends Serializable {
	
	public void get_player_number();
	
	public void  how_many_player(setNumPlayerEvent evt) ;

	public void create_player(setNomeEvent evt);
	
	public void  create_bet(setScommessaEvent evt);
	
	public void tarocca_cavallo(TruccaCavallo event);
	
	public void ask_second_bet(AnswerSecondaScommessaEvent inEvent);
	
	 public void  create_second_bet(setScommessaEvent evt);
	 
	 public void pay(letsPayEvent evt) ;
	 
	 public void sistema(sistemaEvent evt);
	 
	 public void sistema_partita(sistemaEventUI inEvent);

	public void Gonext(GoRaceEvent gre);
}
