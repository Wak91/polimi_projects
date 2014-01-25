package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.GiftListManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;
import com.traveldream.util.web.FacesUtil;
import com.traveldream.viaggio.web.PreDataModel;



@ManagedBean(name="GiftListBean") 
@SessionScoped
public class GiftListBean {

	private String amico;
	private amiciDatamodel amiciDatamodel;

	
	private GiftDataModel giftDataModel;
	private GiftListDTO selectedGiftListDTO;	//gift list per la visualizzazione
	
	
	private EscursionePagataDatamodel escursionePagataDatamodel;
	private ArrayList<GiftListDTO> filteredGift;
	
	private String codiceGift;
	
	@EJB
	private UserMgr userMgr;
	
	@EJB
	private BookManagerBeanLocal BMB; 
	
	@EJB
	private GiftListManagerBeanLocal GLM;

	private GiftListDTO giftListDTO; //gift list per la creazione
	
	
	int costocomplessivo;
	String password;

	
    public void init() {
		giftListDTO = (GiftListDTO)FacesUtil.getSessionMapValue("GiftDTO");
		System.out.println("Username utente"+giftListDTO.getUtente().getUsername());

    }
	
	public String reinit() {  
        amico = new String();           
        return null;  
    }  
		
	public String creaGift(){
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
		return "giftlist.xhtml?faces-redirect=true";
	}
	
	public void eliminaGift() {
		GLM.removeFromGift(selectedGiftListDTO);
		UserDTO current_user = userMgr.getUserDTO();

		setGiftDataModel(new GiftDataModel(GLM.getGiftListDTO(current_user)));


	}
	public void setupamicidialog() {
		amiciDatamodel = new amiciDatamodel(selectedGiftListDTO.getAmico());

	}
	
	public void setupEscursioniPagatedialog(){
		System.out.println("ID selected"+selectedGiftListDTO.getId());
		escursionePagataDatamodel= new EscursionePagataDatamodel(selectedGiftListDTO.getEscursionePagata());
	}
	
	public void getGiftList() {
		UserDTO current_user = userMgr.getUserDTO();
		setGiftDataModel(new GiftDataModel(GLM.getGiftListDTO(current_user)));
	}

	//parte answer gift list

	

	
	
	//calcolo costi
	


	
	public int calcolaCostoHotel(GiftListDTO giftListDTO){
		int costoGiornaliero =giftListDTO.getViaggio().getHotel().getCosto_giornaliero();
		int duration = (int) (( giftListDTO.getViaggio().getHotel().getData_fine().getTime() - giftListDTO.getViaggio().getHotel().getData_inizio().getTime() ) / (1000 * 60 * 60 * 24));
		int numPers =giftListDTO.getNpersone();
		return numPers*duration*costoGiornaliero;
	}


	
	
	public String confermaGift(){
		costocomplessivo=0;
		if (selectedGiftListDTO.isHotelPag()==false){
			costocomplessivo+=calcolaCostoHotel(selectedGiftListDTO);
		}
		if (selectedGiftListDTO.isVoloAPag()==false) {
			costocomplessivo+=selectedGiftListDTO.getViaggio().getVolo_andata().getCosto()*selectedGiftListDTO.getNpersone();
		}
		if (selectedGiftListDTO.isVoloRPag()==false) {
			costocomplessivo+=selectedGiftListDTO.getViaggio().getVolo_ritorno().getCosto()*selectedGiftListDTO.getNpersone();
		}
		for (EscursionePagataDTO escursionePagataDTO : selectedGiftListDTO.getEscursionePagata()) {
			if (escursionePagataDTO.getEscPagata()==false) {
				costocomplessivo+=escursionePagataDTO.getEscursione().getCosto();
			}
		}
		return "pagamentogift.xhtml?faces-redirect=true";
	}
	
	public String AcquistaGift(){
		PrenotazioneDTO prenotazione = new PrenotazioneDTO();
		prenotazione.setCosto(costocomplessivo);
		prenotazione.setNumero_persone(selectedGiftListDTO.getNpersone());
		prenotazione.setUtente(selectedGiftListDTO.getUtente());
		prenotazione.setViaggio(selectedGiftListDTO.getViaggio());
		BMB.savePrenotazione(prenotazione);
		
		GLM.removeFromGift(selectedGiftListDTO);
		return "imieiviaggi.xhtml?faces-redirect=true";
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

	public amiciDatamodel getAmiciDatamodel() {
		return amiciDatamodel;
	}

	public void setAmiciDatamodel(amiciDatamodel amiciDatamodel) {
		this.amiciDatamodel = amiciDatamodel;
	}

	public String getCodiceGift() {
		return codiceGift;
	}

	public void setCodiceGift(String codiceGift) {
		this.codiceGift = codiceGift;
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
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCostocomplessivo() {
		return costocomplessivo;
	}

	public void setCostocomplessivo(int costocomplessivo) {
		this.costocomplessivo = costocomplessivo;
	}

	
	
	
	
}
