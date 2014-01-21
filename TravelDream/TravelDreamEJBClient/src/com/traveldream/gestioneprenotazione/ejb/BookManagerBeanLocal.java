package com.traveldream.gestioneprenotazione.ejb;

public interface BookManagerBeanLocal {

	
	int saveViaggio(ViaggioDTO v);
	int cercaViaggio(ViaggioDTO viaggiodto);
	void savePrenotazione(PrenotazioneDTO pdto);
	void updateViaggio(ViaggioDTO v);
	
}
