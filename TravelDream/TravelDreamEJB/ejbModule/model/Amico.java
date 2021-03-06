package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the Amico database table.
 * 
 */
@Entity
@Table(name="Amico")
@NamedQuery(name="Amico.findAll", query="SELECT a FROM Amico a")
public class Amico implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
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