package Controller_events;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.Controller_Interface;
import Model.Carta_movimento;
import Model.Cavallo;
import Model.Giocatore;
import Model.Lavagna;

public class EventoCorsa  extends HorseFeverEventController implements Serializable{

	private ArrayList <Cavallo> elenco_pedine;
	private Integer lunghezza; 
	private Carta_movimento card_m;
	private Integer fase_corsa;
	private ArrayList <Giocatore> elenco_player;
	private String colore1,colore2;
	private Lavagna lavagna;
	
	public EventoCorsa(Controller_Interface cont , ArrayList <Cavallo> elenco_pedine , Integer lunghezza , Carta_movimento card , Integer fase, ArrayList<Giocatore> elenco_giocatori,String colore1,String colore2,Lavagna lavagna)
	{
		super(EventoCorsa.class);
		super.controller_Interface=cont;	
		this.elenco_pedine = elenco_pedine;
		this.lunghezza = lunghezza;
		this.card_m = card;
		this.fase_corsa = fase;
		this.elenco_player = elenco_giocatori;
		this.colore1 = colore1;
		this.colore2 = colore2;
		this.lavagna=lavagna;
	}
	
	public Lavagna getLavagna() {
		return lavagna;
	}

	public String getColore1()
	{return this.colore1;}
	
	public String getColore2()
	{return this.colore2;}
	
	
	public ArrayList <Cavallo> Getpedine()
	{
		return this.elenco_pedine;
	}
	
	public Integer getLunghezza(){
		return lunghezza;
	}
	
	public Integer getFase()
	{return this.fase_corsa;}
	
	public Carta_movimento getCartaM()
	{
		return this.card_m;
	}
	
public ArrayList <Giocatore> GetElencoPlayer()
{
	return this.elenco_player;
	}
	

}
