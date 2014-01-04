package com.traveldream.gestionecomponente.web;


import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.traveldream.gestionecomponente.ejb.ComponentManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

@ManagedBean(name="ComponentBean") 
@RequestScoped
public class ComponentBean {

	@EJB
	private ComponentManagerBeanLocal CMB;
	
    private HotelDTO hotel;
    private VoloDTO  volo;
    private EscursioneDTO escursione;
	
    private ArrayList<HotelDTO> listaHotel;
    
	public EscursioneDTO getEscursione() {
		return escursione;
	}

	public void setEscursione(EscursioneDTO escursione) {
		this.escursione = escursione;
	}

	public ComponentBean() {
		hotel = new HotelDTO();
		volo  = new VoloDTO();
		escursione = new EscursioneDTO();
		//listaHotel = CMB.getAllHotel(); //viene presa dal db la lista degli hotel presenti
	}

	public VoloDTO getVolo() {
		return volo;
	}

	public void setVolo(VoloDTO volo) {
		this.volo = volo;
	}

	public void setHotel(HotelDTO hoteldto) {
		this.hotel = hoteldto;
	}

	public HotelDTO getHotel() {
		return hotel;
	}
	
	public void validate_Date(FacesContext context,UIComponent component,Object value) throws ValidatorException{
		UIInput datainizio = (UIInput)component.getAttributes().get("dates");
		Date dataInizio = (Date)datainizio.getValue();
		Date dataFine = (Date)value;
		if (dataFine.before(dataInizio)){
                throw new ValidatorException(new FacesMessage("La data di fine validita' deve essere successiva a quella di inizio"));
        }
	}
	
	//---Creazione componenti---
	public String createHotel(){
		CMB.saveHotel(hotel);
		return "impadd.xhtml?faces-redirect=true";
	}
	
	public String createVolo(){
		CMB.saveVolo(volo);
		return "impadd.xhtml?faces-redirect=true";
	}
	
	public String createEscursione(){
		CMB.saveEscursione(escursione);
		return "impadd.xhtml?faces-redirect=true";
	}
	
}
