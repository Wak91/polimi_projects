package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import com.traveldream.gestionepack.ejb.PacchettoDTO;

public class HotelDTO {

	private int id;
	
	@NotEmpty 
	private String nome;

	@NotEmpty
	private String luogo;
	
	@NotNull
    private Integer costo_giornaliero;
	
	@NotNull
    private Date data_inizio;
	
	@NotNull
    private Date data_fine;
	
	@NotNull
	private String HotelImg; 
	
	@NotNull
	private Integer Stelle;
	
	private ArrayList<PacchettoDTO> pacchettos;

	public ArrayList<PacchettoDTO> getPacchettos() {
		return pacchettos;
	}

	public void setPacchettos(ArrayList<PacchettoDTO> pacchettos) {
		this.pacchettos = pacchettos;
	}

	public Integer getStelle() {
		return Stelle;
	}

	public void setStelle(Integer stelle) {
		Stelle = stelle;
	}


	public String getHotelImg() {
		return HotelImg;
	}

	public void setHotelImg(String hotelImg) {
		HotelImg = hotelImg;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public Integer getCosto_giornaliero() {
		return costo_giornaliero;
	}

	public void setCosto_giornaliero(Integer costo_giornaliero) {
		this.costo_giornaliero = costo_giornaliero;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
