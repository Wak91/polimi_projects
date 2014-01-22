package com.traveldream.condivisione.ejb;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBean;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;

import model.Amico;
import model.EscursionePagata;
import model.EscursioneSalvata;
import model.Gift_List;
import model.Utente;
import model.Viaggio;


@Stateless
public class GiftListManagerBean implements GiftListManagerBeanLocal {
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	@EJB
	private UserMgr userMgr;
	
	@EJB
	private BookManagerBeanLocal BMG;
	
	
	public void addToGiftList(GiftListDTO giftListDTO){
		Gift_List gift_List = new Gift_List();
		//crea viaggio
		
			Viaggio viaggio = em.find(Viaggio.class, BMG.saveViaggio(giftListDTO.getViaggio()));
			gift_List.setViaggio(viaggio);
			for (String mail:giftListDTO.getAmico()){
				Amico friend =new Amico(mail);
				em.persist(friend);
				em.flush();
				gift_List.getAmicos().add(friend);
			}
			gift_List.setHotelPag((byte)0);
			gift_List.setVoloAPag((byte)0);
			gift_List.setVoloRPag((byte)0);
			gift_List.setUtente(em.find(Utente.class, giftListDTO.getUtente().getUsername()));
			em.persist(gift_List);
			em.flush();
		
			ArrayList<EscursionePagata> escursionePagatas = new ArrayList<EscursionePagata>();
			for (EscursionePagataDTO escursionePagataDTO : giftListDTO.getEscursionePagata()) {
				EscursioneSalvata escursioneSalvata = em.find(EscursioneSalvata.class, escursionePagataDTO.getEscursione().getId());
				EscursionePagata escursionePagata = new EscursionePagata();
				escursionePagata.setEscursioneSalvata(escursioneSalvata);
				escursionePagata.setPagata((byte)0);
				escursionePagata.setGiftList(em.find(Gift_List.class, gift_List.getId()));
				escursionePagatas.add(escursionePagata);
			}
			gift_List.setEscursionePagatas(escursionePagatas);
		
		
	}

	
	

	
}
