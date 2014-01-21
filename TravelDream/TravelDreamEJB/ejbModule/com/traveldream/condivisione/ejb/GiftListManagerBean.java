package com.traveldream.condivisione.ejb;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.EscursionePagata;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.emptyType;

@Stateless
public class GiftListManagerBean implements GiftListManagerBeanLocal {
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	
	public void addToGiftList(GiftListDTO giftListDTO){
		//crea viaggio
		for (EscursionePagataDTO escursionePagataDTO : giftListDTO.getEscursionePagata()) {
			EscursionePagata escursionePagata = em.find(EscursionePagata.class,escursionePagataDTO.getId() );
		}
		
		
	}
	
	

	
}
