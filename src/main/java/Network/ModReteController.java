package Network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import Controller.Controller_Interface;
import Gui_Events.AnswerSecondaScommessaEvent;
import Gui_Events.GoRaceEvent;
import Gui_Events.HorseFeverEventGui;
import Gui_Events.TruccaCavallo;
import Gui_Events.letsPayEvent;
import Gui_Events.setNomeEvent;
import Gui_Events.setNumPlayerEvent;
import Gui_Events.setScommessaEvent;
import Gui_Events.sistemaEvent;
import Gui_Events.sistemaEventUI;

/**
 * l'MrC viene usato dalle view del client per chiamare le funzioni che interagiscono con il server
 * mandandogli gli eventi appropriati.
 *
 */
public class ModReteController implements Controller_Interface {

	private OutputStream os;  
	ObjectOutputStream oos; 
	private InetAddress serveraddress;
	private int portaserver;
	private Socket Connessione;
	private Client client;
		
	
	
	public ModReteController(Client client , InetAddress serveraddress , int serverport)
	{ 
	this.serveraddress = serveraddress;
	this.portaserver = serverport;
	this.client = client;
	}
	
	public void get_player_number() {
	}

	public void how_many_player(setNumPlayerEvent evt) {
	}

	private void ModreteControllerJob(HorseFeverEventGui event) {
	    //ora lui impacchetta e spedisce al server 
		//prima creo una connessione e mi prendo uno streamer per scrivere
		try {
			Connessione = new Socket(this.serveraddress,this.portaserver);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			os = Connessione.getOutputStream();
			oos = new ObjectOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			oos.writeObject(event); //scrivo l'evento sul socket e lo mando di la
		} catch (IOException e) {
			e.printStackTrace();
		}
		try{
		}catch(Exception e)
		{ e.printStackTrace();}
			
	   this.client.go();	

	}
	public void create_player(setNomeEvent evt) {
		
		ModreteControllerJob(evt);
	}

	public void create_bet(setScommessaEvent evt) {
	
		ModreteControllerJob(evt);
	}

	
	public void tarocca_cavallo(TruccaCavallo event) {
		ModreteControllerJob(event);
	}
	
	public void pay(letsPayEvent evt) {
		ModreteControllerJob(evt);

	}
	
	public void sistema(sistemaEvent evt) {
		ModreteControllerJob(evt);

	}

	public void ask_second_bet(AnswerSecondaScommessaEvent inEvent) {
		ModreteControllerJob(inEvent);
	}
	

	public void Gonext(GoRaceEvent gre) {
		
		ModreteControllerJob(gre);
		
	}
	
	public void create_second_bet(setScommessaEvent evt) {
		ModreteControllerJob(evt);

	}
	
	public void sistema_partita(sistemaEventUI inEvent) {
		ModreteControllerJob(inEvent);
	}
	
}
