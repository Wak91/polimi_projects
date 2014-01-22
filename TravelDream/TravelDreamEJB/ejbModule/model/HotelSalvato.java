package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.HotelDTO;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the HotelSalvato database table.
 * Questa classe serve per congelare gli hotel che sono 
 * stati salvati in un viaggio o in una prenotazione , se avessimo usato
 * la classe Hotel in caso di eliminazione di componenti, gli utenti
 * si sarebbero ritrovato delle prenotazioni incoerenti.
 * La tabella HotelSalvato può essere utilizzata per tracciare tutti gli hotel prenotati
 * almeno una volta nella history del sito.
 */
@Entity
@Table(name="HotelSalvato")
@NamedQuery(name="HotelSalvato.findAll", query="SELECT h FROM HotelSalvato h")
public class HotelSalvato implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY) 
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

	//bi-directional many-to-one association to Viaggio
	@OneToMany(mappedBy="hotelSalvato")
	private List<Viaggio> viaggios;

	public HotelSalvato() {
		super();
	}

  public HotelSalvato(HotelDTO hoteldto) 
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

	public List<Viaggio> getViaggios() {
		return this.viaggios;
	}

	public void setViaggios(List<Viaggio> viaggios) {
		this.viaggios = viaggios;
	}

	public Viaggio addViaggio(Viaggio viaggio) {
		getViaggios().add(viaggio);
		viaggio.setHotelSalvato(this);

		return viaggio;
	}

	public Viaggio removeViaggio(Viaggio viaggio) {
		getViaggios().remove(viaggio);
		viaggio.setHotelSalvato(null);

		return viaggio;
	}

}