package Controller_events;

import java.io.Serializable;
import java.util.EventObject;

import org.w3c.dom.events.Event;

import Controller.Controller_Interface;
import Model.Giocatore;

public abstract class HorseFeverEventController implements Serializable{

	
	protected  transient Controller_Interface controller_Interface;
	private Class tipo;
	
	public HorseFeverEventController(Class tipo)
	{this.tipo=tipo;}
	
	public Controller_Interface getController() {
		return controller_Interface;
	}
	

	

	public Class getTip()
	{
		return this.tipo;
	}

}
