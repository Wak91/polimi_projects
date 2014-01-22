package com.traveldream.gestioneprenotazione.ejb;

import javax.ejb.Local;


import java.util.ArrayList;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

@Local
public interface BookManagerBeanLocal {

	
	int saveViaggio(ViaggioDTO v);
	int cercaViaggio(ViaggioDTO viaggiodto);
	void savePrenotazione(PrenotazioneDTO pdto);
	void updateViaggio(ViaggioDTO v);
	int cercaHotelSalvato(HotelDTO hdto);
	int cercaVoloSalvato(VoloDTO vdto);
	int cercaEscursioneSalvata(EscursioneDTO edto);
	int saveEscursioneSalvata(EscursioneDTO escursioneDTO);
	ArrayList <PrenotazioneDTO> cercaPrenotazione(UserDTO udto);

	
}
