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
@NamedQuery(name="Pacchetto.findAll", query="SELECT p FROM Pacchetto p")
public class Pacchetto implements Serializable {
	private static final long serialVersionUID = 1L;

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
	@ManyToMany
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
	@ManyToMany
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
		super();
	}
	
	public Pacchetto(PacchettoDTO packet){
		this.data_inizio=packet.getData_inizio();
		this.data_fine=packet.getData_fine();
		this.destinazione=packet.getDestinazione();
		this.immagine=packet.getPathtoImage();
		this.nome=packet.getNome();
		this.volos=EntityVoloFromVoloDTO(packet.getLista_voli()); //utilizzo il metodo EntityVoloFromVoloDTO per convertire una lista di DTO in una lista di Entity
		this.escursiones=EntityEscursioneFromEscursioneDTO(packet.getLista_escursioni());
		this.hotels=EntityHotelFromHotelDTO(packet.getLista_hotel());
		
	}
//----------------------------------Metodi per trasformare arraylist di DTO in List di Entity------------------------------------------
	private List<Volo> EntityVoloFromVoloDTO(List<VoloDTO> voli) {
		ArrayList<Volo> voliEntity=new ArrayList<Volo>();
		for (VoloDTO volo : voli) {
			voliEntity.add(new Volo(volo));
		}
		return voliEntity;
	}
	
	private List<Hotel> EntityHotelFromHotelDTO(List<HotelDTO> hotels) {
		ArrayList<Hotel> hotelsEntity=new ArrayList<Hotel>();
		for (HotelDTO hotel : hotels) {
			hotelsEntity.add(new Hotel(hotel));
		}
		return hotelsEntity;
	}
	
	private List<Escursione> EntityEscursioneFromEscursioneDTO(List<EscursioneDTO> escursioni){
		ArrayList<Escursione> escEntity=new ArrayList<Escursione>();
		for(EscursioneDTO escursioneDTO : escursioni){
			escEntity.add(new Escursione(escursioneDTO));
		}	
		return escEntity;
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