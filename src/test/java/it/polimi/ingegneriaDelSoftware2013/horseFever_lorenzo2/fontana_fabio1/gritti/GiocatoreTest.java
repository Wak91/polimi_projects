 package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import static org.junit.Assert.*;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import Model.Carta_azione;
import Model.Carta_personaggio;
import Model.Cavallo;
import Model.Giocatore;

public class GiocatoreTest {
	Giocatore player;
	String immagine ;
	Carta_personaggio carta_personaggio;
	
	@Before
	public void setUp() throws Exception {
		 immagine = "immagini_carte_personaggio/cc.jpg";
		 carta_personaggio = new Carta_personaggio("Prova", 1300, 1 , immagine);

	
	}
	
	@Test
	public void Giocatore(){
	
		Giocatore giocatore = new Giocatore("John",carta_personaggio);
		assertTrue("pv Iniziali non corretti", giocatore.get_pv()==1);
		assertTrue("soldi non corretti",giocatore.get_money()==carta_personaggio.get_soldi());
		
	}
	
	@Test
	public void pv_test(){
		player=new Giocatore("Pippo",carta_personaggio);
		assertFalse(player.sub_pv(2)); //pv -1
		player.add_pv(10);	//pv 9
		int pv = player.get_pv();
		assertFalse(player.sub_pv(pv)); //pv 0
		player.add_pv(2);	//pv 2
		assertTrue(player.sub_pv(1)); //pv 1
		
	}
	
	@Test
	public void play_card_action_test(){
		player=new Giocatore("Pippo",carta_personaggio);

		Carta_azione action = new Carta_azione("Carta_azione_prova","a".charAt(0),"s".charAt(0),"dadsdsa","w2");
		player.add_action_card(action);
		Cavallo cavallo = new Cavallo("rosso");
		assertTrue(cavallo.get_carte_azione().size()==0);
		player.play_action_card(action, cavallo);
		assertTrue(cavallo.get_carte_azione().get(0).getNome()==action.getNome()); //controlla che la carta del giocaore sia andata su cavallo
		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void Scommette_test() {
		Giocatore gioc =new Giocatore("sasa", carta_personaggio);
		int oldMoney = gioc.get_money();
		int importo;
		String color ="rosso";
		String tipo = "Piazzata";
		
		
		System.out.println("Giocatore ha "+gioc.get_money()+" e deve scommettere almeno "+gioc.get_min_bet());
		importo=gioc.get_money();
		gioc.Scommette(importo, color, tipo);
		assertTrue(gioc.get_money()==0);
		assertTrue(gioc.get_scommesse().get(0).get_colore()==color&&gioc.get_scommesse().get(0).get_tipo()==tipo&&gioc.get_scommesse().get(0).getDenaro()==importo);
		
		importo=500;
		gioc.Scommette(importo, color, tipo);
		assertTrue(gioc.get_money()==oldMoney-500);
		assertTrue(gioc.get_scommesse().get(0).get_colore()==color&&gioc.get_scommesse().get(0).get_tipo()==tipo&&gioc.get_scommesse().get(0).getDenaro()==importo);


		

		
		Giocatore gioc2 =new Giocatore("sasa", carta_personaggio);
		gioc2.Scommette(gioc2.get_min_bet()-10, "da", "das");//lancia una eccezione
		
	}

}
