package com.traveldream.util.web;

import javax.annotation.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;


public class Pagamento {
	
	
	

	public static int CalcolaCostoUtenteGift(GiftListDTO giftList){
		int costocomplessivo=0;
		if (giftList.isHotelPag()==false){
			costocomplessivo+=calcolaCostoHotel(giftList);
		}
		if (giftList.isVoloAPag()==false) {
			costocomplessivo+=giftList.getViaggio().getVolo_andata().getCosto()*giftList.getNpersone();
		}
		if (giftList.isVoloRPag()==false) {
			costocomplessivo+=giftList.getViaggio().getVolo_ritorno().getCosto()*giftList.getNpersone();
		}
		for (EscursionePagataDTO escursionePagataDTO : giftList.getEscursionePagata()) {
			if (escursionePagataDTO.getEscPagata()==false) {
				costocomplessivo+=escursionePagataDTO.getEscursione().getCosto()*giftList.getNpersone();
			}
		}
		return costocomplessivo;
	}
	
	public static int calcolaCostoHotel(GiftListDTO giftListDTO){
		int costoGiornaliero =giftListDTO.getViaggio().getHotel().getCosto_giornaliero();
		int duration = (int) (( giftListDTO.getViaggio().getHotel().getData_fine().getTime() - giftListDTO.getViaggio().getHotel().getData_inizio().getTime() ) / (1000 * 60 * 60 * 24));
		int numPers =giftListDTO.getNpersone();
		return numPers*duration*costoGiornaliero;
	}
	
	public static int  calcolaCostoVoloA(GiftListDTO giftListDTO) {
		return giftListDTO.getViaggio().getVolo_andata().getCosto()*giftListDTO.getNpersone();
	}
	
	public static int  calcolaCostoVoloR(GiftListDTO giftListDTO) {
		return giftListDTO.getViaggio().getVolo_ritorno().getCosto()*giftListDTO.getNpersone();
	}

}
