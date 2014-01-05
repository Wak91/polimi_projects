package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;



import model.Pacchetto;
//import model.Pacchetto;
import model.Volo;

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

/**
 * Session Bean implementation class PackManagerBean
 */
@Stateless
public class PackManagerBean implements PackManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;

	@Override
	public void createPacket(PacchettoDTO packet) {
		Pacchetto pacchetto = new Pacchetto(packet);
		em.persist(pacchetto);
		
	}
	
	//public void savePacchetto(PacchettoDTO packdto) {
	//	Pacchetto pack = new Pacchetto(packdto);
	//	em.persist(pack);
		
	//}
	
	

}
