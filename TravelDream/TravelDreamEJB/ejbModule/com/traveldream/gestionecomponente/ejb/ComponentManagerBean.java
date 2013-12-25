package com.traveldream.gestionecomponente.ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Hotel;


import com.traveldream.autenticazione.ejb.UserDTO;

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
	

	public void update(UserDTO user) {
		

	}

	public void remove() {
		// TODO Auto-generated method stub

	}

	
	public HotelDTO getHotelDTO() {
		return null;
		
	}
	
	private HotelDTO convertToDTO(Hotel hotel) {
		return null;
	}
}
