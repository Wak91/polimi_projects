package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.traveldream.gestionecomponente.ejb.HotelDTO;


public interface PackManagerBeanLocal {
	
	public void createPacket(PacchettoDTO packet);
	public ArrayList <PacchettoDTO> getAllPack();
	public PacchettoDTO getPacchettoByID(int id);
	public void deletePacchetto(int id);
	public void modifyPacchetto(PacchettoDTO packet);
	public ArrayList<HotelDTO> getListaHotelCompatibili(String citta, Date inizio, Date fine);

}
