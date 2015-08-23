package com.traveldream.condivisione.ejb;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;


public class EscursionePagataDTO {
	private int id;
	
	private boolean escPagata;
	
	private EscursioneDTO escursione;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getEscPagata() {
		return escPagata;
	}

	public void setEscPagata(boolean escPagata) {
		this.escPagata = escPagata;
	}

	public EscursioneDTO getEscursione() {
		return escursione;
	}

	public void setEscursione(EscursioneDTO escursione) {
		this.escursione = escursione;
	}


}
