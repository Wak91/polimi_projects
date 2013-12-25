package com.traveldream.gestionecomponente.web;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.traveldream.gestionecomponente.ejb.ComponentManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.HotelDTO;

@ManagedBean(name="ComponentBean") 
@RequestScoped
public class ComponentBean {

    private HotelDTO hotel;
	
	@EJB
	private ComponentManagerBeanLocal CMB;

	public ComponentBean() {
		hotel = new HotelDTO();
	}

	public void setHotel(HotelDTO hoteldto) {
		this.hotel = hoteldto;
	}

	public HotelDTO getHotel() {
		return hotel;
	}
	
	public String createHotel(){
		CMB.saveHotel(hotel);
		return "impadd.xhtml?faces-redirect=true";
	}
	
}
