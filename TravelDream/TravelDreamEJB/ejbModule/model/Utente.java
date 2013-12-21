package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the Utente database table.
 * 
 */
@Entity
@NamedQuery(name="Utente.findAll", query="SELECT u FROM Utente u")
public class Utente implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String username;

	private String cognome;

	@Column(name="`Data di nascita`")
	private String data_di_nascita;

	private String email;

	private String nome;

	private String password;

	//bi-directional many-to-many association to Gruppo
	@ManyToMany(mappedBy="utentes")
	private List<Gruppo> gruppos;

	public Utente() {
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getData_di_nascita() {
		return this.data_di_nascita;
	}

	public void setData_di_nascita(String data_di_nascita) {
		this.data_di_nascita = data_di_nascita;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Gruppo> getGruppos() {
		return this.gruppos;
	}

	public void setGruppos(List<Gruppo> gruppos) {
		this.gruppos = gruppos;
	}

}