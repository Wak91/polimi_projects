package com.traveldream.condivisione.web;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.GiftListManagerBeanLocal;

@ManagedBean(name="answerGift") 
@SessionScoped
public class AnswerGiftListBean {
	
private GiftListDTO giftListDTOAmicoDto; //gift list vista dall'amico
	
	@EJB
	private GiftListManagerBeanLocal GLM;

	boolean currentHotelPag;
	boolean currentVoloaPag;
	boolean currentVolorPag;
	ArrayList<EscursionePagataDTO> currentEscPag;
	
	int costocomplessivo;
	String password;
	
	public String verifyCode(String codice) {
		System.out.println(codice);
		GiftListDTO gift=GLM.findGiftByHash(codice);
		if (gift==null){
			System.out.println("sono nel if");
			FacesContext.getCurrentInstance().addMessage("codice", new FacesMessage("Il codice inserito non e' corretto"));
			return null;
		}
		System.out.println("sono fuori nel if");

		giftListDTOAmicoDto=gift;
		return "answergift.xhtml?faces-redirect=true&id="+codice;

	}
	
	public void loadGiftFromHash(String codice){
		System.out.println(codice);
		GiftListDTO gift=GLM.findGiftByHash(codice);
		if (gift==null){
			System.out.println("sono nel if");
			FacesContext.getCurrentInstance().addMessage("null", new FacesMessage("Il codice inserito non e' corretto"));
		}
		giftListDTOAmicoDto=gift;
		currentEscPag=new ArrayList<EscursionePagataDTO>();
		//set up info per valutare queli checkbox l'itente seleziona
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			EscursionePagataDTO escPagata =new EscursionePagataDTO();
			escPagata.setId(escursionePagataDTO.getId());
			escPagata.setEscursione(escursionePagataDTO.getEscursione());
			escPagata.setEscPagata(false);
			currentEscPag.add(escPagata);
		}

		
	}
	
	
	public boolean renderEsc(int id){
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			System.out.println("escursioni in esc amico"+escursionePagataDTO.getId()+escursionePagataDTO.getEscursione().getNome());
			if (escursionePagataDTO.getId()==id & escursionePagataDTO.getEscPagata()) {
				System.out.println("id esc"+escursionePagataDTO.getId()+" pagata? "+escursionePagataDTO.getEscPagata());
				return false;
			}
		}
		return true;
	}
	
	public int calcolaCostoHotel(GiftListDTO giftListDTO){
		int costoGiornaliero =giftListDTO.getViaggio().getHotel().getCosto_giornaliero();
		int duration = (int) (( giftListDTO.getViaggio().getHotel().getData_fine().getTime() - giftListDTO.getViaggio().getHotel().getData_inizio().getTime() ) / (1000 * 60 * 60 * 24));
		int numPers =giftListDTO.getNpersone();
		return numPers*duration*costoGiornaliero;
	}

	
	
	
public String paga(){
	costocomplessivo=0;
	if (currentHotelPag){
		costocomplessivo+=calcolaCostoHotel(giftListDTOAmicoDto);
	}
	if (currentVoloaPag) {
		costocomplessivo+=giftListDTOAmicoDto.getViaggio().getVolo_andata().getCosto()*giftListDTOAmicoDto.getNpersone();
	}
	if (currentVolorPag) {
		costocomplessivo+=giftListDTOAmicoDto.getViaggio().getVolo_ritorno().getCosto()*giftListDTOAmicoDto.getNpersone();
	}
	for (EscursionePagataDTO escursionePagataDTO : currentEscPag) {
		if (escursionePagataDTO.getEscPagata()) {
			costocomplessivo+=escursionePagataDTO.getEscursione().getCosto();
		}
	}
	System.out.println("hotel "+currentHotelPag+"volo a"+currentVoloaPag+"volo r "+currentVolorPag);
	System.out.println("costo comp"+costocomplessivo);
	return "pagamentogift.xhtml?faces-redirect=true";
}
	
	public String salvaPagamentoGift(){
	
		
	
		if(giftListDTOAmicoDto.isHotelPag()==false){	//se l'hotel non e stato ancora pagato lo aggiorno con il valore della scelta attuale se è false rimane uguale altrimenti significa che è stato pagato
		giftListDTOAmicoDto.setHotelPag(currentHotelPag);
		}
		if(giftListDTOAmicoDto.isVoloAPag()==false){	//come per hotel
			giftListDTOAmicoDto.setVoloAPag(currentVoloaPag);
			}
		if(giftListDTOAmicoDto.isVoloRPag()==false){	//come per hotel
			giftListDTOAmicoDto.setVoloRPag(currentVolorPag);
			}
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			for (EscursionePagataDTO currentEsc : currentEscPag) {
				if (escursionePagataDTO.getId()==currentEsc.getId() & escursionePagataDTO.getEscPagata()==false){
					escursionePagataDTO.setEscPagata(currentEsc.getEscPagata());
				}
			}
		}
		GLM.aggiornaGift(giftListDTOAmicoDto);
		return "login.xhtml?faces-redirect=true&user=true";
	}
	
	
	public GiftListDTO getGiftListDTOAmicoDto() {
		return giftListDTOAmicoDto;
	}

	public void setGiftListDTOAmicoDto(GiftListDTO giftListDTOAmicoDto) {
		this.giftListDTOAmicoDto = giftListDTOAmicoDto;
	}

	public int getCostocomplessivo() {
		return costocomplessivo;
	}

	public void setCostocomplessivo(int costocomplessivo) {
		this.costocomplessivo = costocomplessivo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCurrentHotelPag() {
		return currentHotelPag;
	}
	public void setCurrentHotelPag(boolean currentHotelPag) {
		this.currentHotelPag = currentHotelPag;
	}
	public boolean isCurrentVoloaPag() {
		return currentVoloaPag;
	}
	public void setCurrentVoloaPag(boolean currentVoloaPag) {
		this.currentVoloaPag = currentVoloaPag;
	}
	public boolean isCurrentVolorPag() {
		return currentVolorPag;
	}
	public void setCurrentVolorPag(boolean currentVolorPag) {
		this.currentVolorPag = currentVolorPag;
	}
	public ArrayList<EscursionePagataDTO> getCurrentEscPag() {
		return currentEscPag;
	}
	public void setCurrentEscPag(ArrayList<EscursionePagataDTO> currentEscPag) {
		this.currentEscPag = currentEscPag;
	}


}
