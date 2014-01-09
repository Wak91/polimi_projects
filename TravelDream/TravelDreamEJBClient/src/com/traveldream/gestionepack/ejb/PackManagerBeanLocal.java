package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;


public interface PackManagerBeanLocal {
	
	public void createPacket(PacchettoDTO packet);
	public ArrayList <PacchettoDTO> getAllPack();
	

}
