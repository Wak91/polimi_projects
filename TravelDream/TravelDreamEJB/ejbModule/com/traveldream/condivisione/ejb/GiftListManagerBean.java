package com.traveldream.condivisione.ejb;

import java.util.ArrayList;
import java.util.List;

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
			gift_List.setHotelPag((byte)0);
			gift_List.setVoloAPag((byte)0);
			gift_List.setVoloRPag((byte)0);
			gift_List.setUtente(em.find(Utente.class, giftListDTO.getUtente().getUsername()));
			gift_List.setAmicos(saveEntityAmico(giftListDTO.getAmico()));
			System.out.println("id gift list +"+gift_List.getId());
			em.persist(gift_List);
			em.flush();

			System.out.println("Sono dopo fluscXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
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
	

	private List<Amico> saveEntityAmico(List<String> emails) {
		ArrayList<Amico> friends =new ArrayList<Amico>();
		for (String string : emails) {
			Amico amico =new Amico(string);
			em.persist(amico);
			em.flush();
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXid amico "+amico.getId());
			friends.add(em.find(Amico.class, amico.getId()));
		}
		return friends;
	}

	
	

	
}
