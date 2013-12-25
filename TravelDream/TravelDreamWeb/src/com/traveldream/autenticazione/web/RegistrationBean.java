package com.traveldream.autenticazione.web;

import com.traveldream.autenticazione.ejb.*;

import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="registrationBean")
@RequestScoped
public class RegistrationBean {

	private UserDTO user;
	
	@EJB
	private UserMgr userMgr;

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public RegistrationBean() {
		user = new UserDTO();
	}

	public UserDTO getUser() {
		return user;
	}
	
	//queste get possono andare bene qua o dobbiamo organizzare strettamente i bean come da boundary diagram???
	
	public String getUserName()
	{
		return userMgr.getUserDTO().getUsername();
	}
	
	public String getUserFirstName()
	{
		return userMgr.getUserDTO().getFirstName();
	}
	
	public String getUserLastName()
	{
		return userMgr.getUserDTO().getLastName();
	}
	
	public String register(){
		userMgr.saveUser(user);
		return "utente/userhome?faces-redirect=true";
	}
	
	
	
	
}