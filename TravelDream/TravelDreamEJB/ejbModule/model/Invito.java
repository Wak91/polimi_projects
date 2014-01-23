package model;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.persistence.*;

import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;


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

	private boolean status;

	//bi-directional many-to-one association to Utente
	@ManyToOne
	@JoinColumn(name="Utente")
	private Utente utenteBean;

	//uni-directional many-to-one association to Viaggio
	@ManyToOne
	@JoinColumn(name="Viaggio")
	private Viaggio viaggioBean;
	

	public Invito() {
		super();
	}
	
	public Invito(InvitoDTO invito, Utente utente, Viaggio viaggio){
		this.id = invito.getId();
		this.amico = invito.getAmico();
		this.status = invito.getStatus();
		this.utenteBean = utente;
		this.viaggioBean = viaggio;
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

	public boolean getStatus() {
		return this.status;
	}

	public void setStatus(boolean status) {
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