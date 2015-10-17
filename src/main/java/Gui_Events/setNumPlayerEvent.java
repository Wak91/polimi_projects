package Gui_Events;

public class setNumPlayerEvent extends HorseFeverEventGui {

	int num_player;
	public int getNum_player() {
		return num_player;
	}
	
	public setNumPlayerEvent(int i) {
		
		super(setNumPlayerEvent.class);
		num_player=i;
	}
	
}
