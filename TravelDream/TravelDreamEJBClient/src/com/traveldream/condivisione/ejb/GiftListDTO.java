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
	private byte hotelPag;

	@NotNull
	private byte voloAPag;

	
	@NotNull
	private byte voloRPag;
	
	@NotEmpty
	private List<EscursionePagataDTO> escursionePagata;
	
	@NotNull
	private List<String> amico;
	
	@NotNull
	private UserDTO utente;
	
	private ViaggioDTO viaggio;
	
	public GiftListDTO(){
		amico =new ArrayList<String>();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getHotelPag() {
		return hotelPag;
	}

	public void setHotelPag(byte hotelPag) {
		this.hotelPag = hotelPag;
	}

	public byte getVoloAPag() {
		return voloAPag;
	}

	public void setVoloAPag(byte voloAPag) {
		this.voloAPag = voloAPag;
	}

	public byte getVoloRPag() {
		return voloRPag;
	}

	public void setVoloRPag(byte voloRPag) {
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

	



}
