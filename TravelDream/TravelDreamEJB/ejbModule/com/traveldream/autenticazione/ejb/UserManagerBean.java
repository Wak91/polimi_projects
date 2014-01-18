package com.traveldream.autenticazione.ejb;

import java.util.ArrayList;
import java.util.List;

import model.*;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIConversion.User;
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
	
	
	public Utente find(String pusername) {
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


		public ArrayList<UserDTO> getAllImp() {
		List<Utente> myList;
		ArrayList <UserDTO> myDTOlist = new ArrayList <UserDTO> ();
		Query q = em.createNamedQuery("Utente.findAll", Utente.class);
		myList = q.getResultList();
		for (Utente u : myList){
			for(int i = 0; i < u.getUtenteGruppos().size(); i++){
				if (u.getUtenteGruppos().get(i).getGruppo().equals("IMPIEGATO"))
					myDTOlist.add(this.convertToDTO(u));
			}
		}
		return myDTOlist;
				
	}


	//Risolvere problema se cambio username non trova nulla
	@Override
	public void modifyUser(UserDTO user) {
		Utente result = em.createNamedQuery("Utente.findImp", Utente.class).setParameter("username", user.getUsername()).getSingleResult();
		result.setNome(user.getFirstName());
		result.setCognome(user.getLastName());
		result.setData_di_nascita(user.getData());
		result.setEmail(user.getEmail());
		result.setPassword(user.getPassword());
		em.merge(result);
		
	}

	@Override
	public void modifyUser(UserDTO user, String username) {
		// TODO Auto-generated method stub
		Utente result = em.createNamedQuery("Utente.findImp", Utente.class).setParameter("username", username).getSingleResult();
		result.setUsername(user.getUsername());
		result.setNome(user.getFirstName());
		result.setCognome(user.getLastName());
		result.setData_di_nascita(user.getData());
		result.setEmail(user.getEmail());
		result.setPassword(user.getPassword());
		em.merge(result);
		
	}


	@Override
	public UserDTO findImp(String username) {
		Utente result;
		result = em.createNamedQuery("Utente.findImp", Utente.class).setParameter("username", username).getSingleResult();
		return convertToDTO(result);
	}



	

}
