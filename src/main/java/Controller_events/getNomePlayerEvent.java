package Controller_events;

import java.io.Serializable;

import Controller.Controller_Interface;

public class getNomePlayerEvent extends HorseFeverEventController implements Serializable {


	public getNomePlayerEvent(Controller_Interface cont) {
		super(getNomePlayerEvent.class);
		super.controller_Interface=cont;
	}
}
