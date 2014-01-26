package model;

import java.io.Serializable;

import javax.persistence.*;

import org.apache.commons.codec.digest.DigestUtils;

import com.traveldream.autenticazione.ejb.UserDTO;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Utente database table.
 * 
 */
@Entity
@Table(name="Utente")
@NamedQueries
			(
				{	
					@NamedQuery(name="Utente.findAll", query="SELECT u FROM Utente u"),
					@NamedQuery(name="Utente.findImp", query="SELECT u FROM Utente u WHERE u.username = :username")
				}
			)
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

	//bi-directional many-to-one association to Gift_List
	@OneToMany(mappedBy="utente")
	private List<Gift_List> giftLists;

	//bi-directional many-to-one association to Invito
	@OneToMany(mappedBy="utenteBean")
	private List<Invito> invitos;

	//bi-directional many-to-one association to Prenotazione
	@OneToMany(mappedBy="utenteBean")
	private List<Prenotazione> prenotaziones;

	//bi-directional many-to-one association to UtenteGruppo
	@OneToMany(mappedBy="utente", cascade = CascadeType.REMOVE)
	private List<UtenteGruppo> utenteGruppos;

	public Utente() {
		super();
	}
	 
	public Utente(UserDTO user) {
          this.username = user.getUsername();
          this.cognome = user.getLastName();
          this.nome = user.getFirstName();
          this.password = DigestUtils.sha512Hex(user.getPassword() );
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
		this.password = DigestUtils.sha512Hex(password);
	}

	public List<Gift_List> getGiftLists() {
		return this.giftLists;
	}

	public void setGiftLists(List<Gift_List> giftLists) {
		this.giftLists = giftLists;
	}

	public Gift_List addGiftList(Gift_List giftList) {
		getGiftLists().add(giftList);
		giftList.setUtente(this);

		return giftList;
	}

	public Gift_List removeGiftList(Gift_List giftList) {
		getGiftLists().remove(giftList);
		giftList.setUtente(null);

		return giftList;
	}

	public List<Invito> getInvitos() {
		return this.invitos;
	}

	public void setInvitos(List<Invito> invitos) {
		this.invitos = invitos;
	}

	public Invito addInvito(Invito invito) {
		getInvitos().add(invito);
		invito.setUtenteBean(this);

		return invito;
	}

	public Invito removeInvito(Invito invito) {
		getInvitos().remove(invito);
		invito.setUtenteBean(null);

		return invito;
	}

	public List<Prenotazione> getPrenotaziones() {
		return this.prenotaziones;
	}

	public void setPrenotaziones(List<Prenotazione> prenotaziones) {
		this.prenotaziones = prenotaziones;
	}

	public Prenotazione addPrenotazione(Prenotazione prenotazione) {
		getPrenotaziones().add(prenotazione);
		prenotazione.setUtenteBean(this);

		return prenotazione;
	}

	public Prenotazione removePrenotazione(Prenotazione prenotazione) {
		getPrenotaziones().remove(prenotazione);
		prenotazione.setUtenteBean(null);

		return prenotazione;
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