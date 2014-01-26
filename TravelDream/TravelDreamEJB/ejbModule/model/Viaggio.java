package model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Viaggio database table.
 * 
 */
@Entity
@Table(name="Viaggio")
@NamedQueries({
	@NamedQuery(name="Viaggio.findAll", query="SELECT v FROM Viaggio v"),
	@NamedQuery(name="Viaggio.findById", query="SELECT v FROM Viaggio v WHERE v.id = :id"),
})
public class Viaggio implements Serializable {
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

	//bi-directional many-to-many association to EscursioneSalvata
		@ManyToMany()
		@JoinTable(
			name="EscursioneSalvataViaggio"
			, joinColumns={
				@JoinColumn(name="Viaggio_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="EscursioneSalvata_ID")
				}
			)
		private List<EscursioneSalvata> escursioneSalvatas;

	//bi-directional many-to-one association to HotelSalvato
	@ManyToOne
	private HotelSalvato hotelSalvato;

	//bi-directional many-to-one association to VoloSalvato
	@ManyToOne
	@JoinColumn(name="VoloSalvatoAndata_ID")
	private VoloSalvato voloSalvato1;

	//uni-directional many-to-one association to VoloSalvato
	@ManyToOne
	@JoinColumn(name="VoloSalvatoRitorno_ID")
	private VoloSalvato voloSalvato2;

	public Viaggio() {
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

	public List<EscursioneSalvata> getEscursioneSalvatas() {
		return this.escursioneSalvatas;
	}

	public void setEscursioneSalvatas(List<EscursioneSalvata> escursioneSalvatas) {
		this.escursioneSalvatas = escursioneSalvatas;
	}


	public HotelSalvato getHotelSalvato() {
		return this.hotelSalvato;
	}

	public void setHotelSalvato(HotelSalvato hotelSalvato) {
		this.hotelSalvato = hotelSalvato;
	}

	public VoloSalvato getVoloSalvato1() {
		return this.voloSalvato1;
	}

	public void setVoloSalvato1(VoloSalvato voloSalvato1) {
		this.voloSalvato1 = voloSalvato1;
	}

	public VoloSalvato getVoloSalvato2() {
		return this.voloSalvato2;
	}

	public void setVoloSalvato2(VoloSalvato voloSalvato2) {
		this.voloSalvato2 = voloSalvato2;
	}

}