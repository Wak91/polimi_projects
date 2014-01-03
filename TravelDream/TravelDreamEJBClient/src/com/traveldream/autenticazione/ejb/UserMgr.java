package com.traveldream.autenticazione.ejb;


public interface UserMgr {
	
	public void saveUser(UserDTO user);
	
	public void saveImpiegato(UserDTO user);
	
	public void update(UserDTO user);
	
	public void unregister();
	
	public UserDTO getUserDTO();

	public boolean existUsername(String value);
}
