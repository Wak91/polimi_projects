package com.traveldream.condivisione.ejb;

import java.util.ArrayList;

import javax.ejb.Local;

import com.traveldream.autenticazione.ejb.UserDTO;

@Local
public interface GiftListManagerBeanLocal {

	ArrayList<GiftListDTO> getGiftListDTO(UserDTO user);
	void addToGiftList(GiftListDTO giftListDTO);
	void removeFromGift(GiftListDTO giftListDTO);
	GiftListDTO findGiftByHash(String codice);
	void aggiornaGift(GiftListDTO giftListDTO);
}
