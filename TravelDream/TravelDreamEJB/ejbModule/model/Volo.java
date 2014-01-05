package model;

import java.io.Serializable;

import javax.persistence.*;

import com.traveldream.gestionecomponente.ejb.VoloDTO;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the Volo database table.
 * 
 */
@Entity
@NamedQuery(name="Volo.findAll", query="SELECT v FROM Volo v")
public class Volo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String compagnia;

	private int costo;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	private String immagine;

	@Column(name="`Luogo arrivo`")
	private String luogo_arrivo;

	@Column(name="`Luogo partenza`")
	private String luogo_partenza;

	//bi-directional many-to-many association to Pacchetto
	@ManyToMany
	@JoinTable(
		name="VoloPacchetto"
		, joinColumns={
			@JoinColumn(name="Voli_ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="Pacchetto_ID")
			}
		)
	private List<Pacchetto> pacchettos;

    public Volo() {
    super();
  }
  
  public Volo(VoloDTO volodto)
  {
     this.compagnia = volodto.getCompagnia();
     this.costo = volodto.getCosto();
     this.data = volodto.getData();
     this.luogo_arrivo = volodto.getLuogo_arrivo();
     this.luogo_partenza = volodto.getLuogo_partenza();
     this.immagine = ""; // da sistemare 
  }
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompagnia() {
		return this.compagnia;
	}

	public void setCompagnia(String compagnia) {
		this.compagnia = compagnia;
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

	public String getLuogo_arrivo() {
		return this.luogo_arrivo;
	}

	public void setLuogo_arrivo(String luogo_arrivo) {
		this.luogo_arrivo = luogo_arrivo;
	}

	public String getLuogo_partenza() {
		return this.luogo_partenza;
	}

	public void setLuogo_partenza(String luogo_partenza) {
		this.luogo_partenza = luogo_partenza;
	}

	public List<Pacchetto> getPacchettos() {
		return this.pacchettos;
	}

	public void setPacchettos(List<Pacchetto> pacchettos) {
		this.pacchettos = pacchettos;
	}

}