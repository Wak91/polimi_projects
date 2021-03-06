package com.traveldream.condivisione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;


public class GiftListDTO {
	
	
	private int id;

	@NotNull
	private boolean hotelPag;

	@NotNull
	private boolean voloAPag;

	private String hash;
	
	private int npersone;
	
	@NotNull
	private boolean voloRPag;
	
	@NotEmpty
	private List<EscursionePagataDTO> escursionePagata;
	
	@NotNull
	private List<String> amico;
	
	@NotNull
	private UserDTO utente;
	
	private ViaggioDTO viaggio;
	
	public GiftListDTO(){
		amico =new ArrayList<String>();
		escursionePagata = new ArrayList<EscursionePagataDTO>();
		npersone=1;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public boolean isHotelPag() {
		return hotelPag;
	}

	public void setHotelPag(boolean hotelPag) {
		this.hotelPag = hotelPag;
	}

	public boolean isVoloAPag() {
		return voloAPag;
	}

	public void setVoloAPag(boolean voloAPag) {
		this.voloAPag = voloAPag;
	}

	public boolean isVoloRPag() {
		return voloRPag;
	}

	public void setVoloRPag(boolean voloRPag) {
		this.voloRPag = voloRPag;
	}

	public List<EscursionePagataDTO> getEscursionePagata() {
		return escursionePagata;
	}

	public void setEscursionePagata(List<EscursionePagataDTO> escursionePagata) {
		this.escursionePagata = escursionePagata;
	}

	public List<String> getAmico() {
		return amico;
	}

	public void setAmico(List<String> amico) {
		this.amico = amico;
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

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getNpersone() {
		return npersone;
	}

	public void setNpersone(int npersone) {
		this.npersone = npersone;
	}

	



}
