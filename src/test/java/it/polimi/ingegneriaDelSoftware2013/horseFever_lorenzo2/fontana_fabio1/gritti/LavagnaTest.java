package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import Model.Lavagna;
import Model.Quotazione;

public class LavagnaTest {

	@Test
	public void testLavagna()
	{	Lavagna.deleteLavagna();
		Lavagna lava = Lavagna.creaLavagna();
		
		int i;
		
	    Quotazione q = new Quotazione(0,"rosso");
	    lava.aggiungi_quotazione(q);
	    Quotazione q1 = new Quotazione(0,"verde");
	    lava.aggiungi_quotazione(q1);
	    Quotazione q2 = new Quotazione(1,"nero");
	    lava.aggiungi_quotazione(q2);
	    Quotazione q3 = new Quotazione(3,"bianco");
	    lava.aggiungi_quotazione(q3);
	    Quotazione q4 = new Quotazione(4,"blu");
	    lava.aggiungi_quotazione(q4);
	    Quotazione q5 = new Quotazione(6,"giallo");
	    lava.aggiungi_quotazione(q5);
	    
	    // creo una classifica di prova 
		ArrayList<String> classifica = new ArrayList<String>();
		classifica.add("giallo");
		classifica.add("rosso");
		classifica.add("nero");
		classifica.add("verde");
		classifica.add("bianco");
		classifica.add("blu");
		lava.updateLavagna(classifica);
		//assertTrue(lava.getPosizione("rosso")==1);
		System.out.println(lava.getPosizione("verde")+" =1");
		assertTrue(lava.getPosizione("verde")==1);
		//assertTrue(lava.getPosizione("nero")==2);
		assertTrue(lava.getPosizione("bianco")==4);
		assertTrue(lava.getPosizione("blu")==5);
		assertTrue(lava.getPosizione("giallo")==5);
		
		
	
	}
}
