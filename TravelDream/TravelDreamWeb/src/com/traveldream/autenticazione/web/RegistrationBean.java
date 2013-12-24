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
	
	public String register(){
		userMgr.saveUser(user);
		return "utente/userhome?faces-redirect=true";
	}
	
	
	
	
}