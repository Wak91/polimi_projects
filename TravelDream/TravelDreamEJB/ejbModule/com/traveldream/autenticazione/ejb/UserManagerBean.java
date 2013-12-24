package com.traveldream.autenticazione.ejb;

import model.*;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class UserManagerBean implements UserMgr {
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	/**
	 * creo un utente e gli associo il gruppo USER
	 */
	@Override
	public void saveUser(UserDTO userdto) {
		System.out.println("salvo utente");

		Utente  user = new Utente(userdto);	//aggiungo alla tabella Utente una tupla utilizzanto il DTO
		
		UtenteGruppo usergroup = new UtenteGruppo();	//aggiungo una tupla a UtenteGruppo settando manualmente i parametri
		usergroup.setGruppo(Group._UTENTE);	
		usergroup.setUtente(user);
	
		em.persist(user);
		em.persist(usergroup);
		
	}
	
	

	@Override
	public void update(UserDTO user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregister() {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO getUserDTO() {
		// TODO Auto-generated method stub
		return null;
	}

}
