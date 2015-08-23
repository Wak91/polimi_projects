package com.traveldream.gestioneprenotazione.ejb;
import javax.validation.constraints.NotNull;

import com.traveldream.autenticazione.ejb.UserDTO;


public class PrenotazioneDTO {

	private int id;
	
	@NotNull 
	private int costo;
	
	@NotNull
	private int numero_persone;
	
	@NotNull
	private UserDTO utente;
	
	@NotNull
	private ViaggioDTO viaggio;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public int getNumero_persone() {
		return numero_persone;
	}

	public void setNumero_persone(int numero_persone) {
		this.numero_persone = numero_persone;
	}

	public UserDTO getUtente() {
		return utente;
	}

	public void setUtente(UserDTO utente) {
		this.utente = utente;
	}

	public ViaggioDTO getViaggio() {
		return viaggio;
	}

	public void setViaggio(ViaggioDTO viaggio) {
		this.viaggio = viaggio;
	}

}