package com.traveldream.gestioneprenotazione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import model.EscursioneSalvata;
import model.HotelSalvato;
import model.Prenotazione;
import model.Utente;
import model.Viaggio;
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
		Viaggio travel2 = new Viaggio();
		travel.setData_inizio(v.getData_inizio());
		travel.setData_fine(v.getData_fine());
		travel.setHotelSalvato(this.DTOtoEntityHotel(v.getHotel()));
		System.out.println("sava"+v.getHotel().getNome());
		travel.setVoloSalvato1(this.DTOtoEntityVolo(v.getVolo_andata()));
		travel.setVoloSalvato2(this.DTOtoEntityVolo(v.getVolo_ritorno()));
        em.persist(travel);	
        em.flush();
        
        travel2 = em.find(Viaggio.class, travel.getId());
        
        for(EscursioneSalvata es: (this.DTOtoEntityEscursione(v.getLista_escursioni())))
		    {
	    	em.persist(es);
	    	em.flush();
	    	travel2.getEscursioneSalvatas().add(em.find(EscursioneSalvata.class, es.getId()));
		    }
        
			em.merge(travel2);
		
			return  em.find(Viaggio.class, travel.getId()).getId();
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
         EscursioneSalvata es = new EscursioneSalvata();
         for (EscursioneDTO escursioneDTO :escursioneDTOs){
        	      es.setCosto(escursioneDTO.getCosto());
        	      es.setData(escursioneDTO.getData());
        	      es.setImmagine(escursioneDTO.getImmagine());
        	      es.setLuogo(escursioneDTO.getLuogo());
        	      es.setNome(escursioneDTO.getNome());
                  listaEscursioni.add(es);
         }
         return listaEscursioni;
 }
	  private  VoloSalvato DTOtoEntityVolo(VoloDTO volodto){
       
      return em.find(VoloSalvato.class, volodto.getId());
  }
	  
public int cercaHotelSalvato(HotelDTO hdto)
{
	List<HotelSalvato> myList;
	myList = em.createNamedQuery("HotelSalvato.findAll", HotelSalvato.class).getResultList();
	
	for(HotelSalvato hs : myList)
	   {
		if( (   hs.getCosto_giornaliero() == hdto.getCosto_giornaliero() ) 
		     && hs.getData_fine().equals(hdto.getData_fine())
			 && (hs.getData_inizio().equals(hdto.getData_inizio())) 
			 && (hs.getLuogo().equals(hdto.getLuogo()))	
			 && (hs.getNome().equals(hdto.getNome()))
			 && (hs.getStelle() == hdto.getStelle()))
		    {
			 return hs.getId();
		    }
	   }
	return -1;

}


@Override
public int cercaVoloSalvato(VoloDTO vdto) {
	
	List<VoloSalvato> myList;
	myList = em.createNamedQuery("VoloSalvato.findAll", VoloSalvato.class).getResultList();
	
	for(VoloSalvato vs : myList)
	   {
		if( (   vs.getCosto() == vdto.getCosto() ) 
		     && vs.getData().equals(vdto.getData())
			 && (vs.getLuogo_arrivo().equals(vdto.getLuogo_arrivo()))	
			 &&  (vs.getLuogo_partenza().equals(vdto.getLuogo_partenza())) 
			 && (vs.getCompagnia() == vdto.getCompagnia()))  
		    {
			 return vs.getId();
		    }
	   }
	return -1;
	
}

@Override
public int cercaEscursioneSalvata(EscursioneDTO edto) {
	
	List<EscursioneSalvata> myList;
	myList = em.createNamedQuery("EscursioneSalvata.findAll", EscursioneSalvata.class).getResultList();
	
	for(EscursioneSalvata es : myList)
	   {
		if( (   es.getCosto() == edto.getCosto() ) 
		     && es.getData().equals(edto.getData())
			 &&  (es.getLuogo().equals(edto.getLuogo())) 
			 && (es.getNome() == edto.getNome()))   
		    {
			 return es.getId();
		    }
	   }
	return -1;
	
}

//Forse meglio many to many, dovrei aggiungere ad un escursione esistente la nuova key del viaggio
// ma le escurisioni hanno un solo attribut key viaggio! 
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
	
	public int saveEscursioneSalvata(EscursioneDTO escursioneDTO)
	{
		EscursioneSalvata escursione = new EscursioneSalvata();
		escursione.setNome(escursioneDTO.getNome());
		escursione.setLuogo(escursioneDTO.getLuogo());
		escursione.setImmagine(escursioneDTO.getImmagine());
		escursione.setData(escursioneDTO.getData());
		escursione.setCosto(escursioneDTO.getCosto());
		
		em.persist(escursione);
		em.flush();
		return em.find(EscursioneSalvata.class, escursione.getId()).getId();
	}

	public ArrayList <PrenotazioneDTO> cercaPrenotazione(UserDTO udto)
	{
		List <Prenotazione> myList = em.createNamedQuery("Prenotazione.findAll", Prenotazione.class).getResultList();
		ArrayList <PrenotazioneDTO> myDTOList = new ArrayList <PrenotazioneDTO>();
		
		for(Prenotazione p : myList )
		   {
			if(p.getUtenteBean().getUsername().equals(udto.getUsername()))
			  {
				PrenotazioneDTO p1 = new PrenotazioneDTO();
				p1.setCosto(p.getCosto());
				p1.setId(p.getId());
				p1.setNumero_persone(p.getNumero_persone());
				myDTOList.add(p1);
			  }
		   }
		return myDTOList;
		
	}
	
}
