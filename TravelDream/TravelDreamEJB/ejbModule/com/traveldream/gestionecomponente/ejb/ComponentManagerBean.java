package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Escursione;
import model.Hotel;
import model.Volo;

/**
 * Session Bean implementation class ComponentManagerBean
 */
@Stateless
public class ComponentManagerBean implements ComponentManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	/**
	 * creo un hotel e genero un ID univoco (controllo sulle date fatto a monte da pagina web )
	 */
	
	public void saveHotel(HotelDTO hoteldto) {

		Hotel hotel = new Hotel(hoteldto);	//aggiungo alla tabella Utente una tupla utilizzanto il DTO
		em.persist(hotel);	
	}
	
	@Override
	public void saveVolo(VoloDTO volodto) {
		Volo volo = new Volo(volodto);
		em.persist(volo);
		
	}
	
	@Override
	public void saveEscursione(EscursioneDTO escursionedto) {
		Escursione escursione= new Escursione(escursionedto);
		em.persist(escursione);
		
	}
	
	public void update() {
	}

	public void remove() {
	}
    
	//sfrutto la named query per ritornare tutti gli hotel dal DB
	public ArrayList<Hotel> getAllHotel()
	{return null;
	}

	


	
}
