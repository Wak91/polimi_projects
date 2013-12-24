package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the UtenteGruppo database table.
 * 
 */
@Entity
@Table(name="UtenteGruppo")
@NamedQuery(name="UtenteGruppo.findAll", query="SELECT u FROM UtenteGruppo u")
public class UtenteGruppo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String gruppo;

	//bi-directional many-to-one association to Utente
	@ManyToOne
	@JoinColumn(name="Username")
	private Utente utente;

	public UtenteGruppo() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGruppo() {
		return this.gruppo;
	}

	public void setGruppo(String gruppo) {
		this.gruppo = gruppo;
	}

	public Utente getUtente() {
		return this.utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

}