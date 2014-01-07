package com.traveldream.gestionecomponente.ejb;

import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;


public class VoloDTO {

	private int id;
	
	@NotEmpty
	private String compagnia;

	@NotNull
	private int costo;

	@NotNull
	private Date data;

	@NotEmpty
	private String immagine;

	@NotEmpty
	private String luogo_arrivo;

	@NotEmpty
	private String luogo_partenza;

	public String getCompagnia() {
		return compagnia;
	}

	public void setCompagnia(String compagnia) {
		this.compagnia = compagnia;
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

	public String getLuogo_arrivo() {
		return luogo_arrivo;
	}

	public void setLuogo_arrivo(String luogo_arrivo) {
		this.luogo_arrivo = luogo_arrivo;
	}

	public String getLuogo_partenza() {
		return luogo_partenza;
	}

	public void setLuogo_partenza(String luogo_partenza) {
		this.luogo_partenza = luogo_partenza;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
