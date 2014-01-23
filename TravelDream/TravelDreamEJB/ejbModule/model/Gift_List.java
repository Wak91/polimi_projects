package model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the `Gift List` database table.
 * 
 */
@Entity
@Table(name="GiftList")
@NamedQueries ( 
	       {
	    	   @NamedQuery(name="Gift_List.findbyhash", query="SELECT g FROM Gift_List g WHERE g.hash  = :h"),
	    	   @NamedQuery(name="Gift_List.findbyuser", query="SELECT g FROM Gift_List g WHERE g.utente  = :u"),
	    	   @NamedQuery(name="Gift_List.findAll", query="SELECT g FROM Gift_List g")
	       }
	       )
public class Gift_List implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Id
	private int id;

	private boolean hotelPag;
	
	private boolean voloAPag;
	
	private boolean voloRPag;
	
	private String hash;

	//bi-directional many-to-one association to EscursionePagata
	@OneToMany(mappedBy="giftList",cascade = {CascadeType.PERSIST,
            CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE})
	private List<EscursionePagata> escursionePagatas;

	//bi-directional many-to-many association to Amico
	  @ManyToMany(cascade = {CascadeType.PERSIST,
	            CascadeType.MERGE,CascadeType.REFRESH,CascadeType.REMOVE})
	  @JoinTable(
	      name="AmicoGiftList"
	      , joinColumns={
	        @JoinColumn(name="`GiftList_ID`")
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
		amicos = new ArrayList<Amico>();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public List<EscursionePagata> getEscursionePagatas() {
		return this.escursionePagatas;
	}

	public void setEscursionePagatas(List<EscursionePagata> escursionePagatas) {
		this.escursionePagatas = escursionePagatas;
	}

	public EscursionePagata addEscursionePagata(EscursionePagata escursionePagata) {
		getEscursionePagatas().add(escursionePagata);
		escursionePagata.setGiftList(this);

		return escursionePagata;
	}

	public EscursionePagata removeEscursionePagata(EscursionePagata escursionePagata) {
		getEscursionePagatas().remove(escursionePagata);
		escursionePagata.setGiftList(null);

		return escursionePagata;
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

	public boolean isHotelPag() {
		return hotelPag;
	}

	public void setHotelPag(boolean hotelPag) {
		this.hotelPag = hotelPag;
	}

	public boolean isVoloAPag() {
		return voloAPag;
	}

	public void setVoloAPag(boolean voloAPag) {
		this.voloAPag = voloAPag;
	}

	public boolean isVoloRPag() {
		return voloRPag;
	}

	public void setVoloRPag(boolean voloRPag) {
		this.voloRPag = voloRPag;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}
	
	

}