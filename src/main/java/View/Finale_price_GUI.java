package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Controller_events.showPriceEvent;
import Model.Carta_personaggio;
import Model.Giocatore;

public class Finale_price_GUI {

	JPanel panel_1;
	JFrame frame;
	GUI_interface gui_interface;
	
	private JLabel pers_0;
	private JLabel pers_1;
	private JLabel pers_2;
	private JLabel pers_3 ;
	private JLabel pers_4;
	private JLabel pers_5;
	
	private JLabel scom_0;
	private JLabel scom_1;
	private JLabel scom_2;
	private JLabel scom_3;
	private JLabel scom_4;
	private JLabel scom_5;

	private JLabel lscom1; 
	private JLabel lscom2;
	
	ArrayList<JLabel> scomm;
	ArrayList<JLabel> cart_pers;
	ArrayList<JLabel> lscomm;
	private JLabel jlscom3;
	private JLabel jlscom4;
	private JLabel jlscom5;
	private JLabel jlscom6;
	private JLabel jlscom7;
	private JLabel jlscom8;
	private JLabel jlscom9;
	private JLabel jlcom10;
	private JLabel lscom11;
	private JLabel jlscom12;
	
	public Finale_price_GUI(final GUI_interface gui) {
		gui_interface=gui;
		frame = gui.get_frame();
		frame.setBounds(100, 100, 900, 696);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	
		cart_pers=new ArrayList<JLabel>();
		scomm= new ArrayList<JLabel>();
		lscomm = new ArrayList<JLabel>();
		
		JPanel jlscom11 = new JPanel(){ //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
			
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui.resizeImage("images/images.jpg", 554, 721));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		jlscom11.setBounds(346, 0, 554, 721);
		frame.getContentPane().add(jlscom11);
		jlscom11.setLayout(null);
		

		 pers_0 = new JLabel("New label");
		pers_0.setBounds(29, 20, 79, 90);
		jlscom11.add(pers_0);

		
		 pers_1 = new JLabel("New laberel");
		pers_1.setBounds(29, 132, 79, 90);
		jlscom11.add(pers_1);
		
		 pers_2 = new JLabel("New label");
		pers_2.setBounds(29, 241, 79, 90);
		jlscom11.add(pers_2);
		
		 pers_3 = new JLabel("New label");
		pers_3.setBounds(29, 343, 79, 90);
		jlscom11.add(pers_3);
		
		 pers_4 = new JLabel("New label");
		pers_4.setBounds(29, 445, 79, 90);
		jlscom11.add(pers_4);
		
		 pers_5 = new JLabel("New label");
		pers_5.setBounds(29, 547, 79, 90);
		jlscom11.add(pers_5);
		
		cart_pers.add(pers_0);
		cart_pers.add(pers_1);
		cart_pers.add(pers_2);
		cart_pers.add(pers_3);
		cart_pers.add(pers_4);
		cart_pers.add(pers_5);
		
		
		 scom_0 = new JLabel("");
		scom_0.setForeground(Color.WHITE);
		scom_0.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		scom_0.setBounds(120, 20, 316, 88);
		jlscom11.add(scom_0);
		
		 scom_1 = new JLabel("");
		scom_1.setBounds(120, 132, 316, 88);
		scom_1.setForeground(Color.WHITE);
		scom_1.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		jlscom11.add(scom_1);
		
		 scom_2 = new JLabel("");
		 scom_2.setForeground(Color.WHITE);
		scom_2.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		scom_2.setBounds(120, 242, 316, 88);
		jlscom11.add(scom_2);
		
		 scom_3 = new JLabel("");
		scom_3.setBounds(120, 343, 316, 88);
		scom_3.setForeground(Color.WHITE);
		scom_3.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		jlscom11.add(scom_3);
		
		 scom_4 = new JLabel("");
		scom_4.setBounds(120, 447, 316, 88);
		scom_4.setForeground(Color.WHITE);
		scom_4.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		jlscom11.add(scom_4);
		
		scom_5 = new JLabel("");
		scom_5.setBounds(120, 547, 316, 88);
		scom_5.setForeground(Color.WHITE);
		scom_5.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));
		jlscom11.add(scom_5);
		
		scomm.add(scom_0);
		scomm.add(scom_1);
		scomm.add(scom_2);
		scomm.add(scom_3);
		scomm.add(scom_4);
		scomm.add(scom_5);
		
		lscom1 = new JLabel("");
		lscom1.setBounds(456, 11, 54, 49);
		jlscom11.add(lscom1);
		
		lscom2 = new JLabel("");
		lscom2.setBounds(456, 61, 54, 49);
		jlscom11.add(lscom2);
		
		jlscom3 = new JLabel("");
		jlscom3.setBounds(456, 121, 54, 49);
		jlscom11.add(jlscom3);
		
		jlscom4 = new JLabel("");
		jlscom4.setBounds(456, 167, 54, 49);
		jlscom11.add(jlscom4);
		
		jlscom5 = new JLabel("");
		jlscom5.setBounds(456, 232, 54, 49);
		jlscom11.add(jlscom5);
		
		jlscom6 = new JLabel("");
		jlscom6.setBounds(456, 279, 54, 49);
		jlscom11.add(jlscom6);
		
		jlscom7 = new JLabel("");
		jlscom7.setBounds(456, 338, 54, 49);
		jlscom11.add(jlscom7);
		
		jlscom8 = new JLabel("");
		jlscom8.setBounds(456, 384, 54, 49);
		jlscom11.add(jlscom8);
		
		jlscom9 = new JLabel("");
		jlscom9.setBounds(456, 436, 54, 49);
		jlscom11.add(jlscom9);
		
		jlcom10 = new JLabel("");
		jlcom10.setBounds(456, 486, 54, 49);
		jlscom11.add(jlcom10);
		
		lscom11 = new JLabel("");
		lscom11.setBounds(456, 540, 54, 49);
		jlscom11.add(lscom11);
		
		jlscom12 = new JLabel("");
		jlscom12.setBounds(456, 590, 54, 49);
		jlscom11.add(jlscom12);

		lscomm.add(lscom1);
		lscomm.add(lscom2);
		lscomm.add(jlscom3);
		lscomm.add(jlscom4);
		lscomm.add(jlscom5);
		lscomm.add(jlscom6);
		lscomm.add(jlscom7);
		lscomm.add(jlscom8);
		lscomm.add(jlscom9);
		lscomm.add(jlcom10);
		lscomm.add(lscom11);
		lscomm.add(jlscom12);
		
	
	}
	public void show(showPriceEvent evt) {
		HashMap<Giocatore, ArrayList<Integer>> mappa=evt.getPriceHashMap();
		int i=0,j=0;
		
		for (Giocatore gioc : mappa.keySet()) {
			gioc.get_character_card().getImmagine();
			cart_pers.get(i).setIcon(new ImageIcon(gui_interface.resizeImage(gioc.get_character_card().getImmagine(), 79, 90)));
			int num_scomm=gioc.get_scommesse().size();
			if(num_scomm!=0)
			  {lscomm.get(j).setIcon(new ImageIcon(gui_interface.resizeImage("immagini_segnalini_scommessa/" + gioc.get_scommesse().get(0).get_tipo()+gioc.get_scommesse().get(0).get_colore()+".jpg", 54, 49)));
			   j++;
			   if(num_scomm==2)
				 {lscomm.get(j).setIcon(new ImageIcon(gui_interface.resizeImage("immagini_segnalini_scommessa/" + gioc.get_scommesse().get(1).get_tipo()+gioc.get_scommesse().get(1).get_colore()+".jpg", 54, 49)));}
			   j++;
			  }
			String string="<html>"+gioc.get_nome()+" hai vinto "+mappa.get(gioc).get(0)+"$ e "+mappa.get(gioc).get(1)+" pv dalla scommessa e "+mappa.get(gioc).get(2)+" $ dalla tua scuderia</html>";
			scomm.get(i).setText(string);
			i++;
		}
		while (i<6){
			cart_pers.get(i).setIcon(new ImageIcon(gui_interface.resizeImage("immagini_carte_personaggi/retro.jpg",79,90)));
			i++;
		}
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Finale_price_GUI window = new Finale_price_GUI(new GUI_interface());
					HashMap<Giocatore, ArrayList<Integer>> ma = new HashMap<Giocatore, ArrayList<Integer>>();
					ArrayList<Integer> priceArrayList = new ArrayList<Integer>();
					priceArrayList.add(11);
					priceArrayList.add(33);
					priceArrayList.add(44);


					ma.put(new Giocatore("kk", new Carta_personaggio("sq", 1, 2, "immagini_carte_personaggi/cm.jpg")), priceArrayList);
					ma.put(new Giocatore("Jdew", new Carta_personaggio("sq", 1, 2, "immagini_carte_personaggi/cm.jpg")), priceArrayList);

					showPriceEvent ss= new showPriceEvent(null, ma);
 					window.show(ss);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
