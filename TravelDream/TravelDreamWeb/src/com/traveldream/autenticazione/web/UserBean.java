package com.traveldream.autenticazione.web;

import java.util.ArrayList;

import com.sun.mail.util.LogOutputStream;
import com.traveldream.autenticazione.ejb.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;

import org.primefaces.event.RowEditEvent;

@ManagedBean(name="userBean")
@ViewScoped
public class UserBean {
	
	@EJB
	private UserMgr userMgr;
	
	private UserDTO user;
	private UserDataModel userModels;
	private ArrayList<UserDTO> filteredUsers;
	
	public UserBean() {
		user = new UserDTO();
	}
	
	public void initBean()
	{
		setUserModels(new UserDataModel(userMgr.getAllImp()));	
	}
	
	public String createUser(){
		userMgr.saveImpiegato(user);
		return "impadd.xhtml?faces-redirect=true";
	}
		

	//---------------------SETTER&GETTER USER-------------------------------------
	
	

	private void setUserModels(UserDataModel userModels) {
		this.userModels = userModels;		
	}
	
	public UserDataModel getUserModels() {
		return userModels;
	}
	
	public ArrayList<UserDTO> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(ArrayList<UserDTO> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public UserDTO getUser() {
		return user;
	}
		
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
	
	//------------------------------------------------------------------------------
	
	public void validateUsername(FacesContext context,UIComponent component,Object value) throws ValidatorException{
		if (userMgr.existUsername((String)value)){
			throw new ValidatorException(new FacesMessage("Username already used.Choose another one."));
		}
	}
	
	public String register(){
		userMgr.saveUser(user);
		return "utente/userhome?faces-redirect=true";
	}
	
	public String addImpiegato(){
		userMgr.saveImpiegato(user);
		return "admin/adminhome?faces-redirect=true";
	}
	

	public String logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return "/homepage?faces-redirect=true";
	  }
	
	
}