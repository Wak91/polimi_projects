package View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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
import Gui_Events.GoRaceEvent;
import Gui_Events.AnswerSecondaScommessaEvent;
import Gui_Events.TruccaCavallo;
import Gui_Events.letsPayEvent;
import Gui_Events.setNomeEvent;
import Gui_Events.setNumPlayerEvent;
import Gui_Events.setScommessaEvent;
import Gui_Events.sistemaEvent;
import Gui_Events.sistemaEventUI;
import Model.Carta_azione;
import Model.Cavallo;
import Model.Giocatore;
import Model.Lavagna;
import Model.Movimento;

public class Text_interface implements User_interface {
	
	
	HashMap<Integer, String> mappa_interi_colori; //necessaria per tradurre gli interi che leggo in stringhe colori

	
	public Text_interface()
	{
	init_mappa_interi_colori();	//inizializza hash da interi a colori
	System.out.println("-------BENVENUTO IN HORSE FEVER [VERSIONE TESTUALE]----------");		
	}
	
	/**
	 * funzione che inizializza hash map che trasforma gli interi che vengono acquisiti da console in colori dei cavalli  
	 */
	private void init_mappa_interi_colori() {
		mappa_interi_colori= new HashMap<Integer, String>();
		mappa_interi_colori.put(0, "nero");
		mappa_interi_colori.put(1, "blu");
		mappa_interi_colori.put(2, "verde");
		mappa_interi_colori.put(3, "rosso");
		mappa_interi_colori.put(4, "giallo");
		mappa_interi_colori.put(5, "bianco");		
	}
		
	/**
	 * Funzione per gestire input utente riceve un range bottom e top e controlla che l'input 
	 * sia compreso in esso message indica il messaggio che si vuole sia mostrato a schermo la prima volta
	 * ho deciso di usare interi che rendono il gioco piu immediato rispetto al digitare stringhe
	 * @param bottom  limite inferiore
	 * @param top		limite superiore
	 * @param message	messagio mostrato la prima volta
	 * @return	l'intero nel range inserito dall'utente
	 */
	public static int input_check(int bottom,int top,String message){
		boolean ok=false;
		int input=0;
		Scanner scanner;
		if (!message.equals("")){
			System.out.println(message);
		}
		while (!ok){
			scanner = new Scanner(System.in);
			try {
				 input = Integer.parseInt(scanner.nextLine());
				if (input<bottom || input>top) {
					System.out.println("Inserisci un numero da:"+bottom+" a "+top);
				}
				else {
					ok=true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Inserisci un numero");
			}
		}
		return input;
	}
	
	
	public void get_num_player(getNumPlayerEvent eventCont) {
		int num;
		System.out.println("Quanti giocatori?\n");
		num=input_check(2, 6, "");
		setNumPlayerEvent eventGui = new setNumPlayerEvent(num);
		eventCont.getController().how_many_player(eventGui);
		
	}
	
	public void get_nome_player(getNomePlayerEvent event) {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println("Nome del giocatore \n");
    	String nome =  scanner.nextLine();
    	setNomeEvent nameEvent = new setNomeEvent(nome);
    	event.getController().create_player(nameEvent);		
	}
	
	public void get_scommessa(askScommessaEvent event) {
		Giocatore player=event.get_giocatore();
		System.out.println("\n-------GIRO DI SCOMMESSE----------");
		
		if (event.isErrore_cavallo()){
			System.out.println("\nIl cavallo su cui hai scommesso non ha piu segnalini\n");
		}
		if (event.isErrore_tipo()){
			System.out.println("Non puoi scommettere Piazzato se prima hai scommesso vincente e viceversa");
		}
		
		show_action_card(player);
		
		String colore = get_cavallo_scommessa(event.getLavagna());
		
		int soldi= get_soldi_scommessa(player);
		
		String tipo_scommessa=get_tipo_scommessa() ;

		if(event.isSeconda_scommessa()){
			setScommessaEvent evt = new setScommessaEvent(soldi, tipo_scommessa, colore,true);

			event.getController().create_second_bet(evt);
		}
		else{
			setScommessaEvent evt = new setScommessaEvent(soldi, tipo_scommessa, colore,false);

			event.getController().create_bet(evt);

		}
	}

	
	
	public void show_panel_trucca(TruccaEvent event) {
		
		System.out.println("\n-------TRUCCARE LA CORSA----------");
		System.out.println(event.getGiocatore().get_nome()+" hai queste carte azione:");
		int i= 0;
		StringBuffer message = new StringBuffer();
		message.append("Inserisci numero relativo a carta:");
		for (Carta_azione card : event.getGiocatore().get_action_cards()) {	//mostro le carte come "[indice array] : nome" cosi sotto riesco a ricavare la carta dall'indice
			message.append("\n["+i+"]"+card.getNome());				//digitato dall'utente
			i++;
		}
		System.out.println(message);
		int index = input_check(0, 1, "");
		System.out.println("Su quale cavallo vuoi giocare questa carta?");
		show_interi_colori();
		int int_colore =input_check(0, 5, "");
		String color =  mappa_interi_colori.get(int_colore);
		TruccaCavallo tevent = new TruccaCavallo(color, event.getGiocatore().get_action_cards().get(index));
		event.getController().tarocca_cavallo(tevent);
		
	}

	
	public void ask_if_seconda_scommessa(askIfSecondaScommessa inEvent){
		if (inEvent.isEnough_soldi()){
			System.out.println(inEvent.get_giocatore().get_nome()+" vuoi effettuare seconda scommessa?\n[0]No\n[1]Si");
			int risp = input_check(0, 1, "");
			if (risp==1){
				AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(true);
				inEvent.getController().ask_second_bet(outEvent);
			}
			else {
				AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(false);
				inEvent.getController().ask_second_bet(outEvent);
			}
		}
		else {
			System.out.println(inEvent.get_giocatore().get_nome()+" non hai abbastanza soldi per scommettere");
			AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(false);
			inEvent.getController().ask_second_bet(outEvent);
		}
	}

	
//----------------------------------------METODI INTERAZIONE SCOMMESSA---------------------------------------->
	private void show_action_card(Giocatore player) {
		System.out.println("Giocatore "+ player.get_nome() +" hai pescato: ");
		for (Carta_azione carta : player.get_action_cards()) {
			System.out.println(carta.get_string_card());
		}
	}
	
	public boolean make_seconda_scommessa(Giocatore player) {
		System.out.println(player.get_nome()+" vuoi fare una seconda scommessa?\n[0]Si\n[1]No");
		int risp = input_check(0, 1, "");
		if (risp==0)
			{ return true; }
		else 
			{ return false; }
		
	}
	
	private void show_interi_colori(){
		for (Integer key : mappa_interi_colori.keySet()) {
			System.out.println("["+key+"] "+mappa_interi_colori.get(key));
		}
	}
	

	private String get_cavallo_scommessa(Lavagna lavagna) {
		
		System.out.println("\n-------GIRO DI SCOMMESSE----------");
		
		System.out.println("Le Quotazioni attuali sono:");
		lavagna.visualizzaLavagna();
		System.out.println("Inserisci il numero del cavallo su cui vuoi scommettere");
		show_interi_colori();
		int int_colore =input_check(0, 5, "");
		return mappa_interi_colori.get(int_colore);
	}
	
	
	
	private  int get_soldi_scommessa(Giocatore player) {
		boolean exit=false;
		int number_cash;	//numero banconote
		int type_cash;		//tipo banconote 50,100...
		int den=0;			//denaro scommesso
		while (!exit){	
			type_cash = input_check(0,4,"Ora hai:"+(player.get_money()-den)+" soldi "+"\nInserisci banconota da inserire:\n[0]50\n[1]100\n[2]1000\n[3]FINE\n[4]AZZERA SOLDI");
			switch (type_cash) {
			case 0:
				 number_cash= input_check(0, (player.get_money()-den)/50, "Inserisci quante banconote da 50 vuoi");
					 den+=number_cash*50;				
				break;
	
			case 1:
				number_cash= input_check(0, (player.get_money()-den)/100, "Inserisci quante banconote da 100 vuoi");
					 den+=number_cash*100;				
				break;
			case 2:
				number_cash= input_check(0, (player.get_money()-den)/1000, "Inserisci quante banconote da 1000 vuoi");
					den+=number_cash*1000;
					break;
			case 3:
				if(den>=player.get_min_bet())
				   { exit=true; }
				else {
					System.out.println("Devi Scommettere almeno "+player.get_min_bet());
				}
				break;
			case 4:
				den=0;
				System.out.println("Soldi scommessa azzerati");
				break;
				
			default:break;
			}	
			
		}		
		return den;	
	}
	/**
	 * gestisce il tipo di scommessa scelto utente
	 * @return ritorna alternativamente "Vincente" o "Piazzata" a seconda della scelta
	 */
	private String get_tipo_scommessa() {

		int tipo = input_check(0, 1, "Inserisci tipo scommessa:\n[0]Vincente\n[1]Piazzata");
		String tipo_scommessa;
		if (tipo==0)
			{ tipo_scommessa="Vincente"; }
		else
			{ tipo_scommessa= "Piazzata"; }
		return tipo_scommessa;
	}
	
	
//<------------------------------TRUCCARE CORSA---------------------------------------->
	
	

	
	public void show_corsa(EventoCorsa event) {
		
		if(event.getFase()==2) // se sono in fase running normale mostro la carta movimento pescata 
		  {
			System.out.println("Carta movimento pescata ");

			   {
				   for(Movimento m : event.getCartaM().get_movimenti())
				System.out.println(m.visualizza());
			   }
		  }
		if(event.getFase()==1 || event.getFase()==3)
		{System.out.println("2 cavalli hanno sprintato:");}
		
		for(Cavallo horse : event.Getpedine())
		   {
			System.out.println("cavallo "+horse.get_color()+" in posizione "+horse.get_posizione()+"\n");
		   }
		
		System.out.print("--TABELLONE CORSA--\n\n\n");
		for(Cavallo horse : event.Getpedine())
		   {
			System.out.print(""+horse.get_color()+"  )\n");
			for(int i=1;i<=event.getLunghezza()-1;i++)
				{ System.out.print("- "); }
			System.out.print("F\n");
			
			if(horse.get_posizione()<=event.getLunghezza())
			  {
			
			   for(int i=1;i<horse.get_posizione();i++)
			       { System.out.print("# "); }
			   System.out.print("X ");
			   for(int i=horse.get_posizione()+1;i<=event.getLunghezza();i++)
				   { System.out.print("# "); }
			   System.out.print("\n");
			   for(int i=1;i<=event.getLunghezza()-1;i++)
				   { System.out.print("- "); }
			   System.out.print("F ");
			  }
			else
				{				   
				for(int i=1;i<=event.getLunghezza();i++)
					{ System.out.print("# ");  }
				
			    for(int i=event.getLunghezza()+1;i<horse.get_posizione();i++)
			    	{ System.out.print("# "); }
			    System.out.print("X");
			    }
			System.out.print("\n");
		   }
		GoRaceEvent gre = new GoRaceEvent();
		System.out.println("Premi invio");
		//Scanner in  = new Scanner(System.in);
		//in.nextLine();
		event.getController().Gonext(gre);
		}

	public void show_classifica(showClassificaEvent inEvent) {
		
		int posizione=1;
		System.out.println("<--CLASSIFICA-->\n");
		for(String s : inEvent.getClassifica())
		   {
			
			System.out.println("" + posizione +" )" + s);
			posizione++;
		   }
		inEvent.getController().pay(new letsPayEvent());
	}

	public void show_win_price(showPriceEvent inEvent) {
		HashMap<Giocatore, ArrayList<Integer>> mappa= inEvent.getPriceHashMap();
		for (Giocatore gioc : mappa.keySet()) {
			System.out.println("Giocatore "+gioc.get_nome()+"hai vinto "+mappa.get(gioc).get(0)+" e "+mappa.get(gioc).get(1)+" pv dalla scommessa");
			System.out.println("e "+mappa.get(gioc).get(2)+" soldi scuderia");
			
		}
		inEvent.getController().sistema(new sistemaEvent());
	}
	
	
	

	
	public void show_winner(showWinnerEvent inEvent){
		System.out.println("Il vincitore è: "+inEvent.getGiocatore());
	}

	public void show_bad_end_message(showBadEndEvent evt) {
		System.out.println("Avete perso tutti assieme");
		
	}


	
	

	public void show_rimossi(showRimossiEvent evt) {
		if (!evt.getRimossi().isEmpty()){
			for (Giocatore player : evt.getRimossi()) {
				System.out.println(player.get_nome()+" Hai perso");
			}
		}
		evt.getController().sistema_partita(new sistemaEventUI());
		
	}

}

