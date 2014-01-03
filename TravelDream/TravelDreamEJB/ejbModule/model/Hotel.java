package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.HotelDTO;

import java.util.Date;


/**
 * The persistent class for the Hotel database table.
 * 
 */
@Entity
@Table(name="Hotel")
@NamedQueries({
	@NamedQuery(name="cercoHOTEL", query="SELECT h FROM Hotel h")

})public class Hotel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="`Costo giornaliero`")
	private int costo_giornaliero;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data fine`")
	private Date data_fine;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data inizio`")
	private Date data_inizio;

	@Column(name="Immagine")
	private String immagine;

	@Column(name="Luogo")
	private String luogo;

	@Column(name="Nome")
	private String nome;

	@Column(name="Stelle")
	private int stelle;


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
         this.immagine = ""; // da sistemare
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

}