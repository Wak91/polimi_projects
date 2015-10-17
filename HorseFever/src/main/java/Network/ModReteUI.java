package Network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
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
import Model.Giocatore;
import View.User_interface;

/**
 * Classe che implementa l'interfaccia UI per impacchettare gli eventi creati dal controller e spedirli 
 * verso i socket dei giocatori.
 * Dall'altra parte esiste un client in ascolto sul suo buffer in entrata.
 *
 */
public class ModReteUI implements User_interface {
	
	
	private ObjectOutputStream ooS;
	private OutputStream outS;

	public ModReteUI(){}

	public void get_num_player(getNumPlayerEvent event) {
	}

	public void get_nome_player(getNomePlayerEvent evemt) {
		
		try {
			for(Socket s: Server.elenco_connessioni){ System.out.println(""+s);}
			outS = 	Server.nextConnection().getOutputStream();
			ooS = new ObjectOutputStream(outS);
			ooS.writeObject(evemt);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		}
	
	public void get_scommessa(askScommessaEvent event) {
		try {
			outS = Server.getMap_gioc_socket().get(event.get_giocatore()).getOutputStream();
			ooS = new ObjectOutputStream(outS);
			ooS.writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	public void show_panel_trucca(TruccaEvent event) {
		try {
			outS = Server.getMap_gioc_socket().get(event.getGiocatore()).getOutputStream();
			ooS = new ObjectOutputStream(outS);
			ooS.writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}


	public void ask_if_seconda_scommessa(askIfSecondaScommessa event) {
		try {
			outS = Server.getMap_gioc_socket().get(event.get_giocatore()).getOutputStream();
			ooS = new ObjectOutputStream(outS);
			ooS.writeObject(event);
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}
	
	public void show_corsa(EventoCorsa evento) {
		try {
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet()) // per ogni connessione nell'elenco faccio vedere la gara
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evento);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void show_classifica(showClassificaEvent evt) {
		try {
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet())
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evt);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void show_win_price(showPriceEvent evt) {
		try {
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet()) 
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evt);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void show_rimossi(showRimossiEvent evt) {
		try { 
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet())
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evt);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void show_bad_end_message(showBadEndEvent evt) {
		
		try {
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet())  
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evt);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	public void show_winner(showWinnerEvent evt) {
		try {
			for(Giocatore gioc : Server.getMap_gioc_socket().keySet()) 
			   {
				outS = Server.getMap_gioc_socket().get(gioc).getOutputStream();
				ooS = new ObjectOutputStream(outS);
				ooS.writeObject(evt);
			   }
		} catch (IOException e) {
			e.printStackTrace();
		}

	}



}
