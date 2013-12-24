package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the Volo database table.
 * 
 */
@Entity
@NamedQuery(name="Volo.findAll", query="SELECT v FROM Volo v")
public class Volo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ID")
	private int id;

	@Column(name="Compagnia")
	private String compagnia;

	@Column(name="Costo")
	private int costo;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="Data")
	private Date data;

	@Column(name="`Luogo arrivo`")
	private String luogo_arrivo;

	@Column(name="`Luogo partenza`")
	private String luogo_partenza;

	public Volo() {
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

}