package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Invito database table.
 * 
 */
@Entity
@NamedQuery(name="Invito.findAll", query="SELECT i FROM Invito i")
public class Invito implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String amico;

	private byte status;

	//bi-directional many-to-one association to Utente
	@ManyToOne
	@JoinColumn(name="Utente")
	private Utente utenteBean;

	//uni-directional many-to-one association to Viaggio
	@ManyToOne
	@JoinColumn(name="Viaggio")
	private Viaggio viaggioBean;

	public Invito() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAmico() {
		return this.amico;
	}

	public void setAmico(String amico) {
		this.amico = amico;
	}

	public byte getStatus() {
		return this.status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}

	public Utente getUtenteBean() {
		return this.utenteBean;
	}

	public void setUtenteBean(Utente utenteBean) {
		this.utenteBean = utenteBean;
	}

	public Viaggio getViaggioBean() {
		return this.viaggioBean;
	}

	public void setViaggioBean(Viaggio viaggioBean) {
		this.viaggioBean = viaggioBean;
	}

}