package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.GiftListManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.util.web.FacesUtil;



@ManagedBean(name="GiftListBean") 
@RequestScoped
public class GiftListBean {

	String amico;

	@EJB
	GiftListManagerBeanLocal GLM;

	GiftListDTO giftListDTO;
	

	@PostConstruct
    public void init() {
		giftListDTO = (GiftListDTO)FacesUtil.getSessionMapValue("GiftDTO");
    }
	
	public String reinit() {  
        amico = new String();           
        return null;  
    }  
	
	public String getAmico() {
		return amico;
	}
	public void setAmico(String amico) {
		this.amico = amico;
	}

	public GiftListDTO getGiftListDTO() {
		return giftListDTO;
	}

	public void setGiftListDTO(GiftListDTO giftListDTO) {
		this.giftListDTO = giftListDTO;
	}
	
	public void submit(){
		giftListDTO.setVoloAPag((byte)0);
		giftListDTO.setVoloRPag((byte)0);
		giftListDTO.setHotelPag((byte)0);
		for (EscursioneDTO escursioneDTO : giftListDTO.getViaggio().getLista_escursioni()) {
			EscursionePagataDTO escursionePagata = new EscursionePagataDTO();
			escursionePagata.setEscPagata((byte)0);
			escursionePagata.setEscursione(escursioneDTO);
			System.out.println("aggiunta "+escursionePagata.getEscursione().getNome());
			giftListDTO.getEscursionePagata().add(escursionePagata);
		}
		System.out.println("aggiunta "+giftListDTO.getViaggio().getData_fine());

		GLM.addToGiftList(giftListDTO);
		for (String string : getGiftListDTO().getAmico() ) {
			System.out.println("gift "+string);

		}
		
	}

	
	
	
}
