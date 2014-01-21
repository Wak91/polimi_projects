package com.traveldream.gestioneprenotazione.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

public class ViaggioDTO {

	private int id;
	
	@NotNull 
	private HotelDTO hotel;
	
	@NotNull
	private VoloDTO volo_andata;
	
	@NotNull
	private VoloDTO volo_ritorno; // perchè viaggio nel db ha solo un volo?!?! andata + ritorno!!
		
	@NotNull
    private Date data_inizio;
	
	@NotNull
    private Date data_fine;
	
	private ArrayList <EscursioneDTO> lista_escursioni;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HotelDTO getHotel() {
		return hotel;
	}

	public void setHotel(HotelDTO hotel) {
		this.hotel = hotel;
	}

	public VoloDTO getVolo_andata() {
		return volo_andata;
	}

	public void setVolo_andata(VoloDTO volo_andata) {
		this.volo_andata = volo_andata;
	}

	public VoloDTO getVolo_ritorno() {
		return volo_ritorno;
	}

	public void setVolo_ritorno(VoloDTO volo_ritorno) {
		this.volo_ritorno = volo_ritorno;
	}

	public Date getData_inizio() {
		return data_inizio;
	}

	public void setData_inizio(Date data_inizio) {
		this.data_inizio = data_inizio;
	}

	public Date getData_fine() {
		return data_fine;
	}

	public void setData_fine(Date data_fine) {
		this.data_fine = data_fine;
	}

	public List<EscursioneDTO> getLista_escursioni() {
		return lista_escursioni;
	}

	public void setLista_escursioni(ArrayList<EscursioneDTO> lista_escursioni) {
		this.lista_escursioni = lista_escursioni;
	}
}