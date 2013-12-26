package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;

import java.util.Date;


/**
 * The persistent class for the Escursione database table.
 * 
 */
//Entity per escursione
@Entity
@Table(name = "Escursione")
@NamedQuery(name="Escursione.findAll", query="SELECT e FROM Escursione e")
public class Escursione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="Costo")
	private int costo;

	@Temporal(TemporalType.DATE)
	@Column(name="Data")
	private Date data;

	@Column(name="Immagine")
	private String immagine;

	@Column(name="Luogo")
	private String luogo;

	@Column(name="Nome")
	private String nome;

	public Escursione() {
		super();
	}
	
	public Escursione(EscursioneDTO escursionedto)
	{
		this.costo = escursionedto.getCosto();
		this.data = escursionedto.getData();
		this.immagine = ""; // da sistemare 
		this.luogo = escursionedto.getLuogo();
		this.nome = escursionedto.getNome();
		
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

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getImmagine() {
		return this.immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public String getLuogo() {
		return this.luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}