package com.traveldream.gestionecomponente.ejb;

import javax.ejb.Local;
//commento2
@Local
public interface ComponentManagerBeanLocal {

	public void saveHotel(HotelDTO hoteldto);
}
