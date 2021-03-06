package model;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the EscursionePagata database table.
 * 
 */
@Entity
@Table(name="EscursionePagata")
@NamedQuery(name="EscursionePagata.findAll", query="SELECT e FROM EscursionePagata e")
public class EscursionePagata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private boolean pagata;

	//bi-directional many-to-one association to Gift_List
	@ManyToOne
    @JoinColumn(name="GiftList_ID", referencedColumnName="ID")
	private Gift_List giftList;

	//uni-directional many-to-one association to EscursioneSalvata
	@ManyToOne
	private EscursioneSalvata escursioneSalvata;

	public EscursionePagata() {
		escursioneSalvata= new EscursioneSalvata();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean getPagata() {
		return this.pagata;
	}

	public void setPagata(boolean pagata) {
		this.pagata = pagata;
	}

	public Gift_List getGiftList() {
		return this.giftList;
	}

	public void setGiftList(Gift_List giftList) {
		this.giftList = giftList;
	}

	public EscursioneSalvata getEscursioneSalvata() {
		return this.escursioneSalvata;
	}

	public void setEscursioneSalvata(EscursioneSalvata escursioneSalvata) {
		this.escursioneSalvata = escursioneSalvata;
	}

}