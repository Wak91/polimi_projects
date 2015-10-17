package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import Model.Carta_azione;
import Model.Lavagna;

import Model.Cavallo;

public class CavalloTest {

	Cavallo horseCavallo;
	ArrayList<Carta_azione> deck = new ArrayList<Carta_azione>();
	@Before
	/**
	 * Aggiungo 6 carte al cavallo e tengo traccia di queste carte in un deck che controllero poi
	 * @throws Exception
	 */
	public void setUp() throws Exception {
		horseCavallo = new Cavallo("rosso");
		for (int i = 0; i < 6; i++) {
			Carta_azione card = new Carta_azione("prova", "s".charAt(0), "a".charAt(0), "asd", "null");
			 deck.add(card);
			horseCavallo.add_carta_azione(card);

		}
		
	}

	@Test
	/**
	 * controllo che le 6 carte aggiunte al cavallo siano quelle corrette
	 */
	public void have_got_carte_azione_test() {//
		for (int i = 0; i < 6; i++) {
			assertTrue(horseCavallo.get_carte_azione().get(i)==deck.get(i));
		}
		
	}
	@Test
	/**
	 * controllo risultato compare to
	 */
	public void compare_to_test_pos(){
		Lavagna lavagna =Lavagna.creaLavagna();
		Cavallo cav2 = new Cavallo("verde");
		if (lavagna.getPosizione(cav2.get_color())<lavagna.getPosizione(horseCavallo.get_color())) {
			assertTrue(cav2.compareTo(horseCavallo)==1);
		}
		else {
			assertTrue(cav2.compareTo(horseCavallo)==-1);
		}
	}
	@Test
	public void compare_to_test_card(){
		Cavallo cav2 = new Cavallo("verde");
		Carta_azione carta=new Carta_azione("in_igni_veritas", "se".charAt(0), "se".charAt(0), "xd", "");
		cav2.add_carta_azione(carta);
		assertTrue(cav2.compareTo(horseCavallo)==-1);
		
		cav2.delete_action_card(carta);
		Carta_azione carta2=new Carta_azione("mala_tempora", "se".charAt(0), "se".charAt(0), "xd", "");
		
		cav2.add_carta_azione(carta2);
		assertTrue(cav2.compareTo(horseCavallo)==1);

	}
	
}
