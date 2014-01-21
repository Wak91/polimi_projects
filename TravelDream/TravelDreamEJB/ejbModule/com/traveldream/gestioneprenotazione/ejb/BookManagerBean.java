package com.traveldream.gestioneprenotazione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.Convert;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

import model.Escursione;
import model.EscursioneSalvata;
import model.Hotel;
import model.HotelSalvato;
import model.Pacchetto;
import model.Prenotazione;
import model.Utente;
import model.Viaggio;
import model.Volo;
import model.VoloSalvato;

/**
 * Session Bean implementation class BookManagerBean
 */
@Stateless
public class BookManagerBean implements BookManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	
	public int saveViaggio(ViaggioDTO v)
	{
		Viaggio travel = new Viaggio();
		travel.setData_inizio(v.getData_inizio());
		travel.setData_fine(v.getData_fine());
		travel.setHotelSalvato(this.DTOtoEntityHotel(v.getHotel()));
		travel.setVoloSalvato1(this.DTOtoEntityVolo(v.getVolo_andata()));
		travel.setVoloSalvato2(this.DTOtoEntityVolo(v.getVolo_ritorno()));
        em.persist(travel);	
        em.flush();
        return em.find(Viaggio.class, travel.getId()).getId();
	}
	
	public void updateViaggio(ViaggioDTO v)
	{
		Viaggio travel = new Viaggio();
		travel.setData_inizio(v.getData_inizio());
		travel.setData_fine(v.getData_fine());
		travel.setHotelSalvato(this.DTOtoEntityHotel(v.getHotel()));
		travel.setVoloSalvato1(this.DTOtoEntityVolo(v.getVolo_andata()));
		travel.setVoloSalvato2(this.DTOtoEntityVolo(v.getVolo_ritorno()));
		travel.setEscursioneSalvatas(this.DTOtoEntityEscursione(v.getLista_escursioni()));
		em.merge(travel);
	}
	
	public void savePrenotazione(PrenotazioneDTO pdto)
	{
		Prenotazione p = new Prenotazione();
		p.setCosto(pdto.getCosto());
		p.setNumero_persone(pdto.getNumero_persone());
		p.setUtenteBean(DTOtoEntityUtente(pdto.getUtente()));
		p.setViaggioBean(DTOtoEntityViaggio(pdto.getViaggio()));
		em.persist(p);
	}
	
	 private Viaggio DTOtoEntityViaggio(ViaggioDTO viaggio) {
		return em.find(Viaggio.class, viaggio.getId());
	}


	private Utente DTOtoEntityUtente(UserDTO utente) {
		return em.find(Utente.class, utente.getUsername());
	}


	private HotelSalvato DTOtoEntityHotel(HotelDTO hoteldto){
         return em.find(HotelSalvato.class, hoteldto.getId());      
 }
	 
	 private List<EscursioneSalvata> DTOtoEntityEscursione(List<EscursioneDTO> escursioneDTOs){
         ArrayList<EscursioneSalvata> listaEscursioni = new ArrayList<EscursioneSalvata>();
         for (EscursioneDTO escursioneDTO :escursioneDTOs){
                 EscursioneSalvata nuovaesc = em.find(EscursioneSalvata.class, escursioneDTO.getId());
                 listaEscursioni.add(nuovaesc);
         }
         return listaEscursioni;
 }
	  private  VoloSalvato DTOtoEntityVolo(VoloDTO volodto){
       
      return em.find(VoloSalvato.class, volodto.getId());
  }
	  
 public int cercaViaggio(ViaggioDTO viaggiodto)
 {
		List <Viaggio> mylist;
		mylist = em.createNamedQuery("Viaggio.findAll", Viaggio.class).getResultList();
		
		for(Viaggio v: mylist)
		   {
			 if(v.getHotelSalvato().getId() == (viaggiodto.getHotel().getId()) &&   
		        v.getData_fine().equals(viaggiodto.getData_fine()) &&
				v.getData_inizio().equals(viaggiodto.getData_inizio()) &&
				v.getVoloSalvato1().getId() == viaggiodto.getVolo_andata().getId() &&
				v.getVoloSalvato2().getId() == viaggiodto.getVolo_ritorno().getId() &&
				sameEscursioni(v,viaggiodto) == 1 )
			    {
				  return v.getId();
			    }
			
		   }
		
		return -1;		
 }	
 
 

	private int sameEscursioni(Viaggio v, ViaggioDTO vdto)
	{
		for(EscursioneSalvata e: v.getEscursioneSalvatas())
		   {
			if(e.getId() != vdto.getId() )
				return 0;
		   }
		return 1;
		
	}
	
}
