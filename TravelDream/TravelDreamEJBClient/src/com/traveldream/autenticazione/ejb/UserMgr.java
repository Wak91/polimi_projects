package com.traveldream.autenticazione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

@Local
public interface UserMgr {
	
	public UserDTO findImp(String username);

	public void saveUser(UserDTO user);
	
	public void saveImpiegato(UserDTO user);
	
	public void modifyUser(UserDTO user);
	
	public void modifyUser(UserDTO user, String username);

	
	public void unregister();
	
	public UserDTO getUserDTO();
	
	public <Utente> Utente find(String un);

	public boolean existUsername(String value);

	public ArrayList<UserDTO> getAllImp();


}
