package View;


import Controller_events.EventoCorsa;
import Controller_events.TruccaEvent;
import Controller_events.askIfSecondaScommessa;
import Controller_events.askScommessaEvent;
import Controller_events.getNomePlayerEvent;
import Controller_events.getNumPlayerEvent;
import Controller_events.showBadEndEvent;
import Controller_events.showClassificaEvent;
import Controller_events.showPriceEvent;
import Controller_events.showRimossiEvent;
import Controller_events.showWinnerEvent;


public interface User_interface {
	
	public void get_num_player(getNumPlayerEvent event);

	public void get_nome_player(getNomePlayerEvent evemt);
	
	public void show_panel_trucca( TruccaEvent event);

	public void show_corsa(EventoCorsa evento);
	
//---------------------------------GIRO SCOMMESSE--------------------------------->
	
	public void get_scommessa(askScommessaEvent event);
	
	public void ask_if_seconda_scommessa(askIfSecondaScommessa event);
	


//-----------------------------------TRUCCARE CORSA----------------------------------->
	
		
//-----------------------------------GESTIONE CORSA----------------------------------->
	


public void show_classifica(showClassificaEvent evt);





//----------------------------------GESTIONE FINALE----------------------------------->

	public void show_bad_end_message(showBadEndEvent evt);

	public void show_win_price(showPriceEvent evt);
	
	public void show_rimossi(showRimossiEvent evt) ;

	
	public void show_winner(showWinnerEvent evt);




	
	
}