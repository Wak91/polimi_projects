package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Escursione database table.
 * 
 */
@Entity
@Table(name="Escursione")
@NamedQuery(name="Escursione.findAll", query="SELECT e FROM Escursione e")
public class Escursione implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private int costo;

	@Temporal(TemporalType.DATE)
	private Date data;

	private String immagine;

	private String luogo;

	private String nome;

	//bi-directional many-to-many association to Pacchetto
	@ManyToMany(mappedBy="escursiones",cascade = CascadeType.PERSIST)
	private List<Pacchetto> pacchettos;

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

  	public void addPacchetto(Pacchetto pacchetto){
  		pacchettos.add(pacchetto);
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

	public List<Pacchetto> getPacchettos() {
		return this.pacchettos;
	}

	public void setPacchettos(List<Pacchetto> pacchettos) {
		this.pacchettos = pacchettos;
	}

}