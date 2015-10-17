package Controller_events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import Controller.Controller_Interface;
import Model.Giocatore;

public class showPriceEvent extends HorseFeverEventController implements Serializable{

	HashMap<Giocatore, ArrayList<Integer>> priceHashMap;
	
	public showPriceEvent(Controller_Interface controller,HashMap<Giocatore, ArrayList<Integer>> map) {
		
		super(showPriceEvent.class);
		super.controller_Interface=controller;
		priceHashMap=map;
	}

	public HashMap<Giocatore, ArrayList<Integer>> getPriceHashMap() {
		return priceHashMap;
	}
	
}
