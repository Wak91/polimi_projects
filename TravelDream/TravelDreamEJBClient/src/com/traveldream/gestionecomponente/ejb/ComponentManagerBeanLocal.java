package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;

import javax.ejb.Local;

@Local
public interface ComponentManagerBeanLocal {

	public void saveHotel(HotelDTO hoteldto); 
    public void saveVolo(VoloDTO volodto);
    public void saveEscursione(EscursioneDTO escursionedto);
    public ArrayList<HotelDTO> getAllHotel();
}
