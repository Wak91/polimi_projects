package Network;

import it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti.GameOption;
import it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti.Main;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
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
import Model.Giocatore;
import Model.Model_interface;

public class Server extends Thread{

	private ServerSocket Server;
	private static int num_player;
	private static int connessioni=0;
	public static ArrayList<Socket> elenco_connessioni;
	private static int current_connession=0;
	private Controller_Interface game_controller;
	private static HashMap<Giocatore, Socket> map_gioc_socket;
	private Model_interface model;  //non so quanto giusto ma mi serve per associare ai socket i rispettivi giocatori
	private boolean need_refresh_map_gioc_socket=true;
	private boolean sistema_conn=false;
	
	private int contclick=0;
	
	public Server(Controller_Interface game_controller,Model_interface modello) throws Exception {
		model=modello;
		try {
			Server =  new ServerSocket(4560);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Errore configurazione server probabilmente sta girando un altro Horse fever in giro");
			System.exit(1);
		}
		JOptionPane.showMessageDialog(null, "Server avviato correttamente premi ok e aspetta che si connettino i giocatori");

		this.game_controller = game_controller;
		elenco_connessioni = new ArrayList<Socket>();
		num_player=GameOption.numplayer; 
		this.start();
	}
	
	
	
	public static HashMap<Giocatore, Socket> getMap_gioc_socket() {
		return map_gioc_socket;
	}



	public static Socket nextConnection()
	{ 
		int old_connession= current_connession;
		if(current_connession>=num_player-1)
			{current_connession=0;}
		else
		    {current_connession++;}
		return elenco_connessioni.get(old_connession);
	}
	
	public static void remove_a_player()
	{ num_player--;}
	
	public static ArrayList<Socket> getConnesioni()
	{
		return elenco_connessioni;
	}
	
	
	public static Socket getCurrentConnession()
	{
		return elenco_connessioni.get(current_connession);
	}
	
	public void run() {
		while (connessioni<num_player) {
			try {
				Socket client = Server.accept();
				System.out.println("Connessione accettata da: "
						+ client.getInetAddress());
				elenco_connessioni.add(client);
				connessioni++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Main.start_online(new setNumPlayerEvent(num_player));
		while (true) {
			
			try {
				
				Socket client2 = Server.accept(); //quando ricevo qualcosa
				InputStream sIN = client2.getInputStream(); //apro il canale
				ObjectInputStream ois = new ObjectInputStream(sIN); //per l'oggetto
				HorseFeverEventGui EventArrived = (HorseFeverEventGui)ois.readObject(); //leggo l'oggetto e faccio un cast verso un evento generico della gui
				//ora devo analizzare di che tipo di evento si tratta
				if(EventArrived.getClass() == setNomeEvent.class) // se e' un evento di tipo nome
				   { 
					setNomeEvent sNE = (setNomeEvent)EventArrived; //allora faccio il cast verso di lui
					game_controller.create_player(sNE); //e chiamola funzione di creazione giocatore sul controller del gioco!
				   }
				//if necessario per linkare i giocatori a una connessione viene eseguito solo quando need_refresh e 
				//settato a true il metodo successivo lo setta a false per non entrarci la volta successiva inutilmente
				if (need_refresh_map_gioc_socket) {
					map_gioc_socket=new HashMap<Giocatore, Socket>();
					int i=0;
					for (Giocatore giocatore : model.get_giocatori()) {
						map_gioc_socket.put(giocatore, elenco_connessioni.get(i));
						i++;
					
					}
				}
				
				if(EventArrived.getClass() == setScommessaEvent.class) // se e' un evento di tipo nome
				   { 
					
					need_refresh_map_gioc_socket=false;
					setScommessaEvent sNE = (setScommessaEvent)EventArrived; //allora faccio il cast verso di lui
					if (sNE.isSeconda_scommessa()) {//devo chiamare 2 metodi diversi del controller a seconda se è o meno seconda scommessa
						game_controller.create_second_bet(sNE);
					}
					else {
						game_controller.create_bet(sNE); //e chiamola funzione di creazione giocatore sul controller del gioco!

					}
				   }
				
				if(EventArrived.getClass() == TruccaCavallo.class) // se e' un evento di tipo nome
				   { 
					TruccaCavallo sNE = (TruccaCavallo)EventArrived; //allora faccio il cast verso di lui
					game_controller.tarocca_cavallo(sNE); //e chiamola funzione di creazione giocatore sul controller del gioco!
				   }
				else if (EventArrived.getClass() == AnswerSecondaScommessaEvent.class) {
					AnswerSecondaScommessaEvent Asb = (AnswerSecondaScommessaEvent)EventArrived;
					game_controller.ask_second_bet(Asb);
				}
				else if ( (  ( EventArrived.getClass() == GoRaceEvent.class ) )) {
					    
					    contclick++;
					    if(contclick==num_player)
					      {GoRaceEvent GrE = (GoRaceEvent)EventArrived;
						    game_controller.Gonext(GrE); 
						   contclick=0;  
					      }
					    else { continue;}
					    
				}
					    
				
			    else if ( (  ( EventArrived.getClass() == letsPayEvent.class ) )) {
			    	  contclick++;
					    if(contclick==num_player)
					      { letsPayEvent lPe = (letsPayEvent)EventArrived;
						    game_controller.pay(lPe); 
						   contclick=0;  
					      }
					    else { continue;}
			    
			   
			    
			    }
				
			    else if ( (  ( EventArrived.getClass() == sistemaEvent.class ) )) {
				    
			    	contclick++;
				    if(contclick==num_player)
				      { sistemaEvent sE = (sistemaEvent)EventArrived;
					    game_controller.sistema(sE); 
					   contclick=0;  
				      }
				    else { continue;}    
			    }
				
				
				
               else if ( (  ( EventArrived.getClass() == sistemaEventUI.class ) )) 
               
               {    contclick++;
				    if(contclick==num_player)
				      {sistemaEventUI sEUI = (sistemaEventUI)EventArrived;
					   game_controller.sistema_partita(sEUI); 
					   contclick=0;  
					   sistema_conn=true;
				      }
				    else { continue;}     	
			  }
				if (sistema_conn){
					ArrayList<Giocatore> rimossiArrayList=new ArrayList<Giocatore>();
					for (Giocatore player : map_gioc_socket.keySet()) {
						if (model.get_giocatori().contains(player)==false){//giocatore eliminato
							rimossiArrayList.add(player);
						}
					}
					for (Giocatore gioc : rimossiArrayList) {
						map_gioc_socket.remove(gioc);
						num_player--;
					}
				}
				sistema_conn=false;
			}
		
			    
			
				catch (Exception e) {
				
					e.printStackTrace();
				}
				
				
			}
	}
}
