package com.traveldream.autenticazione.ejb;

import java.util.ArrayList;
import java.util.List;


public interface UserMgr {
	
	public void saveUser(UserDTO user);
	
	public void saveImpiegato(UserDTO user);
	
	public void update(UserDTO user);
	
	public void unregister();
	
	public UserDTO getUserDTO();

	public boolean existUsername(String value);

	public ArrayList<UserDTO> getAllUser();
}
