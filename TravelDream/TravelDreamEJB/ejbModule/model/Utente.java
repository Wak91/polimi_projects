package model;

import java.io.Serializable;
import com.traveldream.autenticazione.ejb.*;
import javax.persistence.*;
import java.util.Date;
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

	@Temporal(TemporalType.DATE)
	@Column(name="`Data di nascita`")
	private Date data_di_nascita;

	private String email;

	private String nome;

	private String password;

	//bi-directional many-to-one association to UtenteGruppo
	@OneToMany(mappedBy="utente")
	private List<UtenteGruppo> utenteGruppos;

	public Utente() {
		super();
	}
	
	 public Utente(UserDTO user) {
         this.username = user.getUsername();
         this.cognome = user.getLastName();
         this.nome = user.getFirstName();
         this.email = user.getEmail();
         this.data_di_nascita = user.getData();
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

	public Date getData_di_nascita() {
		return this.data_di_nascita;
	}

	public void setData_di_nascita(Date data_di_nascita) {
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

	public List<UtenteGruppo> getUtenteGruppos() {
		return this.utenteGruppos;
	}

	public void setUtenteGruppos(List<UtenteGruppo> utenteGruppos) {
		this.utenteGruppos = utenteGruppos;
	}

	public UtenteGruppo addUtenteGruppo(UtenteGruppo utenteGruppo) {
		getUtenteGruppos().add(utenteGruppo);
		utenteGruppo.setUtente(this);

		return utenteGruppo;
	}

	public UtenteGruppo removeUtenteGruppo(UtenteGruppo utenteGruppo) {
		getUtenteGruppos().remove(utenteGruppo);
		utenteGruppo.setUtente(null);

		return utenteGruppo;
	}

}