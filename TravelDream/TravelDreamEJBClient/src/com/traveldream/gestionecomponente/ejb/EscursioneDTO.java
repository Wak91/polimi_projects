package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;


public class EscursioneDTO {

	private int id;
	
	@NotNull
	private int costo;

	@NotNull
	private Date data;

	@NotEmpty
	private String immagine;

	@NotEmpty
	private String luogo;

	@NotEmpty
	private String nome;
	
	private ArrayList<PacchettoDTO> pacchettos;

	private ViaggioDTO viaggio;

	public ViaggioDTO getViaggio() {
		return viaggio;
	}

	public void setViaggio(ViaggioDTO viaggio) {
		this.viaggio = viaggio;
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public ArrayList<PacchettoDTO> getPacchettos() {
		return pacchettos;
	}

	public void setPacchettos(ArrayList<PacchettoDTO> pacchettos) {
		this.pacchettos = pacchettos;
	}
	
	
}
