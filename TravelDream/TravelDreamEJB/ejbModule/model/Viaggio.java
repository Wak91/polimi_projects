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
@NamedQuery(name="Viaggio.findAll", query="SELECT v FROM Viaggio v")
public class Viaggio implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data fine`")
	private Date data_fine;

	@Temporal(TemporalType.DATE)
	@Column(name="`Data inizio`")
	private Date data_inizio;

	//bi-directional many-to-many association to Escursione
	@ManyToMany
	@JoinTable(
		name="EscursioneViaggio"
		, joinColumns={
			@JoinColumn(name="Viaggio_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Escursioni_ID")
			}
		)
	private List<Escursione> escursiones;

	//uni-directional many-to-one association to Hotel
	@ManyToOne
	@JoinColumn(name="Hotel")
	private Hotel hotelBean;

	//uni-directional many-to-one association to Volo
	@ManyToOne
	@JoinColumn(name="VoloAndata")
	private Volo volo1;

	//uni-directional many-to-one association to Volo
	@ManyToOne
	@JoinColumn(name="VoloRitorno")
	private Volo volo2;

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

	public List<Escursione> getEscursiones() {
		return this.escursiones;
	}

	public void setEscursiones(List<Escursione> escursiones) {
		this.escursiones = escursiones;
	}

	public Hotel getHotelBean() {
		return this.hotelBean;
	}

	public void setHotelBean(Hotel hotelBean) {
		this.hotelBean = hotelBean;
	}

	public Volo getVolo1() {
		return this.volo1;
	}

	public void setVolo1(Volo volo1) {
		this.volo1 = volo1;
	}

	public Volo getVolo2() {
		return this.volo2;
	}

	public void setVolo2(Volo volo2) {
		this.volo2 = volo2;
	}

}