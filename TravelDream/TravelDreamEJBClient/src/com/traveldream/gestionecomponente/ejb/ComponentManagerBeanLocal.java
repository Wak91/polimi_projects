package com.traveldream.gestionecomponente.ejb;

import javax.ejb.Local;

@Local
public interface ComponentManagerBeanLocal {

	public void saveHotel(HotelDTO hoteldto);
}
