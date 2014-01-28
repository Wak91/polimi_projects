package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;


public interface PackManagerBeanLocal {
	
	public void createPacket(PacchettoDTO packet);
	public ArrayList <PacchettoDTO> getAllPack();
	public PacchettoDTO getPacchettoByID(int id);
	public void deletePacchetto(int id);
	public void modifyPacchetto(PacchettoDTO packet);
	public ArrayList<HotelDTO> getListaHotelCompatibili(String citta, Date inizio, Date fine);
	public ArrayList<VoloDTO> getListaVoliCompatibili(String citta, Date inizio, Date fine);
	public ArrayList<EscursioneDTO> getListaEscursioniCompatibili(String citta, Date inizio, Date fine);
	public ArrayList<PacchettoDTO> getFilteredPacchetti(String destinazione_pacchetto,Date data_inizio_pacchetto,Date data_fine_pacchetto);
}
