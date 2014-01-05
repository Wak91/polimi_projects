package com.traveldream.gestionepack.ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//import model.Pacchetto;
import model.Volo;

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
	
	//public void savePacchetto(PacchettoDTO packdto) {
	//	Pacchetto pack = new Pacchetto(packdto);
	//	em.persist(pack);
		
	//}

}
