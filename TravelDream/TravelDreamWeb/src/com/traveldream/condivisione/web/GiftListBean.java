package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.util.web.FacesUtil;



@ManagedBean(name="GiftListBean") 
@RequestScoped
public class GiftListBean {

	String amico;


	GiftListDTO giftListDTO;
	

	@PostConstruct
    public void init() {
		giftListDTO = (GiftListDTO)FacesUtil.getSessionMapValue("GiftDTO");


		System.out.println("volod"+giftListDTO.getId());

		if (giftListDTO==null) {
			System.out.println("XXXXXXXXXXXXXXinding cazzi");
		}
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
		System.out.println("dddewdwedewew");
		if (giftListDTO==null) {
			System.out.println("XXXXXXXXXXXXXXinding cazzi");
		}
		for (String string : getGiftListDTO().getAmico() ) {
			System.out.println("gift "+string);

		}
		System.out.println("volo"+giftListDTO.getViaggio().getVolo_andata().getCompagnia());
	}

	
	
	
}
