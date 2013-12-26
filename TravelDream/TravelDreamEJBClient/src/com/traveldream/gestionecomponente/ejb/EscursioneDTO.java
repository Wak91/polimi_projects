package com.traveldream.gestionecomponente.ejb;

import java.util.Date;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


public class EscursioneDTO {

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
	
	
}
