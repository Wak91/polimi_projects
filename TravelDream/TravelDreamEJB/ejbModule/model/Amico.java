package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


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

	//bi-directional many-to-many association to Gift_List
	@ManyToMany(mappedBy="amicos")
	private List<Gift_List> giftLists;

	public Amico() {
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

	public List<Gift_List> getGiftLists() {
		return this.giftLists;
	}

	public void setGiftLists(List<Gift_List> giftLists) {
		this.giftLists = giftLists;
	}

}