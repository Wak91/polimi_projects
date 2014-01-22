package com.traveldream.gestioneprenotazione.ejb;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

public interface BookManagerBeanLocal {

	
	int saveViaggio(ViaggioDTO v);
	int cercaViaggio(ViaggioDTO viaggiodto);
	void savePrenotazione(PrenotazioneDTO pdto);
	void updateViaggio(ViaggioDTO v);
	int cercaHotelSalvato(HotelDTO hdto);
	int cercaVoloSalvato(VoloDTO vdto);
	int cercaEscursioneSalvata(EscursioneDTO edto);
	
}
