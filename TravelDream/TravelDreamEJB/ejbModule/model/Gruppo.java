package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Gruppo database table.
 * 
 */
@Entity
@NamedQuery(name="Gruppo.findAll", query="SELECT g FROM Gruppo g")
public class Gruppo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String nomeGruppo;

	//bi-directional many-to-many association to Utente
	@ManyToMany
	@JoinTable(
		name="UtenteGruppo"
		, joinColumns={
			@JoinColumn(name="Gruppo_NomeGruppo")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Utente_Username")
			}
		)
	private List<Utente> utentes;

	public Gruppo() {
	}

	public String getNomeGruppo() {
		return this.nomeGruppo;
	}

	public void setNomeGruppo(String nomeGruppo) {
		this.nomeGruppo = nomeGruppo;
	}

	public List<Utente> getUtentes() {
		return this.utentes;
	}

	public void setUtentes(List<Utente> utentes) {
		this.utentes = utentes;
	}

}