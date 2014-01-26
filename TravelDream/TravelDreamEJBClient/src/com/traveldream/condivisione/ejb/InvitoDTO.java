package com.traveldream.condivisione.ejb;

import javax.validation.constraints.NotNull;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;


public class InvitoDTO {
	
	
	private int id;
	
	@NotNull
	private String amico;

	@NotNull
	private boolean status;
	
	@NotNull
	private UserDTO utente;
	
	private ViaggioDTO viaggio;
	
	public InvitoDTO(){

	}
	
	public int getId(){
		return id;
	}

	public void setId(int id){
		this.id = id;
	}

	public boolean getStatus(){
		return status;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public String getAmico(){
		return amico;
	}

	public void setAmico(String amico){
		this.amico = amico;
	}

	public UserDTO getUtente(){
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
