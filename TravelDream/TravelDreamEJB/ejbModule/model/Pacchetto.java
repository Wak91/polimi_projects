package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import com.traveldream.gestionepack.ejb.PacchettoDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * The persistent class for the Pacchetto database table.
 * 
 */
@Entity
@Table(name="Pacchetto")
@NamedQueries ( 
		     {
@NamedQuery(name="Pacchetto.findAll", query="SELECT p FROM Pacchetto p"),
		     }
              )
public class Pacchetto implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data fine`")
	private Date data_fine;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data inizio`")
	private Date data_inizio;

	private String destinazione;

	private String immagine;

	private String nome;

	//bi-directional many-to-many association to Escursione
	@ManyToMany()
	@JoinTable(
		name="EscursionePacchetto"
		, joinColumns={
			@JoinColumn(name="Pacchetto_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Escursioni_ID")
			}
		)
	private List<Escursione> escursiones;

	//bi-directional many-to-many association to Hotel
	@ManyToMany()
	@JoinTable(
		name="HotelPacchetto"
		, joinColumns={
			@JoinColumn(name="Pacchetto_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Hotel_ID")
			}
		)
	private List<Hotel> hotels;

	//bi-directional many-to-many association to Volo
	@ManyToMany(mappedBy="pacchettos")
	private List<Volo> volos;

	public Pacchetto() {
		hotels = new ArrayList<Hotel>();
		volos = new ArrayList<Volo>();
		escursiones = new ArrayList<Escursione>();
	}

	
	public void addHotel(Hotel hotel){
		hotels.add(hotel);
	}
	
	public void addEscursione(Escursione esc){
		escursiones.add(esc);
	}
	
	public void addVolo(Volo volo){
		volos.add(volo);
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDestinazione() {
		return this.destinazione;
	}

	public void setDestinazione(String destinazione) {
		this.destinazione = destinazione;
	}

	public String getImmagine() {
		return this.immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Escursione> getEscursiones() {
		return this.escursiones;
	}

	public void setEscursiones(List<Escursione> escursiones) {
		this.escursiones = escursiones;
	}

	public List<Hotel> getHotels() {
		return this.hotels;
	}

	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}

	public List<Volo> getVolos() {
		return this.volos;
	}

	public void setVolos(List<Volo> volos) {
		this.volos = volos;
	}

}