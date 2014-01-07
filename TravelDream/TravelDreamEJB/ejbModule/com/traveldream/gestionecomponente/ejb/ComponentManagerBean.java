package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Escursione;
import model.Hotel;
import model.Volo;

/**
 * Session Bean implementation class ComponentManagerBean
 */
@Stateless
public class ComponentManagerBean implements ComponentManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;
	
	/**
	 * creo un hotel e genero un ID univoco (controllo sulle date fatto a monte da pagina web )
	 */
	
	public void saveHotel(HotelDTO hoteldto) {

		Hotel hotel = new Hotel(hoteldto);	//aggiungo alla tabella Utente una tupla utilizzanto il DTO
		em.persist(hotel);	
	}
	
	@Override
	public void saveVolo(VoloDTO volodto) {
		Volo volo = new Volo(volodto);
		em.persist(volo);
		
	}
	
	@Override
	public void saveEscursione(EscursioneDTO escursionedto) {
		Escursione escursione= new Escursione(escursionedto);
		em.persist(escursione);
		
	}
	
	public void update() {
	}

	public void remove() {
	}
    
	//sfrutto la named query per ritornare tutti gli hotel dal DB
	//convertendoli in DTO per il managed bean
	public ArrayList<HotelDTO> getAllHotel()
	{
		List <Hotel> myList;
		ArrayList <HotelDTO> myDTOlist = new ArrayList <HotelDTO> ();
		myList = em.createNamedQuery("Hotel.findAll", Hotel.class).getResultList();
		for (Hotel h : myList)
		    {
			 myDTOlist.add(this.HotelToDTO(h));
		    }
		return myDTOlist;
	}

	private HotelDTO HotelToDTO(Hotel h) {
		HotelDTO hdto = new HotelDTO();
		hdto.setId(h.getId());
		hdto.setCosto_giornaliero(h.getCosto_giornaliero());
		hdto.setData_fine(h.getData_fine());
		hdto.setData_inizio(h.getData_inizio());
		hdto.setLuogo(h.getLuogo());
		hdto.setNome(h.getNome());
		hdto.setStelle(h.getStelle());
		hdto.setPathtoImage("");
		hdto.setId(h.getId());
		return hdto;
 
	}
	
	public ArrayList<VoloDTO> getAllVolo()
	{
		List <Volo> myList;
		ArrayList <VoloDTO> myDTOlist = new ArrayList <VoloDTO> ();
		myList = em.createNamedQuery("Volo.findAll", Volo.class).getResultList();
		for (Volo v : myList)
		    {
			 myDTOlist.add(this.VoloToDTO(v));
		    }
		return myDTOlist;
	}

	private VoloDTO VoloToDTO(Volo v) {
		VoloDTO vdto = new VoloDTO();
		vdto.setId(v.getId());
		vdto.setCompagnia(v.getCompagnia());
		vdto.setCosto(v.getCosto());
		vdto.setData(v.getData());
		vdto.setLuogo_arrivo(v.getLuogo_arrivo());
		vdto.setLuogo_partenza(v.getLuogo_partenza());
		vdto.setImmagine("");
		vdto.setId(v.getId());
		return vdto;
	}
	
	public ArrayList<EscursioneDTO> getAllEscursione()
	{
		List <Escursione> myList;
		ArrayList <EscursioneDTO> myDTOlist = new ArrayList <EscursioneDTO> ();
		myList = em.createNamedQuery("Escursione.findAll", Escursione.class).getResultList();
		for (Escursione e : myList)
		    {
			 myDTOlist.add(this.EscursioneToDTO(e));
		    }
		return myDTOlist;
	}

	private EscursioneDTO EscursioneToDTO(Escursione e) {
		EscursioneDTO edto = new EscursioneDTO();
		edto.setId(e.getId());
		edto.setCosto(e.getCosto());
		edto.setData(e.getData());
		edto.setLuogo(e.getLuogo());
		edto.setNome(e.getNome());
		edto.setImmagine("");
		edto.setId(e.getId());
		return edto;
	}



	
}
