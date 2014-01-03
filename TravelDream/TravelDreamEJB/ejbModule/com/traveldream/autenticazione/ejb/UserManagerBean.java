package com.traveldream.autenticazione.ejb;

import model.*;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/*
 * Questo è il bean che gestisce
 * gli utenti 
 * */
@Stateless
public class UserManagerBean implements UserMgr {
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	/**
	 * creo un utente e gli associo il gruppo UTENTE
	 */
	@Override
	public void saveUser(UserDTO userdto) {

		Utente  user = new Utente(userdto);	//aggiungo alla tabella Utente una tupla utilizzanto il DTO
		
		UtenteGruppo usergroup = new UtenteGruppo();	//aggiungo una tupla a UtenteGruppo settando manualmente i parametri
		usergroup.setGruppo(Group._UTENTE);	
		usergroup.setUtente(user);
	
		em.persist(user);
		em.persist(usergroup);
		
	}
	
	

	@Override
	public void update(UserDTO user) {
		

	}

	@Override
	public void unregister() {
		// TODO Auto-generated method stub

	}

	@Override
	public UserDTO getUserDTO() {
		UserDTO userDTO = convertToDTO(getPrincipalUser());
		return userDTO;
	}
	
	private UserDTO convertToDTO(Utente user) {
		UserDTO udto = new UserDTO();
		udto.setUsername(user.getUsername());
		udto.setFirstName(user.getNome());
		udto.setLastName(user.getCognome());
		udto.setEmail(user.getEmail());
		return udto;
	}

	//---Per la ricerca---
	
    public Utente getPrincipalUser()
    {
    	return find(getPrincipalUsername());
    }


	public String getPrincipalUsername()
	{
		return context.getCallerPrincipal().getName(); // ritorna la chiave specificata nel reame ( USERNAME )
	}
	
	private Utente find(String pusername) {
		return em.find(Utente.class, pusername);
	}

	public boolean existUsername(String username) {
		if (em.find(Utente.class,username)!=null){
			return true;
		}
			
		return false;
	}



	@Override
	public void saveImpiegato(UserDTO userdto) {
		// TODO Auto-generated method stub
		
		Utente  user = new Utente(userdto);	//aggiungo alla tabella Utente una tupla utilizzanto il DTO
		
		UtenteGruppo usergroup = new UtenteGruppo();	//aggiungo una tupla a UtenteGruppo settando manualmente i parametri
		usergroup.setGruppo(Group._IMPIEGATO);	
		usergroup.setUtente(user);
	
		em.persist(user);
		em.persist(usergroup);
		
	}

}
