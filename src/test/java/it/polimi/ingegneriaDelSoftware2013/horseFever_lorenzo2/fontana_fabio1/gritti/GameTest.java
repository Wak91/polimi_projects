package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.swing.ImageIcon;

import org.junit.Before;
import org.junit.Test;

import Model.Carta_azione;
import Model.Carta_personaggio;
import Model.Cavallo;
import Model.Game;
import Model.Giocatore;
import Model.Lavagna;
import Model.Scommessa;

public class GameTest {
	
	int oldLoremoney;
	int oldPippomoney;
	int oldPlutomoney;

	ArrayList<Giocatore> elenco_giocatori=new ArrayList<Giocatore>();
	ArrayList<String> classifica =new ArrayList<String>();
	Lavagna lavagna;
	Game gmGame = new Game();
	final int  importo=300;
	final String winner="rosso";
	final String second="giallo";
	final String third="blu";

	@Before
	public void setUp() throws Exception {
		
		//creo giocatori
		gmGame.create_player("Lore");
		gmGame.create_player("Pippo");
		gmGame.create_player("Pluto");
		//piazzo scommesse
		gmGame.set_primo_giocatore("orario");
		gmGame.get_giocatore_corrente().Scommette(importo, winner, "Vincente");
		gmGame.next_giocatore("orario");
		
		gmGame.get_giocatore_corrente().Scommette(importo, second, "Piazzata");
		gmGame.next_giocatore("orario");
		
		gmGame.get_giocatore_corrente().Scommette(importo, winner, "Piazzata");
		gmGame.get_giocatore_corrente().Scommette(importo, second, "Piazzata");
		
		gmGame.set_primo_giocatore("orario");
		//salvo i soldi correnti dei giocatori
		oldLoremoney=gmGame.get_giocatore_corrente().get_money();
		System.out.println(gmGame.get_giocatore_corrente().get_nome()+oldLoremoney);
		gmGame.next_giocatore("orario");
		oldPippomoney=gmGame.get_giocatore_corrente().get_money();
		System.out.println(gmGame.get_giocatore_corrente().get_nome()+oldPippomoney);
		gmGame.next_giocatore("orario");
		oldPlutomoney=gmGame.get_giocatore_corrente().get_money();
		System.out.println(gmGame.get_giocatore_corrente().get_nome()+oldPlutomoney);
		//creo classifica;
		classifica= gmGame.getClassifica();
		classifica.add(winner);
		classifica.add(second);
		classifica.add(third);
		classifica.add("verde");
		classifica.add("bianco");
		classifica.add("nero");
		System.out.println("Classifica");

		for (String colString : gmGame.getClassifica()) {
			System.out.println(colString);
		}
	}

	@Test
	public void test_pagamenti() {
		lavagna = Lavagna.creaLavagna();
		System.out.println("start test");
		gmGame.pay_price();
		
		gmGame.set_primo_giocatore("orario");
		System.out.println(gmGame.get_giocatore_corrente().get_nome()+" ha "+gmGame.get_giocatore_corrente().get_money()+"scommettendo"+importo+"  Vincente su quot "+(lavagna.getPosizione("rosso")+2));
		System.out.println("Vince "+(lavagna.getPosizione("rosso")+2)*importo);

		int scud_money= make_payment_scud(gmGame.get_giocatore_corrente());
		assertTrue(gmGame.get_giocatore_corrente().get_money()==oldLoremoney+(lavagna.getPosizione(winner)+2)*importo+2*scud_money);
		
		gmGame.next_giocatore("orario");
		System.out.println(gmGame.get_giocatore_corrente().get_nome()+" ha "+gmGame.get_giocatore_corrente().get_money()+"scommettendo "+importo+" Piazzato su quot "+(lavagna.getPosizione("rosso")+2));
		System.out.println("Vince "+(lavagna.getPosizione("rosso")+2)*importo);
		scud_money= make_payment_scud(gmGame.get_giocatore_corrente());
		System.out.println("scud_money "+scud_money);

		assertTrue(gmGame.get_giocatore_corrente().get_money()==oldPippomoney+2*importo+2*scud_money);//2 scud_money perche make payment aggiunge a giocatore i soldi oltre a ritornarne il valore

		gmGame.next_giocatore("orario");
		scud_money= make_payment_scud(gmGame.get_giocatore_corrente());
		assertTrue(gmGame.get_giocatore_corrente().get_money()==oldPlutomoney+4*importo+2*scud_money+(lavagna.getPosizione(winner)+2)*importo*0);//2 scud_money perche make payment aggiunge a giocatore i soldi oltre a ritornarne il valore


	}

	private int make_payment_scud(Giocatore player){
		final int primo_premio=GameOption.primo_premio;
		final int secondo_premio=GameOption.secondo_premio;
		final int terzo_premio=GameOption.terzo_premio;
		if(player.get_scuderia()==classifica.get(0)){
			player.set_money(player.get_money()+primo_premio);
			return primo_premio;
		}
		else if(player.get_scuderia()==classifica.get(1)){
			player.set_money(player.get_money()+secondo_premio);
			return secondo_premio;
		}
		else if(player.get_scuderia()==classifica.get(2)){
			player.set_money(player.get_money()+terzo_premio);
			return terzo_premio;
		}	
	return 0;
	}
	
	
}
