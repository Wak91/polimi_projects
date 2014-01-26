package com.traveldream.condivisione.ejb;

import java.util.ArrayList;

import javax.ejb.Local;

import com.traveldream.autenticazione.ejb.UserDTO;

@Local
public interface InvitoManagerBeanLocal {

	void submit(InvitoDTO invitoDTO);

	ArrayList<InvitoDTO> cercaInvito(UserDTO udto);

	InvitoDTO cercaInvitoById(int id_amico, String mail);

	void cambiaStato(InvitoDTO invitoDTO);

	InvitoDTO cercaInvitoById(int id);

}
