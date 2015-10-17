package Gui_Events;

import java.io.Serializable;

public class setScommessaEvent extends HorseFeverEventGui implements Serializable {

	int money;
	String tipo;
	String cavallo;
	boolean seconda_scommessa;
	
	public setScommessaEvent(int soldi,String type,String horse,boolean sec_scommessa) {
		super(setScommessaEvent.class);
		money=soldi;
		tipo=type;
		cavallo=horse;
		seconda_scommessa=sec_scommessa;
	}
	public boolean isSeconda_scommessa(){
		return seconda_scommessa;
	}

	public int getMoney() {
		return money;
	}


	public String getTipo() {
		return tipo;
	}


	public String getCavallo() {
		return cavallo;
	}
	
}
