package model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the EscursionePagata database table.
 * 
 */
@Entity
@NamedQuery(name="EscursionePagata.findAll", query="SELECT e FROM EscursionePagata e")
public class EscursionePagata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private byte escPagata;

	@Column(name="`Gift List_ID`")
	private int gift_List_ID;

	//uni-directional many-to-one association to Escursione
	@ManyToOne
	private Escursione escursione;

	//bi-directional many-to-one association to Gift_List
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="Gift List_ID", referencedColumnName="ID"),
		})
	private Gift_List giftList;

	public EscursionePagata() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte getEscPagata() {
		return this.escPagata;
	}

	public void setEscPagata(byte escPagata) {
		this.escPagata = escPagata;
	}

	public int getGift_List_ID() {
		return this.gift_List_ID;
	}

	public void setGift_List_ID(int gift_List_ID) {
		this.gift_List_ID = gift_List_ID;
	}

	public Escursione getEscursione() {
		return this.escursione;
	}

	public void setEscursione(Escursione escursione) {
		this.escursione = escursione;
	}

	public Gift_List getGiftList() {
		return this.giftList;
	}

	public void setGiftList(Gift_List giftList) {
		this.giftList = giftList;
	}

}