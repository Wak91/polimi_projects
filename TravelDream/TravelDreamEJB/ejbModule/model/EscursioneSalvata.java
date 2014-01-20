package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;

import java.util.Date;


/**
 * The persistent class for the EscursioneSalvata database table.
 * 
 */
@Entity
@NamedQuery(name="EscursioneSalvata.findAll", query="SELECT e FROM EscursioneSalvata e")
public class EscursioneSalvata implements Serializable {
	private static final long serialVersionUID = 1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	@Id
	private int id;

	private int costo;

	@Temporal(TemporalType.DATE)
	private Date data;

	private String immagine;

	private String luogo;

	private String nome;

	//bi-directional many-to-one association to Viaggio
	@ManyToOne
	private Viaggio viaggio;

	public EscursioneSalvata() {
		
		super();
	}
	
	public EscursioneSalvata(EscursioneDTO escursionedto)
	{
	    this.costo = escursionedto.getCosto();
	    this.data = escursionedto.getData();
	    this.immagine = escursionedto.getImmagine(); 
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

	public Viaggio getViaggio() {
		return this.viaggio;
	}

	public void setViaggio(Viaggio viaggio) {
		this.viaggio = viaggio;
	}

}