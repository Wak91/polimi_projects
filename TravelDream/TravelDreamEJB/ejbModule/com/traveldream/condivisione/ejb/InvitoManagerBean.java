package com.traveldream.condivisione.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import model.Group;
import model.Invito;
import model.Pacchetto;
import model.Prenotazione;
import model.Utente;
import model.UtenteGruppo;
import model.Viaggio;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;
import com.traveldream.util.Converter;


@Stateless
public class InvitoManagerBean implements InvitoManagerBeanLocal{
	
	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	

	@Override
	public void submit(InvitoDTO invitoDTO) {
		// TODO Auto-generated method stub
		
			Invito invite = new Invito();
			invite.setStatus(invitoDTO.getStatus());
			invite.setAmico(invitoDTO.getAmico());
			invite.setUtenteBean(this.DTOtoEntityUtente(invitoDTO.getUtente()));
			invite.setViaggioBean(this.DTOtoEntityViaggio(invitoDTO.getViaggio()));;
			System.out.println(invite.getViaggioBean().getHotelSalvato().getLuogo());
	        em.persist(invite);	
	        em.flush();
	        
			}
		
	
	private Viaggio DTOtoEntityViaggio(ViaggioDTO viaggio) {
		return em.find(Viaggio.class, viaggio.getId());
	}
	 
	private Utente DTOtoEntityUtente(UserDTO utente) {
		return em.find(Utente.class, utente.getUsername());
	}
	
	@Override
	public ArrayList <InvitoDTO> cercaInvito(UserDTO udto)
	{	
		System.out.println("udto" +udto.getUsername());
		List <Invito> myList = em.createNamedQuery("Invito.findAll", Invito.class).getResultList();
		ArrayList <InvitoDTO> myDTOList = new ArrayList <InvitoDTO>();
		
		for(Invito i : myList )
		   {
			System.out.println("i:" +i.getUtenteBean().getUsername());
			if(i.getUtenteBean().getUsername().equals(udto.getUsername()))
			  {
				InvitoDTO i1 = new InvitoDTO();
				i1.setAmico(i.getAmico());
				i1.setStatus(i.getStatus());
				i1.setId(i.getId());
				i1.setViaggio(Converter.ViaggioToDTO(i.getViaggioBean()));
				
				myDTOList.add(i1);
			  }
		   }
		return myDTOList;
		
	}


	@Override
	public InvitoDTO cercaInvitoById(int id_amico, String amico) {
		// TODO Auto-generated method stub
		try {
			Invito invito = em.createNamedQuery("Invito.findByIdMail", Invito.class)
					.setParameter("id", id_amico)
					.setParameter("amico", amico)
					.getSingleResult();
			return Converter.InvitoToDTO(invito);
		} catch (NoResultException e1){
	        return null;
		} catch (NullPointerException e2){
			return null;
		}
	}


	@Override
	public void cambiaStato(InvitoDTO idto) {
		// TODO Auto-generated method stub
		Invito invito = em.createNamedQuery("Invito.findByIdMail", Invito.class)
				.setParameter("id", idto.getId())
				.setParameter("amico", idto.getAmico())
				.getSingleResult();
		invito.setStatus(idto.getStatus());
		em.merge(invito);

	}


	@Override
	public InvitoDTO cercaInvitoById(int id) {
		// TODO Auto-generated method stub
		try {
			Invito invito = em.createNamedQuery("Invito.findById", Invito.class)
					.setParameter("id", id)
					.getSingleResult();
			return Converter.InvitoToDTO(invito);
		} catch (NoResultException e1){
	        return null;
		} catch (NullPointerException e2){
			return null;
		}
	}


	@Override
	public void eliminaInvito(int id) {
		// TODO Auto-generated method stub
		Invito invito = em.createNamedQuery("Invito.findById", Invito.class)
				.setParameter("id", id)
				.getSingleResult();
		em.remove(invito);
	}
	
		
}


