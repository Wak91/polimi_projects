package Controller_events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.text.StyledEditorKit.BoldAction;

import Controller.Controller_Interface;
import Model.Cavallo;
import Model.Giocatore;
import Model.Lavagna;

public class askScommessaEvent extends HorseFeverEventController implements Serializable {

	private boolean errore_tipo;
	private boolean errore_cavallo;
	private Giocatore player;
	private boolean seconda_scommessa;
	private HashMap<String, Integer> disp_scud;
	private Lavagna lavagna;

	
	
	public boolean isSeconda_scommessa() {
		return seconda_scommessa;
	}





	public Lavagna getLavagna() {
		return lavagna;
	}





	public  askScommessaEvent(Controller_Interface cont,Giocatore gioc,boolean er_cav,boolean er_tipo,boolean seconda_scomm,HashMap<String, Integer> segnalini,Lavagna lav) {
		super(askScommessaEvent.class);
		super.controller_Interface=cont;
		player=gioc;
		errore_cavallo=er_cav;
		errore_tipo=er_tipo;
		seconda_scommessa=seconda_scomm;
		disp_scud=segnalini;
		lavagna=lav;
		
	}



	public HashMap<String, Integer> getDisp_scud() {
		return disp_scud;
	}


	public boolean isErrore_tipo() {
		return errore_tipo;
	}


	public boolean isErrore_cavallo() {
		return errore_cavallo;
	}
	public Giocatore get_giocatore() {
		return this.player;
	}

}
