package Gui_Events;

public class setNomeEvent extends HorseFeverEventGui{

	String name;
	
	public setNomeEvent(String nome) {
		super(setNomeEvent.class); // serve per capire al ritorno dalla rete che evento ho ricevuto
		// dato che io faccio un cast verso un evento GUI generale e voglio capire cosa è arrivato!
		name=nome;
		
	}

	public String getName() {
		return name;
	}

	
}
