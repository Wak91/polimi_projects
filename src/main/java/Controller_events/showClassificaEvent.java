package Controller_events;

import java.io.Serializable;
import java.util.ArrayList;

import Controller.Controller_Interface;

public class showClassificaEvent extends HorseFeverEventController implements Serializable{
	

	private ArrayList<String> classifica;

	
	
	public  showClassificaEvent(Controller_Interface cont,ArrayList<String> classif) {
		super(showClassificaEvent.class);
		super.controller_Interface=cont;
		classifica = classif;
	}
	public ArrayList<String> getClassifica() {
		return classifica;
	}
	
	
}
