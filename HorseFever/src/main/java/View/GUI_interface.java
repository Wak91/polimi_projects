package View;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import Gui_Events.AnswerSecondaScommessaEvent;
import Gui_Events.GoRaceEvent;
import Gui_Events.letsPayEvent;
import Gui_Events.sistemaEventUI;
import Model.Giocatore;


/**
 * I metodi per la creazione delle view vengono chiamati su questa classe che 
 * estende User_interface.
 * Questa si preoccupa di gestire l'istanziazione della view la prima volta
 * ( tramite l'utilizzo dei flag first_time_... ) e di fare invece una show su di esse le volte successive
 * mostrando i dati corretti senza dover "ritirarla su" ( molto pesante ) ogni volta
 * che viene chiamata.
 * 
 */
public  class  GUI_interface implements User_interface {
	
	
	private Scommesse_GUI view_second_bid;
	private Scommesse_GUI view_bid;
	private Race_view race;
	private Action_GUI ag;
	private JFrame frame;
	private int first_time_view=0;
	private int first_time_second_bet=0;
	private int first_time_view_trucca=0;
	private int first_time_view_gara=0;
	//---------ATTRIBUTI METODI-------> 
	private JLabel image_num_player;

	
	public GUI_interface() {
		
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	
	}
	
	public JFrame get_frame() {
		return this.frame;
	}
	

	/**
	 * Metodo che riceve percorso file  dimensioni e 
	 * restituisce una buffered Image ridimensionata che puo essere usata per creare ImageIcon
	 * @param nome_immagine
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 */
	public BufferedImage resizeImage(String nome_immagine,int width,int height) {  
	     try {
			
	    	File fileIcon = new File(nome_immagine);
			BufferedImage originalImage = ImageIO.read(fileIcon);
			BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  
	        Graphics2D g = resizedImage.createGraphics();  
	        g.drawImage(originalImage, 0, 0, width, height, null);  
	        g.dispose();  
	        return resizedImage;  
	     } catch (Exception e) {
				e.printStackTrace();
			}  
	     return null;
    }  
	
	/**
	 * Crea un JoptionPane che con 2 bottoni permette regolare numero giocatori
	 * restituisce numero giocatori
	 */
	public void get_num_player(getNumPlayerEvent  event) {
		Giocatore_GUI gio= new Giocatore_GUI(this , event);
		gio.show();
		
	}

	public void get_nome_player(getNomePlayerEvent event) {
		frame.dispose();
		Giocatore_name_GUI gioc= new Giocatore_name_GUI(this,event);
		gioc.show();
	}

	
	
	
	public void get_scommessa(askScommessaEvent event) {
		if (event.isSeconda_scommessa()==false){
			if(first_time_view==0)
			   {
				 frame.dispose();
				 first_time_view=1;  //ricordarsi di rimetterlo a 0!!!!
				 this.view_bid = new Scommesse_GUI(this);
			     view_bid.show(event);
			   }
			else 
			    {
				view_bid.show(event);} // sarebbe meglio indicare in questo evento se e' l'ultima volta che faro' vedere la schermata
			//provascome scomProvascommesse = new provascome(this,event); 
		}
		else{//Second bet
			if(first_time_second_bet==0)
			   { 
				frame.getContentPane().removeAll();
		    	frame.dispose();
				 first_time_second_bet=1;  //ricordarsi di rimetterlo a 0!!!!
				 this.view_second_bid = new Scommesse_GUI(this);
			     view_second_bid.show(event);
			   }
			else 
			    {
				view_second_bid.show(event);}   	
	    }
}
	
	public void ask_if_seconda_scommessa(askIfSecondaScommessa inEvent) {
		if (inEvent.isEnough_soldi()){
			int reply = JOptionPane.showConfirmDialog(null, inEvent.get_giocatore().get_nome()+" vuoi effettuare una seconda scommessa", "Seconda Scommessa", JOptionPane.YES_NO_OPTION);
			if (reply==JOptionPane.YES_OPTION){
				AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(true);
				inEvent.getController().ask_second_bet(outEvent);

			}
			else {
				AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(false);
				inEvent.getController().ask_second_bet(outEvent);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, inEvent.get_giocatore().get_nome() + " non hai abbastanza soldi per fare un'altra scommessa");
			AnswerSecondaScommessaEvent outEvent = new AnswerSecondaScommessaEvent(false);
			inEvent.getController().ask_second_bet(outEvent);
		}
		
		
	}
	public void show_panel_trucca(TruccaEvent event) {
		
		if(first_time_view_trucca==0) 
		   {
			first_time_view_trucca=1;
			frame.getContentPane().removeAll();
			frame.dispose();
			ag = new Action_GUI(this);
		   ag.show(event);
		   }
		else
		{ag.show(event);}
		}
	
	public void show_corsa(EventoCorsa evento) {
		
		if(first_time_view_gara==0) 
		   {
			first_time_view_gara=1;
			frame.getContentPane().removeAll();
			frame.dispose();
			race = new Race_view(this);
		   race.show(evento);
		   }
		else if(evento.getFase()==3)
		        {evento.getController().Gonext(new GoRaceEvent());}
		     else {race.show(evento);}
		
	}
		
	
	
	public void show_classifica(showClassificaEvent inEvent) {
		frame.getContentPane().removeAll();
		frame.dispose();
		
		Finale_GUI finale_GUI = new Finale_GUI(this);
		finale_GUI.show(inEvent);
		inEvent.getController().pay(new letsPayEvent());
	}
	
	public void show_win_price(showPriceEvent evt) {
		Finale_price_GUI finale_price_GUI =new Finale_price_GUI(this);
		finale_price_GUI.show(evt);
	}
	

	public void show_bad_end_message(showBadEndEvent evt) {
		JOptionPane.showMessageDialog(null, "Avete perso tutti");

	}



	public void show_rimossi(showRimossiEvent evt) {
		
		StringBuffer buf = new StringBuffer();
		for (Giocatore player : evt.getRimossi()) {
			buf.append(player.get_nome()+" hai perso\n");
		}
		String outString =buf.toString();
		if (!outString.equals("")) {
			JOptionPane.showMessageDialog(null,outString);
		}
		setup_GUI(evt);
	}
	
	private void setup_GUI(showRimossiEvent evt) {
		first_time_second_bet=0;
		first_time_view=0;
		first_time_view_gara=0;
		first_time_view_trucca=0;
		frame.getContentPane().removeAll();
		frame.dispose();
		evt.getController().sistema_partita(new sistemaEventUI());

	}
	public void show_winner(showWinnerEvent evt) {
		JOptionPane.showMessageDialog(null,"Il vincitore e' "+evt.getGiocatore());

		
	}
	

	


	



}
