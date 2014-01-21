package com.traveldream.condivisione.ejb;

import javax.ejb.Local;

@Local
public interface GiftListManagerBeanLocal {

	void addToGiftList(GiftListDTO giftListDTO);
}
