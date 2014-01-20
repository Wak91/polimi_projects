package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.util.web.FacesUtil;



@ManagedBean(name="GiftListBean") 
@SessionScoped
public class GiftListBean {

	GiftListDTO giftListDTO;
	

	@PostConstruct
    public void init() {
		giftListDTO = (GiftListDTO)FacesUtil.getSessionMapValue("GiftDTO");
		giftListDTO.getAmico().add("");

		System.out.println("volod"+giftListDTO.getId());

		if (giftListDTO==null) {
			System.out.println("XXXXXXXXXXXXXXinding cazzi");
		}
    }
	public void addAmico(){
		giftListDTO.getAmico().add("");
	}
	
	public GiftListDTO getGiftListDTO() {
		return giftListDTO;
	}

	public void setGiftListDTO(GiftListDTO giftListDTO) {
		this.giftListDTO = giftListDTO;
	}

	public void submit(){
		if (giftListDTO==null) {
			System.out.println("XXXXXXXXXXXXXXinding cazzi");
		}
		for (String string : giftListDTO.getAmico()) {
			System.out.println("gift "+string);

		}
		System.out.println("volo"+giftListDTO.getViaggio().getVolo_andata().getCompagnia());
	}
	
	
}
