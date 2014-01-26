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
import com.traveldream.util.web.Pagamento;

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
			escPagata.setEscPagata(escursionePagataDTO.getEscPagata());
			currentEscPag.add(escPagata);
		}
		currentHotelPag=gift.isHotelPag();
		currentVoloaPag=gift.isVoloAPag();
		currentVolorPag=gift.isVoloRPag();

		
	}
	
	
	public boolean renderEsc(int id){
		for (EscursionePagataDTO escursionePagataDTO : currentEscPag) {
			System.out.println("escursioni in esc amico"+escursionePagataDTO.getId()+escursionePagataDTO.getEscursione().getNome());
			if (escursionePagataDTO.getId()==id & escursionePagataDTO.getEscPagata()) {
				System.out.println("id esc"+escursionePagataDTO.getId()+" pagata? "+escursionePagataDTO.getEscPagata());
				return false;
			}
		}
		return true;
	}
	
	public int calcolaCostoHotel(){
		return Pagamento.calcolaCostoHotel(giftListDTOAmicoDto);
	}
	public int  calcolaCostoVoloA() {
		return Pagamento.calcolaCostoVoloA(giftListDTOAmicoDto);
	}
	public int  calcolaCostoVoloR() {
		return Pagamento.calcolaCostoVoloR(giftListDTOAmicoDto);
	}

	
	
	
public String paga(){
	
	costocomplessivo=0;
	if (!currentHotelPag & giftListDTOAmicoDto.isHotelPag()){
		costocomplessivo+=calcolaCostoHotel();
	}
	if (!currentVoloaPag & giftListDTOAmicoDto.isVoloAPag()) {
		costocomplessivo+=calcolaCostoVoloA();
	}
	if (!currentVolorPag & giftListDTOAmicoDto.isVoloRPag()) {
		costocomplessivo+=calcolaCostoVoloR();
	}
	for (EscursionePagataDTO escursionePagataDTO : currentEscPag) {
		for (EscursionePagataDTO escInGift : giftListDTOAmicoDto.getEscursionePagata()) {
			if (!escursionePagataDTO.getEscPagata() & escInGift.getId()==escursionePagataDTO.getId() & escInGift.getEscPagata()) {
				costocomplessivo+=escursionePagataDTO.getEscursione().getCosto()*giftListDTOAmicoDto.getNpersone();
			}
		}
	}
	
	System.out.println("hotel "+currentHotelPag+"volo a"+currentVoloaPag+"volo r "+currentVolorPag);
	System.out.println("costo comp"+costocomplessivo);
	return "pagamentogift.xhtml?faces-redirect=true";
}
	
	public String salvaPagamentoGift(){

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
