package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import Model.Carta_azione;
import Model.Cavallo;
import Model.Game;

public class Card_Handler_test {
	ArrayList<Cavallo> elenco_cav= new ArrayList<Cavallo>();
	Cavallo cav1;
	Cavallo cav2;
	Cavallo cav3;
	Cavallo cav4;
	Cavallo cav5;
	Cavallo cav6;
	Game gmGame =new Game();

	@Before
	public void setUp() throws Exception {
		cav1=new Cavallo("rosso");
		cav2=new Cavallo("verde");
		cav3=new Cavallo("giallo");
		cav4=new Cavallo("nero");
		cav5=new Cavallo("bianco");
		cav6=new Cavallo("blu");

		elenco_cav.add(cav1);
		elenco_cav.add(cav2);
		elenco_cav.add(cav3);
		elenco_cav.add(cav4);
		elenco_cav.add(cav5);
		elenco_cav.add(cav6);


	}

	@Test
	public void test_carte_partenza() {
		gmGame.INIT();
		int quotcav1=gmGame.getLavagna().getPosizione(gmGame.get_cavalli().get(0).get_color());
		int quotcav2=gmGame.getLavagna().getPosizione(gmGame.get_cavalli().get(1).get_color());
		int quotcav3=gmGame.getLavagna().getPosizione(gmGame.get_cavalli().get(2).get_color());
		int quotcav4=gmGame.getLavagna().getPosizione(gmGame.get_cavalli().get(3).get_color());

	
		
		int old_pos1 = gmGame.get_cavalli().get(0).get_posizione();
		int old_pos2 = gmGame.get_cavalli().get(1).get_posizione();
		int old_pos3 = gmGame.get_cavalli().get(2).get_posizione();
		int old_pos4 = gmGame.get_cavalli().get(3).get_posizione();

		gmGame.add_card_horse(gmGame.get_cavalli().get(0), new Carta_azione("fortuna_benevola", "B".charAt(0), "P".charAt(0), "dwd", "dadas"));

		gmGame.add_card_horse(gmGame.get_cavalli().get(1), new Carta_azione("magna_velocitas", "A".charAt(0), "P".charAt(0), "dwd", "dadas"));
		gmGame.add_card_horse(gmGame.get_cavalli().get(2), new Carta_azione("globus_obscurus", "A".charAt(0), "N".charAt(0), "dwd", "dadas"));
		gmGame.add_card_horse(gmGame.get_cavalli().get(1), new Carta_azione("fortuna_benevola", "B".charAt(0), "P".charAt(0), "dwd", "dadas"));
		gmGame.add_card_horse(gmGame.get_cavalli().get(2), new Carta_azione("aqua_putrida", "B".charAt(0), "N".charAt(0), "dwd", "dadas"));
		gmGame.add_card_horse(gmGame.get_cavalli().get(3), new Carta_azione("aqua_putrida", "B".charAt(0), "N".charAt(0), "dwd", "dadas"));

		gmGame.partenza();
		assertTrue(gmGame.get_cavalli().get(0).get_posizione()-old_pos1==gmGame.getCartaMovimento().get_movimento(quotcav1)+1);
		
		assertTrue(gmGame.get_cavalli().get(1).get_posizione()-old_pos1==4);
		assertTrue(gmGame.get_cavalli().get(2).get_posizione()-old_pos2==0);
		assertTrue(gmGame.get_cavalli().get(3).get_posizione()-old_pos3==gmGame.getCartaMovimento().get_movimento(quotcav4)-1);

		System.out.println(gmGame.get_cavalli().get(1).get_posizione());
		
	}

	public static void main(String[] args) {
		
	}

}
