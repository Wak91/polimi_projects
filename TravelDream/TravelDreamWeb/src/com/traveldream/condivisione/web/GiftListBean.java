package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.GiftListManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.util.web.FacesUtil;
import com.traveldream.viaggio.web.PreDataModel;



@ManagedBean(name="GiftListBean") 
@ViewScoped
public class GiftListBean {

	String amico;
	
	GiftDataModel giftDataModel;
	GiftListDTO selectedGiftListDTO;
	
	
	EscursionePagataDatamodel escursionePagataDatamodel;
	ArrayList<GiftListDTO> filteredGift;
	
	
	@EJB
	UserMgr userMgr;
	
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
		giftListDTO.setVoloAPag(false);
		giftListDTO.setVoloRPag(false);
		giftListDTO.setHotelPag(false);
		for (EscursioneDTO escursioneDTO : giftListDTO.getViaggio().getLista_escursioni()) {
			System.out.println("Aggiungo escursioni a pagata"+escursioneDTO.getId()+escursioneDTO.getNome());
			EscursionePagataDTO escursionePagata = new EscursionePagataDTO();
			escursionePagata.setEscPagata(false);
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
	public void setupEscursioniPagatedialog(){
		System.out.println("ID selected"+selectedGiftListDTO.getId());
		escursionePagataDatamodel= new EscursionePagataDatamodel(selectedGiftListDTO.getEscursionePagata());
	}
	
	public void getGiftList() {
		UserDTO current_user = userMgr.getUserDTO();
		setGiftDataModel(new GiftDataModel(GLM.getGiftListDTO(current_user)));
	}

	public GiftDataModel getGiftDataModel() {
		return giftDataModel;
	}

	public void setGiftDataModel(GiftDataModel giftDataModel) {
		this.giftDataModel = giftDataModel;
	}

	public GiftListDTO getSelectedGiftListDTO() {
		return selectedGiftListDTO;
	}

	public void setSelectedGiftListDTO(GiftListDTO selectedGiftListDTO) {
		this.selectedGiftListDTO = selectedGiftListDTO;
	}

	
	public ArrayList<GiftListDTO> getFilteredGift() {
		return filteredGift;
	}

	public void setFilteredGift(ArrayList<GiftListDTO> filteredGift) {
		this.filteredGift = filteredGift;
	}

	public EscursionePagataDatamodel getEscursionePagataDatamodel() {
		return escursionePagataDatamodel;
	}

	public void setEscursionePagataDatamodel(
			EscursionePagataDatamodel escursionePagataDatamodel) {
		this.escursionePagataDatamodel = escursionePagataDatamodel;
	}

	
	
	
}
