package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;


/**
 * The persistent class for the Prenotazione database table.
 * 
 */
@Entity
@NamedQuery(name="Prenotazione.findAll", query="SELECT p FROM Prenotazione p")
public class Prenotazione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int costo;

	@Column(name="`Numero persone`")
	private int numero_persone;

	//bi-directional many-to-one association to Utente
	@ManyToOne
	@JoinColumn(name="Utente")
	private Utente utenteBean;

	//uni-directional many-to-one association to Viaggio
	@ManyToOne
	@JoinColumn(name="Viaggio")
	private Viaggio viaggioBean;

	public Prenotazione() {
	}
	
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCosto() {
		return this.costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public int getNumero_persone() {
		return this.numero_persone;
	}

	public void setNumero_persone(int numero_persone) {
		this.numero_persone = numero_persone;
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