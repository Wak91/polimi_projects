package com.traveldream.gestioneprenotazione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.gestionecomponente.ejb.ComponentManagerBean;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import com.traveldream.util.Converter;

import model.EscursioneSalvata;
import model.HotelSalvato;
import model.Invito;
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

	
	
	public ViaggioDTO saveViaggio(ViaggioDTO v)
	{
        
		ArrayList <Integer> id_escursioni = new ArrayList <Integer> ();
		int id_e;
		// salvo le copie degli elementi selezionati per la creazione del viaggio, controllando se esistono già
		int id_h = this.cercaHotelSalvato(v.getHotel());
		if(id_h == -1 )
		   {
			id_h  = this.saveHotelSalvato(v.getHotel()); //recupero gli id delle copie appena salvate
		   }
		int id_vsa = this.cercaVoloSalvato(v.getVolo_andata());
		if(id_vsa == -1)
		   {
			id_vsa = this.saveVoloSalvato(v.getVolo_andata());
		   }
		int id_vsr = this.cercaVoloSalvato(v.getVolo_ritorno());
		if(id_vsr == -1)
		  {
			id_vsr = this.saveVoloSalvato(v.getVolo_ritorno());
		  }
		
		for(EscursioneDTO edto: v.getLista_escursioni())
		   {
			id_e = this.cercaEscursioneSalvata(edto);
			if(id_e == -1 )
			  {
				id_e = this.saveEscursioneSalvata(edto);
			  }
			id_escursioni.add(id_e); // salvo tutti gli id delle escursioni in entrambi i casi ( salvate o no )
		    edto.setId(id_e); // già che sto ciclando sulle selectedEsc metto a posto il DTO con il nuovo ID
		   }
		
		 v.getHotel().setId(id_h); //aggiorno gli id dei DTO, solo quelli perchè gli altri campi sono gia' a posto
		 v.getVolo_andata().setId(id_vsa);  
		 v.getVolo_ritorno().setId(id_vsr);
		 //Gli id dei componenti ora sono id che si riferiscono alla tabella dei componenti salvati
		 
		 int id = this.cercaViaggio(v); // vado alla ricerca di possibili duplicati del viaggio appena creato
		 if(id == -1 )
		 {
		   Viaggio travel = new Viaggio();
		   travel.setData_inizio(v.getData_inizio());
		   travel.setData_fine(v.getData_fine());
		   travel.setHotelSalvato(this.DTOtoEntityHotel(v.getHotel()));
		   travel.setVoloSalvato1(this.DTOtoEntityVolo(v.getVolo_andata()));
		   travel.setVoloSalvato2(this.DTOtoEntityVolo(v.getVolo_ritorno()));		  
		   travel.setEscursioneSalvatas(this.DTOtoEntityEscursione(v.getLista_escursioni()));
		   
		   em.persist(travel);	
		   em.flush();
		   return  Converter.ViaggioToDTO(em.find(Viaggio.class, travel.getId()));
		 }
		 else
			 return Converter.ViaggioToDTO(em.find(Viaggio.class, id));
         
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
		em.flush();
		
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
             EscursioneSalvata es = new EscursioneSalvata();

        	 if (escursioneDTO.getId()!=0) {
				es.setId(escursioneDTO.getId());
			}        	
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
		if( 
			 (   hs.getCosto_giornaliero() == hdto.getCosto_giornaliero() ) 
		     && 
		     (   hs.getData_fine().equals(hdto.getData_fine()) )
			 && 
			 (   hs.getData_inizio().equals(hdto.getData_inizio()) ) 
			 && 
			 (   hs.getLuogo().equals(hdto.getLuogo())             )	
			 && 
			 (   hs.getNome().equals(hdto.getNome())               )
			 //&& 
			 //(   hs.getStelle() == hdto.getStelle()                )              
		  )
		    {
			 return hs.getId();
		    }
	   }
	return -1;

}


public int cercaVoloSalvato(VoloDTO vdto) {
	
	List<VoloSalvato> myList;
	myList = em.createNamedQuery("VoloSalvato.findAll", VoloSalvato.class).getResultList();
	
	for(VoloSalvato vs : myList)
	   {
		if( (   vs.getCosto() == vdto.getCosto() ) 
		     && vs.getData().equals(vdto.getData())
			 && (vs.getLuogo_arrivo().equals(vdto.getLuogo_arrivo()))	
			 &&  (vs.getLuogo_partenza().equals(vdto.getLuogo_partenza())) 
			 && (vs.getCompagnia().equals(vdto.getCompagnia())))  
		    {
			 return vs.getId();
		    }
	   }
	return -1;
	
}

public int cercaEscursioneSalvata(EscursioneDTO edto) {
	
	List<EscursioneSalvata> myList;
	myList = em.createNamedQuery("EscursioneSalvata.findAll", EscursioneSalvata.class).getResultList();
	
	for(EscursioneSalvata es : myList)
	   {
		if( (   es.getCosto() == edto.getCosto() ) 
		     && es.getData().equals(edto.getData())
			 &&  (es.getLuogo().equals(edto.getLuogo())) 
			 && (es.getNome().equals(edto.getNome())))   
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
		int cont=0;
		if(v.getEscursioneSalvatas().size() != vdto.getLista_escursioni().size())
			return 0; // se gi�� le dimensioni sono diverse una conterr�� un'escursione diversa da un altra
		for(EscursioneSalvata e: v.getEscursioneSalvatas())
		   {
			cont=0;
			for(EscursioneDTO edto : vdto.getLista_escursioni())
			   {
				if(e.getId() == edto.getId() )
				  cont++;
			   }
			if(cont==0) // il cont deve essere andato a uno per avere le corrispondenze di escursioni uguali
				return 0;
		   }
		return 1;
		
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
				System.out.println("sono in cerca prenotazioe");
				for (EscursioneSalvata escursioneSalvata : p.getViaggioBean().getEscursioneSalvatas()) {
					System.out.println("esc in viaggiobean "+escursioneSalvata.getNome());

				};

				
				p1.setViaggio(Converter.ViaggioToDTO(p.getViaggioBean()));
				myDTOList.add(p1);
			  }
		   }
		return myDTOList;	
	}
	
	
	public int saveHotelSalvato(HotelDTO hoteldto)
	{
		HotelSalvato hotel_s = new HotelSalvato(hoteldto);
		em.persist(hotel_s);
		em.flush();
		return em.find(HotelSalvato.class, hotel_s.getId()).getId();
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
	
	public int saveVoloSalvato(VoloDTO volodto)
	{
		VoloSalvato volo = new VoloSalvato(volodto);
		em.persist(volo);
		em.flush();
		return em.find(VoloSalvato.class, volo.getId()).getId();
	}

	@Override
	public ViaggioDTO cercaViaggioById(int id) {
			try { Viaggio viaggio = em.createNamedQuery("Viaggio.findById", Viaggio.class)
						.setParameter("id", id)
						.getSingleResult();
				return Converter.ViaggioToDTO(viaggio);
			} catch (NoResultException e1){
		        return null;
			} catch (NullPointerException e2){
				return null;
			}
		
	}
	
	
	
}
