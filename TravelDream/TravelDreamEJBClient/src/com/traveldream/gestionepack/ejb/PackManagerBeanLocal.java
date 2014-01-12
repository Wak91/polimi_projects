package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;


public interface PackManagerBeanLocal {
	
	public void createPacket(PacchettoDTO packet);
	public ArrayList <PacchettoDTO> getAllPack();
	public PacchettoDTO getPacchettoByID(int id);
	public void deletePacchetto(int id);
	public void modifyPacchetto(PacchettoDTO packet);

}
