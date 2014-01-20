package com.traveldream.gestioneprenotazione.ejb;

public interface BookManagerBeanLocal {

	
	void saveViaggio(ViaggioDTO v);
	int cercaViaggio(ViaggioDTO viaggiodto);
	
	
}
