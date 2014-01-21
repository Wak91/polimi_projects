package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the Amico database table.
 * 
 */
@Entity
@NamedQuery(name="Amico.findAll", query="SELECT a FROM Amico a")
public class Amico implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String amico;

	public Amico() {
	super();}
	
	public Amico(String mail) {
		amico=mail;
	}
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAmico() {
		return this.amico;
	}

	public void setAmico(String amico) {
		this.amico = amico;
	}

}