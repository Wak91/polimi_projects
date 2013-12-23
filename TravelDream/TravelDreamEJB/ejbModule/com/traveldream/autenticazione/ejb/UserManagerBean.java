package com.traveldream.autenticazione.ejb;

import java.util.ArrayList;

import model.*;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class UserManagerBean implements UserMgr {
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	

	@Override
	public void saveUser(UserDTO userdto) {
		Utente  user = new Utente(userdto);
		UtenteGruppo usergroup = new UtenteGruppo();
		usergroup.setGruppo("");
	
		
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
