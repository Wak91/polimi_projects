package model;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.HotelDTO;

import java.util.Date;
import java.util.List;
import java.util.Scanner;


/**
 * The persistent class for the Hotel database table.
 * 
 */
@Entity
@Table(name="Hotel")
@NamedQueries ( 
		       {
		    	@NamedQuery(name="Hotel.findAll", query="SELECT h FROM Hotel h"),
                @NamedQuery(name="Hotel.findbyId", query="SELECT h FROM Hotel h WHERE h.id = :d")
		       }
		      )
public class Hotel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Column(name="`Costo giornaliero`")
	private int costo_giornaliero;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data fine`")
	private Date data_fine;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data inizio`")
	private Date data_inizio;

	private String immagine;

	private String luogo;

	private String nome;

	private int stelle;

	@ManyToMany(mappedBy="hotels")
	private List<Pacchetto> pacchettos;
	 


	public Hotel() {
		    super();
		  }

	  public Hotel(HotelDTO hoteldto) 
	  {
	     this.nome = hoteldto.getNome();
	         this.luogo = hoteldto.getLuogo();
	         this.costo_giornaliero = hoteldto.getCosto_giornaliero();
	         this.data_inizio = hoteldto.getData_inizio();
	         this.data_fine = hoteldto.getData_fine();
			 this.immagine = hoteldto.getHotelImg();
	         this.stelle = hoteldto.getStelle();
	  }

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCosto_giornaliero() {
		return this.costo_giornaliero;
	}

	public void setCosto_giornaliero(int costo_giornaliero) {
		this.costo_giornaliero = costo_giornaliero;
	}

	public Date getData_fine() {
		return this.data_fine;
	}

	public void setData_fine(Date data_fine) {
		this.data_fine = data_fine;
	}

	public Date getData_inizio() {
		return this.data_inizio;
	}

	public void setData_inizio(Date data_inizio) {
		this.data_inizio = data_inizio;
	}

	public String getImmagine() {
		return this.immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public String getLuogo() {
		return this.luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getStelle() {
		return this.stelle;
	}

	public void setStelle(int stelle) {
		this.stelle = stelle;
	}
	public List<Pacchetto> getPacchettos() {
		return pacchettos;
	}

	public void setPacchettos(List<Pacchetto> pacchettos) {
		this.pacchettos = pacchettos;
	}

}