package com.traveldream.autenticazione.web;

import java.util.ArrayList;

import com.traveldream.autenticazione.ejb.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.annotation.PostConstruct;
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
	
	@PostConstruct
	public void initBean()
	{
		setUserModels(new UserDataModel(userMgr.getAllImp()));	
	}
	

	//---------------------SETTER&GETTER USER------------------------------------
	
	

	private void setUserModels(UserDataModel userModels) {
		this.userModels = userModels;		
	}
	
	public void saveUserModels() {
        userMgr.modifyUser(userModels.getRowData());
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
	
	public void getcurrentUser() {
		this.user=userMgr.getUserDTO();
	}
	
	public void getImpByUn(String username)
	{   
		this.user = userMgr.findImp(username);
	}
	
	public String modificaUtente(){
		userMgr.modifyUser(user);
		return "adminlist?faces-redirect=true";
	}
	
	public String register(){
		userMgr.saveUser(user);
		return "adminlist?faces-redirect=true";
	}
	
	public String addImpiegato(){
		userMgr.saveImpiegato(user);
		return "adminlist?faces-redirect=true";
	}
	

	public String logout() {
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	    return "homepage?faces-redirect=true";
	  }
	
	public String deleteUser(){
		userMgr.unregister(user);
		return "adminlist?faces-redirect=true";
		}

}