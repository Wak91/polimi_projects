package com.traveldream.gestioneprenotazione.ejb;

import java.util.ArrayList;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

public interface BookManagerBeanLocal {

	
	ViaggioDTO saveViaggio(ViaggioDTO v);
	int cercaViaggio(ViaggioDTO viaggiodto);
	void savePrenotazione(PrenotazioneDTO pdto);
	void updateViaggio(ViaggioDTO v);
	int cercaHotelSalvato(HotelDTO hdto);
	int cercaVoloSalvato(VoloDTO vdto);
	int cercaEscursioneSalvata(EscursioneDTO edto);
	int saveEscursioneSalvata(EscursioneDTO escursioneDTO);
	ArrayList <PrenotazioneDTO> cercaPrenotazione(UserDTO udto);
	int saveHotelSalvato(HotelDTO hoteldto); 
    int saveVoloSalvato(VoloDTO volodto);

	
}
