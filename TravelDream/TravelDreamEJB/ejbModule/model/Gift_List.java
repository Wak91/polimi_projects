package model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the `Gift List` database table.
 * 
 */
@Entity
@Table(name="`Gift List`")
@NamedQuery(name="Gift_List.findAll", query="SELECT g FROM Gift_List g")
public class Gift_List implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private byte hotelPag;

	private byte voloAPag;

	private byte voloRPag;

	//uni-directional many-to-many association to Escursione
	@ManyToMany()
	@JoinTable(
		name="Gift ListEscursione"
		, joinColumns={
			@JoinColumn(name="Gift List_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Escursione_ID")
			}
		)
	private List<Escursione> escursiones;

	//bi-directional many-to-many association to Amico
	@ManyToMany()
	@JoinTable(
			name="AmicoGift List"
			, joinColumns={
				@JoinColumn(name="Gift List_ID")
				}
			, inverseJoinColumns={
				@JoinColumn(name="Amico_ID")
				}
			)
	private List<Amico> amicos;

	//bi-directional many-to-one association to Utente
	@ManyToOne
	private Utente utente;

	//uni-directional many-to-one association to Viaggio
	@ManyToOne
	private Viaggio viaggio;

	public Gift_List() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getHotelPag() {
		return this.hotelPag;
	}

	public void setHotelPag(byte hotelPag) {
		this.hotelPag = hotelPag;
	}

	public byte getVoloAPag() {
		return this.voloAPag;
	}

	public void setVoloAPag(byte voloAPag) {
		this.voloAPag = voloAPag;
	}

	public byte getVoloRPag() {
		return this.voloRPag;
	}

	public void setVoloRPag(byte voloRPag) {
		this.voloRPag = voloRPag;
	}

	public List<Escursione> getEscursiones() {
		return this.escursiones;
	}

	public void setEscursiones(List<Escursione> escursiones) {
		this.escursiones = escursiones;
	}

	public List<Amico> getAmicos() {
		return this.amicos;
	}

	public void setAmicos(List<Amico> amicos) {
		this.amicos = amicos;
	}

	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public Viaggio getViaggio() {
		return this.viaggio;
	}

	public void setViaggio(Viaggio viaggio) {
		this.viaggio = viaggio;
	}

}