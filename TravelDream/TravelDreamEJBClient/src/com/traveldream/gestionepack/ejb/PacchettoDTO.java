package com.traveldream.gestionepack.ejb;
import  com.traveldream.gestionecomponente.ejb.*;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

public class PacchettoDTO {

	@NotEmpty 
	private String nome;
	
	@NotEmpty
	private String destinazione;
	
	@NotNull
    private Date data_inizio;
	
	@NotNull
    private Date data_fine;
	
	@NotEmpty
	private String PathtoImage; 
	
	@NotNull
	private ArrayList <HotelDTO> lista_hotel;
	
	@NotNull 
	private ArrayList <VoloDTO> lista_voli;
	
	@NotNull
	private ArrayList <EscursioneDTO> lista_escursioni;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
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

	public String getPathtoImage() {
		return PathtoImage;
	}

	public void setPathtoImage(String pathtoImage) {
		PathtoImage = pathtoImage;
	}

	public ArrayList<HotelDTO> getLista_hotel() {
		return lista_hotel;
	}

	public void setLista_hotel(ArrayList<HotelDTO> lista_hotel) {
		this.lista_hotel = lista_hotel;
	}

	public ArrayList<VoloDTO> getLista_voli() {
		return lista_voli;
	}

	public void setLista_voli(ArrayList<VoloDTO> lista_voli) {
		this.lista_voli = lista_voli;
	}

	public ArrayList<EscursioneDTO> getLista_escursioni() {
		return lista_escursioni;
	}

	public void setLista_escursioni(ArrayList<EscursioneDTO> lista_escursioni) {
		this.lista_escursioni = lista_escursioni;
	}
	
	
	
	
}
