package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

@Local
public interface ComponentManagerBeanLocal {

	public void saveHotel(HotelDTO hoteldto); 
    public void saveVolo(VoloDTO volodto);
    public void saveEscursione(EscursioneDTO escursionedto);
    public ArrayList<HotelDTO> getAllHotel();
	public List<VoloDTO> getAllVolo();
	public List<EscursioneDTO> getAllEscursione();
	
	public HotelDTO getHotelById(int id);
	public void modificaHotel(HotelDTO h);
	public void eliminaHotel(HotelDTO h);
	
	public VoloDTO getVoloById(int id);
	public void modificaVolo(VoloDTO v);
	public void eliminaVolo(VoloDTO v);

	public EscursioneDTO getEscursioneById(int id);
	public void modificaEscursione(EscursioneDTO v);
	public void eliminaEscursione(EscursioneDTO v);
	
}
