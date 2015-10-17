package View;


import Model.Carta_azione;
import Model.Carta_movimento;
import Model.Cavallo;
import Model.Giocatore;
import Model.Lavagna;
import Model.Scommessa;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JButton;
import Controller_events.EventoCorsa;
import Gui_Events.GoRaceEvent;

public class Race_view {

	private JFrame frame;
	private final GUI_interface gui;
	private EventoCorsa event;
	private ArrayList <Cavallo> elenco_cavalli_temp;
	private ArrayList <Giocatore> elenco_player_temp;
	private boolean do_paint_player; // serve per capire se show deve aggiornare le immagini dei giocatori o no...
	private Carta_movimento card_m_temp;
	private JLabel JLmovpescato;
	
	/**
	 * questo attributo serve per capire se la corsa e' appena cominciata , se lo
	 * e' non sposto i cavalli  vengono quindi visualizzati nella loro posizione originaria, ed inoltre
	 * scrivo le quotazioni delle scuderie una volta per tutte. 
	 * ( cerco in pratica di togliere del lavoro alle successive show ) 
	 */
	private boolean race_just_start=true;
	
	private ArrayList <JLabel> segnalini_cavalli;
	private JLabel jlqnero;
	private JLabel jlqblu;
	private JLabel jlqrosso;
	private JLabel jlqgiallo;
	private JLabel jlqverde;
	private JLabel jlqbianco;
	
	private JLabel color1;
	private JLabel color2;

	private ArrayList <JButton> immagini_players;
	private JButton JBplayer1; 
	private JButton JBplayer2;
	private JButton JBplayer3;
	private JButton JBplayer4;
	private JButton JBplayer5;
	private JButton JBplayer6;
	
	
	private ArrayList <JLabel> nomi_giocatori;
	private JLabel JLplayer1;
	private JLabel JLplayer2;
	private JLabel JLplayer3;
	private JLabel JLplayer4;
	private JLabel JLplayer5;
	private JLabel JLplayer6;
	
	private JButton JBpescamov;
	private boolean first_click;
	
	
	/**
	 * Create the application.
	 */
	public Race_view(final GUI_interface gui) {
		
		this.gui = gui;
		frame = gui.get_frame();
		frame.setBounds(0, 0, 950, 740);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setTitle("GARA");
		
		segnalini_cavalli = new ArrayList<JLabel>();;
		immagini_players = new ArrayList <JButton>();
		nomi_giocatori = new ArrayList <JLabel>();
		
		first_click=true;
		do_paint_player=true;
		JPanel panel = new JPanel();//nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
		panel.setBounds(0, 0, 942, 706);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel() { //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
		
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui.resizeImage("images/tabellonefever.jpg", 825, 420));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		panel_1.setBounds(117, 61, 825, 420);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel horsenero = new JLabel();
		horsenero.setBounds(5, 28, 35, 38);
		horsenero.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqbla.jpg", 35, 38)));
		panel_1.add(horsenero);
		
		JLabel horseblu = new JLabel("New label");
		horseblu.setBounds(5, 99, 35, 38);
		horseblu.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqblu.jpg", 35, 38)));
		panel_1.add(horseblu);
		
		JLabel horseverde = new JLabel("New label");
		horseverde.setBounds(5, 171, 35, 38);
		horseverde.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqgree.jpg", 35, 38)));
		panel_1.add(horseverde);
		
		JLabel horserosso = new JLabel("New label");
		horserosso.setBounds(5, 237, 35, 38);
		horserosso.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqred.jpg", 35, 38)));
		panel_1.add(horserosso);
		
		JLabel horsegiallo = new JLabel("New label");
		horsegiallo.setBounds(5, 300, 35, 38);
		horsegiallo.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqye.jpg", 35, 38)));
		panel_1.add(horsegiallo);
		
		JLabel horsebianco = new JLabel("New label");
		horsebianco.setBounds(5, 355, 35, 38);
		horsebianco.setIcon(new ImageIcon(gui.resizeImage("immagini_segnalini_quotazione/sqwh.jpg", 35, 38)));
		panel_1.add(horsebianco);
		
		segnalini_cavalli.add(horsenero);
		segnalini_cavalli.add(horseblu);
		segnalini_cavalli.add(horseverde);
		segnalini_cavalli.add(horserosso);
		segnalini_cavalli.add(horsegiallo);
		segnalini_cavalli.add(horsebianco);
		
		JPanel panel_2 = new JPanel(){ //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
		
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon gcarpet = new ImageIcon(gui.resizeImage("images/gcarpet.jpg", 942, 227));
				Image gcarp = gcarpet.getImage();
				graphics.drawImage(gcarp, 0, 0, null);
			}
		};
		panel_2.setBounds(0, 480, 942, 226);
		panel.add(panel_2);
		panel_2.setLayout(null);
		
		ImageIcon retrocard = new ImageIcon(gui.resizeImage("immagini_carte_movimento/retro.jpg", 114, 204));
		JBpescamov = new JButton(retrocard);
		JBpescamov.setBounds(10, 11, 114, 204);
		JBpescamov.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			    frame.setEnabled(false);
				event.getController().Gonext(new GoRaceEvent());
				
			}
		});
		panel_2.add(JBpescamov);
		
		JLmovpescato = new JLabel();
		JLmovpescato.setHorizontalAlignment(SwingConstants.CENTER);
		JLmovpescato.setBounds(134, 11, 114, 204);
	
		panel_2.add(JLmovpescato);
		
		
		
		JLplayer1 = new JLabel("");
		JLplayer1.setBounds(368, 11, 46, 14);
		nomi_giocatori.add(JLplayer1);
		panel_2.add(JLplayer1);
		
		
		JLplayer2 = new JLabel("");
		JLplayer2.setBounds(468, 11, 46, 14);
		nomi_giocatori.add(JLplayer2);
		panel_2.add(JLplayer2);
		
		JLplayer3 = new JLabel("");
		JLplayer3.setBounds(566, 11, 46, 14);
		nomi_giocatori.add(JLplayer3);
		panel_2.add(JLplayer3);
		
		JLplayer4 = new JLabel("");
		JLplayer4.setBounds(667, 11, 46, 14);
		nomi_giocatori.add(JLplayer4);
		panel_2.add(JLplayer4);
		
		JLplayer5 = new JLabel("");
		JLplayer5.setBounds(765, 11, 46, 14);
		nomi_giocatori.add(JLplayer5);
		panel_2.add(JLplayer5);
		
		JLplayer6 = new JLabel("");
		JLplayer6.setBounds(864, 11, 46, 14);
		nomi_giocatori.add(JLplayer6);
		panel_2.add(JLplayer6);
		
		JBplayer1 = new JButton();
		JBplayer1.setBounds(348, 36, 89, 179);
		JBplayer1.setActionCommand("JBplayer1");
		panel_2.add(JBplayer1);
		
		JBplayer2 = new JButton();
		JBplayer2.setBounds(447, 36, 89, 179);
		JBplayer2.setActionCommand("JBplayer2");
		panel_2.add(JBplayer2);
		
		JBplayer3 = new JButton();
		JBplayer3.setBounds(546, 36, 89, 179);
		JBplayer3.setActionCommand("JBplayer3");
		panel_2.add(JBplayer3);
	
		JBplayer4 = new JButton();
		JBplayer4.setBounds(645, 36, 89, 179);
		JBplayer4.setActionCommand("JBplayer4");
		panel_2.add(JBplayer4);
		
		JBplayer5 = new JButton();
		JBplayer5.setBounds(744, 36, 89, 179);
		JBplayer5.setActionCommand("JBplayer5");
		panel_2.add(JBplayer5);
		
		JBplayer6 = new JButton();
		JBplayer6.setBounds(843, 36, 89, 179);
		JBplayer6.setActionCommand("JBplayer6");
		panel_2.add(JBplayer6);
		immagini_players.add(JBplayer1);
		immagini_players.add(JBplayer2);
		immagini_players.add(JBplayer3);
		immagini_players.add(JBplayer4);
		immagini_players.add(JBplayer5);
		immagini_players.add(JBplayer6);
		
		JLabel lblSprintano = new JLabel("Sprintano");
		lblSprintano.setHorizontalAlignment(SwingConstants.CENTER);
		lblSprintano.setBackground(Color.ORANGE);
		lblSprintano.setFont(new Font("Monotype Corsiva", Font.BOLD | Font.ITALIC, 18));
		lblSprintano.setBounds(249, 11, 89, 52);
		panel_2.add(lblSprintano);
		
		color1 = new JLabel("");
		color1.setHorizontalAlignment(SwingConstants.CENTER);
		color1.setFont(new Font("Monotype Corsiva", Font.BOLD | Font.ITALIC, 25));
		color1.setBounds(249, 80, 89, 52);
		panel_2.add(color1);
		
		color2 = new JLabel("");
		color2.setHorizontalAlignment(SwingConstants.CENTER);
		color2.setFont(new Font("Monotype Corsiva", Font.BOLD | Font.ITALIC, 25));
		color2.setBounds(249, 143, 89, 52);
		panel_2.add(color2);
		
		JPanel panel_3 = new JPanel(){ //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
		
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui.resizeImage("images/images.jpg", 116, 420));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		panel_3.setBounds(0, 61, 119, 419);
		panel.add(panel_3);
		panel_3.setLayout(null);
		
		JButton infobianco = new JButton("INFO>>");
		infobianco.setBackground(Color.WHITE);
		infobianco.setBounds(17, 369, 89, 23);
		infobianco.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				StringBuffer buf = new StringBuffer();
			buf.append("v--Carte attive sul cavallo--v\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(5).get_carte_azione())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
			    }
			buf.append("x--Carte eliminate dal cavallo--x\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(5).get_carte_eliminate())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
			    }
			String card_played=buf.toString();
			JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/bianco.jpg"));
				
			}
		}); 
		panel_3.add(infobianco);
		
		JButton infogiallo = new JButton("INFO>>");
		infogiallo.setToolTipText("provatooltip");
		infogiallo.setBackground(Color.YELLOW);
		infogiallo.setBounds(17, 312, 89, 23);
		infogiallo.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			StringBuffer buf = new StringBuffer();
			buf.append("v--Carte attive sul cavallo--v\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(4).get_carte_azione())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
			    }
			buf.append("x--Carte eliminate dal cavallo--x\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(4).get_carte_eliminate())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
			    }
			String card_played = buf.toString();
			JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/giallo.jpg"));
				
			}
		}); 
		panel_3.add(infogiallo);
		
		JButton inforosso = new JButton("INFO>>");
		inforosso.setBackground(Color.RED);
		inforosso.setBounds(17, 248, 89, 23);
		inforosso.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			StringBuffer buf = new StringBuffer();
			buf.append("v--Carte attive sul cavallo--v\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(3).get_carte_azione())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
			    }
			buf.append("x--Carte eliminate dal cavallo--x\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(3).get_carte_eliminate())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
			    }
			String card_played=buf.toString();
			JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/rosso.jpg"));		
			}
		}); 
		panel_3.add(inforosso);
		
		JButton infoverde = new JButton("INFO>>");
		infoverde.setBackground(Color.GREEN);
		infoverde.setBounds(17, 183, 89, 23);
		infoverde.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			StringBuffer buf= new StringBuffer();
			buf.append("v--Carte attive sul cavallo--v\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(2).get_carte_azione())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
			    }
			buf.append("x--Carte eliminate dal cavallo--x\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(2).get_carte_eliminate())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
			    }
			String card_played=buf.toString();
			JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/verde.jpg"));
				
			}
		}); 
		panel_3.add(infoverde);
		
		JButton infoblu = new JButton("INFO>>");
		infoblu.setBackground(Color.BLUE);
		infoblu.setBounds(17, 109, 89, 23);
		infoblu.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
			StringBuffer buf = new StringBuffer();
			buf.append("v--Carte attive sul cavallo--v\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(1).get_carte_azione())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
			    }
			buf.append("x--Carte eliminate dal cavallo--x\n"); 
			for(Carta_azione card : elenco_cavalli_temp.get(1).get_carte_eliminate())
			    {
				 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
			    }
			String card_played=buf.toString();
			JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/blu.jpg"));
				
			}
		}); 
		panel_3.add(infoblu);
		
		JButton infonero = new JButton("INFO>>");
		infonero.setForeground(Color.WHITE);
		infonero.setBackground(Color.BLACK);
		infonero.setBounds(17, 38, 89, 23);
		infonero.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					StringBuffer buf = new StringBuffer();
				buf.append("v--Carte attive sul cavallo--v\n"); 
				for(Carta_azione card : elenco_cavalli_temp.get(0).get_carte_azione())
				    {
					 buf.append(" -> " + card.getNome().toUpperCase()+card.get_descrizione().toLowerCase()+"\n");
				    }
				buf.append("x--Carte eliminate dal cavallo--x\n"); 
				for(Carta_azione card : elenco_cavalli_temp.get(0).get_carte_eliminate())
				    {
					 buf.append(" -> " + card.getNome().toUpperCase()+"\n");
				    }
				String card_played=buf.toString();
				JOptionPane.showMessageDialog(null, card_played, "Carte azione scuderia", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("immagini_carte_scuderia/nero.jpg"));
					
				}
			}); 
		panel_3.add(infonero);
		
		jlqnero = new JLabel("");
		jlqnero.setForeground(Color.WHITE);
		jlqnero.setHorizontalAlignment(SwingConstants.CENTER);
		jlqnero.setBounds(37, 11, 46, 14);
		
		panel_3.add(jlqnero);
		
		jlqblu = new JLabel("");
		jlqblu.setForeground(Color.WHITE);
		jlqblu.setHorizontalAlignment(SwingConstants.CENTER);
		jlqblu.setBounds(37, 87, 46, 14);
		
		panel_3.add(jlqblu);
		
		jlqverde = new JLabel("");
		jlqverde.setForeground(Color.WHITE);
		jlqverde.setHorizontalAlignment(SwingConstants.CENTER);
		jlqverde.setBounds(37, 158, 46, 14);
		
		panel_3.add(jlqverde);
		
		jlqrosso = new JLabel("");
		jlqrosso.setForeground(Color.WHITE);
		jlqrosso.setHorizontalAlignment(SwingConstants.CENTER);
		jlqrosso.setBounds(37, 223, 46, 14);
		
		panel_3.add(jlqrosso);
		
		jlqgiallo = new JLabel("");
		jlqgiallo.setForeground(Color.WHITE);
		jlqgiallo.setHorizontalAlignment(SwingConstants.CENTER);
		jlqgiallo.setBounds(37, 287, 46, 14);
	
		panel_3.add(jlqgiallo);
		
		jlqbianco = new JLabel("");
		jlqbianco.setForeground(Color.WHITE);
		jlqbianco.setHorizontalAlignment(SwingConstants.CENTER);
		jlqbianco.setBounds(37, 346, 46, 14);

		panel_3.add(jlqbianco);
		
		JPanel JPtitle = new JPanel(){ //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
		
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui.resizeImage("images/images.jpg", 942, 60));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		JPtitle.setBounds(0, 0, 942, 60);
		panel.add(JPtitle);
		JPtitle.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("GARA");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Monotype Corsiva", Font.ITALIC, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 942, 60);
		JPtitle.add(lblNewLabel);
		frame.setVisible(true);
	
	}

	
	public void show(final EventoCorsa event)
	
	{	first_click=true;
	    frame.setEnabled(true);
		Lavagna lavagna= event.getLavagna();
		this.event = event;
		this.elenco_cavalli_temp = event.Getpedine();
		this.card_m_temp = event.getCartaM();
		this.elenco_player_temp = event.GetElencoPlayer();
		//ImageIcon retrocard = new ImageIcon(gui.resizeImage("immagini_carte_movimento/retro.jpg", 114, 204));
		//JBpescamov.setIcon(retrocard);
		if(event.getColore1()!=null && event.getColore2()!=null)
		  {
			color1.setText(""+event.getColore1());
			color2.setText(""+event.getColore2());
		  }
		if(card_m_temp != null){
		JLmovpescato.setIcon(new ImageIcon(gui.resizeImage(event.getCartaM().getImmagine(), 114, 204)));
		}
		if(do_paint_player==true)  // per incollare le figurine dei giocatori una sola volta all'inizio
		  { paint_immagini_player(); 
		    do_paint_player=false;
		  }
		if(race_just_start==false)  
		   {sposta_cavalli();}
		else{ //se la corsa e' appena iniziata disegno le quotazioni e setto race_just_start a false in modo da sbloccare l'aggiornamento delle posizioni dei cavalli
			jlqnero.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(0).get_color()))); 
			jlqblu.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(1).get_color())));
			jlqverde.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(2).get_color())));
			jlqrosso.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(3).get_color())));
			jlqgiallo.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(4).get_color())));
			jlqbianco.setText("1_"+(2+lavagna.getPosizione(elenco_cavalli_temp.get(5).get_color())));
			
			
			race_just_start = false;}
		frame.setVisible(true);
	}
	
	
	/**
	 * Serve per istanziare i bottoni dei giocatori, va fatto solo la prima volta che viene
	 * istanziata la gui, ma non ou� essere fatto dal costruttore perche' quando lo chiamo
	 * non ho a disposizione l'elenco dei giocatori.
	 * Viene usatq quindi la variabile do_paint_player per capire se devo aggiornarli o no.
	 */
	public void paint_immagini_player()
	{
	int index=0;  //usato per creare dinamicamente i bottoni dei player in gioco 
	for(Giocatore player : elenco_player_temp)
	   {
		nomi_giocatori.get(index).setText(player.get_nome());
		immagini_players.get(index).setIcon(new ImageIcon(gui.resizeImage(player.get_character_card().getImmagine(),90, 179)));
		immagini_players.get(index).addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int index_analyzer=-1;
				if(e.getActionCommand().equals("JBplayer1")){ index_analyzer=0;}
				else if(e.getActionCommand().equals("JBplayer2")){ index_analyzer=1;}
				else if(e.getActionCommand().equals("JBplayer3")){ index_analyzer=2;}
				else if(e.getActionCommand().equals("JBplayer4")){ index_analyzer=3;}
				else if(e.getActionCommand().equals("JBplayer5")){ index_analyzer=4;}
				else if(e.getActionCommand().equals("JBplayer6")){ index_analyzer=5;}
				StringBuffer buffer = new StringBuffer();
				buffer.append("$---Elenco scommesse---$\n");
				for(Scommessa bet: elenco_player_temp.get(index_analyzer).get_scommesse() )
				   { buffer.append(""+bet.get_colore().toUpperCase()+ " " +bet.get_tipo().toUpperCase()+" " +bet.getDenaro()+  "\n"); }
				buffer.append("\nSoldi rimanenti:" +elenco_player_temp.get(index_analyzer).get_money()+"\n" );
				buffer.append("Pv rimanenti:" +elenco_player_temp.get(index_analyzer).get_pv()+"\n" );
				buffer.append("\n$----------------------------------$\n");
				buffer.append("                firma "+elenco_player_temp.get(index_analyzer).get_nome().toUpperCase());
				String bet_show=buffer.toString();
				JOptionPane.showMessageDialog(null, bet_show, "Scommesse giocatore", JOptionPane.INFORMATION_MESSAGE , new ImageIcon(elenco_player_temp.get(index_analyzer).get_character_card().getImmagine()));
			}
		}); 
		index++;
	   }
	while(index<=5)
	   {
		immagini_players.get(index).setIcon(new ImageIcon(gui.resizeImage("immagini_carte_personaggi/retro.jpg", 89, 179)));
		//immagini_players.get(index).setEnabled(false);
		index++;
	   }
	 }
	
	/**
	 * Metodo che sposta le label dei cavalli, partendo dalla vecchia posizione del 
	 * cavallo memorizzata in vecchie_posizioni ed arrivando alla nuova posizione
	 * del cavallo che mi e' arrivata dall'evento sposto di 50px verso destra la label
	 */
	private void sposta_cavalli()
	{
	int index=0;
	for(Cavallo horse : elenco_cavalli_temp)	
	   {
		System.out.print(""+ horse.get_color() + horse.get_posizione()+"  ");
		System.out.println("\n");
		segnalini_cavalli.get(index).setBounds((39*(horse.get_posizione()-1))+5,segnalini_cavalli.get(index).getBounds().y , 35, 38);
	    index++;
	   }
	}
}
