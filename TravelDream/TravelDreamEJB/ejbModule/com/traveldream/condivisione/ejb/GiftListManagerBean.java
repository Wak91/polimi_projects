package com.traveldream.condivisione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBean;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;
import com.traveldream.util.Converter;

import model.Amico;
import model.EscursionePagata;
import model.EscursioneSalvata;
import model.Gift_List;
import model.Hotel;
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
		
			Viaggio viaggio = em.find(Viaggio.class, BMG.saveViaggio(giftListDTO.getViaggio()).getId());
			gift_List.setViaggio(viaggio);
			gift_List.setHotelPag(false);
			gift_List.setVoloAPag(false);
			gift_List.setVoloRPag(false);
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
				escursionePagata.setPagata(false);
				escursionePagata.setGiftList(em.find(Gift_List.class, gift_List.getId()));
				System.out.println("escursione salvat :"+escursionePagata.getEscursioneSalvata().getNome()+escursionePagata.getEscursioneSalvata().getId());
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
			friends.add(em.find(Amico.class, amico.getId()));
		}
		return friends;
	}


	@Override
	public ArrayList<GiftListDTO> getGiftListDTO(UserDTO user) {
		ArrayList<GiftListDTO> giftListDTOs = new ArrayList<GiftListDTO>();
		List<Gift_List> gift_Lists =em.createNamedQuery("Gift_List.findbyuser", Gift_List.class).setParameter("u", em.find(Utente.class, user.getUsername())).getResultList();
		
		for (Gift_List gift_List : gift_Lists) {
			em.refresh(gift_List);
			giftListDTOs.add(EntitytoDtoGift(gift_List));
		}

		System.out.println(giftListDTOs);
		return giftListDTOs;
	}

	private GiftListDTO EntitytoDtoGift(Gift_List gift_List){
		GiftListDTO giftListDTO =new GiftListDTO();
		giftListDTO.setId(gift_List.getId());
		giftListDTO.setHotelPag(gift_List.isHotelPag());
		giftListDTO.setVoloAPag(gift_List.isVoloAPag());
		giftListDTO.setVoloRPag(gift_List.isVoloRPag());
		for (Amico amico : gift_List.getAmicos()) {
			giftListDTO.getAmico().add(amico.getAmico());
		}
		for (EscursionePagata escursionePagata : gift_List.getEscursionePagatas()) {
			System.out.println("in coverter gift list"+giftListDTO.getId()+" escursione pagata id "+escursionePagata.getId()+"escursione salvata "+escursionePagata.getEscursioneSalvata().getId()+escursionePagata.getEscursioneSalvata().getNome());
			giftListDTO.getEscursionePagata().add(EntitytoDTOEscuzionePagata(escursionePagata));
		}
		giftListDTO.setViaggio(Converter.ViaggioToDTO(gift_List.getViaggio()));
		return giftListDTO;
	}
	
	private EscursionePagataDTO EntitytoDTOEscuzionePagata(EscursionePagata escursionePagata) {
		EscursionePagataDTO escursionePagataDTO = new EscursionePagataDTO();
		escursionePagataDTO.setEscPagata(escursionePagata.getPagata());
		escursionePagataDTO.setEscursione(Converter.EscursioneToDTO((escursionePagata.getEscursioneSalvata())));
		return escursionePagataDTO;

	}
	
}
