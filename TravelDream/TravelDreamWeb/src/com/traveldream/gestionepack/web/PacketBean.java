package com.traveldream.gestionepack.web;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import com.traveldream.gestionecomponente.ejb.ComponentManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

@ManagedBean(name="PacketBean") 
@RequestScoped
public class PacketBean {

	//nel costruttore vado a fare una query per prelevare gli hotel, i voli e le escursioni.
	// e me li salvo in array list posizionati qui, la lista degli hote la piazzo in un component
	// Drag & drop per creare da li la lista di hotel presenti in quel pacchetto. 
	// http://www.primefaces.org/showcase/ui/dndTable.jsf
}
