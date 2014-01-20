package com.traveldream.gestionecomponente.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.traveldream.util.Converter;

import model.Escursione;
import model.EscursioneSalvata;
import model.Hotel;
import model.HotelSalvato;
import model.Pacchetto;
import model.Volo;
import model.VoloSalvato;

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
	
	public int saveHotelSalvato(HotelDTO hoteldto)
	{
		HotelSalvato hotel_s = new HotelSalvato(hoteldto);
		em.persist(hotel_s);
		em.flush();
		return em.find(HotelSalvato.class, hotel_s.getId()).getId();
	}
	
	@Override
	public void saveVolo(VoloDTO volodto) {
		Volo volo = new Volo(volodto);
		em.persist(volo);
		
	}
	
	public int saveVoloSalvato(VoloDTO volodto)
	{
		VoloSalvato volo = new VoloSalvato(volodto);
		em.persist(volo);
		em.flush();
		return em.find(VoloSalvato.class, volo.getId()).getId();
	}
	
	@Override
	public void saveEscursione(EscursioneDTO escursionedto) {
		Escursione escursione= new Escursione(escursionedto);
		em.persist(escursione);
		
	}
	
	public int saveEscursioneSalvata(EscursioneDTO escursioneDTO)
	{
		EscursioneSalvata escursione = new EscursioneSalvata(escursioneDTO);
		em.persist(escursione);
		em.flush();
		return em.find(EscursioneSalvata.class, escursione.getId()).getId();
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
			 myDTOlist.add(this.HotelToDTOExtended(h));
		    }
		return myDTOlist;
	}
	
	public HotelDTO getHotelById(int id)
	{
		Hotel result;
		result = em.createNamedQuery("Hotel.findbyId", Hotel.class).setParameter("d", id).getSingleResult();
		return HotelToDTOExtended(result);
	}

	public void modificaHotel(HotelDTO h)
	{
		Hotel result;
		result = em.createNamedQuery("Hotel.findbyId", Hotel.class).setParameter("d", h.getId()).getSingleResult();
		result.setNome(h.getNome());
		result.setCosto_giornaliero(h.getCosto_giornaliero());
		result.setData_inizio(h.getData_inizio());
		result.setData_fine(h.getData_fine());
		result.setLuogo(h.getLuogo());
		result.setStelle(h.getStelle());
		result.setImmagine(h.getHotelImg());
		result.setId(h.getId());
		em.merge(result);
	}
	
	public void eliminaHotel(int hid)
	{
		List<Pacchetto> pacchetti = em.createNamedQuery("Pacchetto.findAll", Pacchetto.class).getResultList();
		for (Pacchetto pacchetto :pacchetti){
			List<Hotel> toremoveHotels =new ArrayList<Hotel>();
			Iterator<Hotel> iterator =pacchetto.getHotels().iterator();
			while (iterator.hasNext()) {
				Hotel hotel = (Hotel) iterator.next();
				if (hotel.getId()==hid) {
					toremoveHotels.add(hotel);
				}
			}
			for (Hotel hremove : toremoveHotels){
				pacchetto.getHotels().remove(hremove);
			}
			
		}

		em.remove(em.createNamedQuery("Hotel.findbyId", Hotel.class).setParameter("d", hid).getSingleResult());
		
	}
	/**
	 * Questo metodo converte da entita HOTEL a DTO ritornando anche l'insieme di pacchetti a cui appartiene
	 * è perciò diverso dal metodo del Converter HotelToDTOSimple che non ritorna la lista dei pacchetti in cui e' incluso
	 * @param h Hotel da convertire
	 * @return HOTELDTO
	 */
	public  HotelDTO HotelToDTOExtended(Hotel h) {
		HotelDTO hdto = new HotelDTO();
		hdto.setId(h.getId());
		hdto.setCosto_giornaliero(h.getCosto_giornaliero());
		hdto.setData_fine(h.getData_fine());
		hdto.setData_inizio(h.getData_inizio());
		hdto.setLuogo(h.getLuogo());
		hdto.setNome(h.getNome());
		hdto.setStelle(h.getStelle());
		hdto.setHotelImg(h.getImmagine());
		hdto.setId(h.getId());
		hdto.setPacchettos(Converter.EntitytoDTOPacchetto(h.getPacchettos()));
		return hdto;
	}
	
	public ArrayList<VoloDTO> getAllVolo()
	{
		List <Volo> myList;
		ArrayList <VoloDTO> myDTOlist = new ArrayList <VoloDTO> ();
		myList = em.createNamedQuery("Volo.findAll", Volo.class).getResultList();
		for (Volo v : myList)
		    {
			 myDTOlist.add(this.VoloToDTOExtended(v));
		    }
		return myDTOlist;
	}


	public VoloDTO getVoloById(int id)
	{
		Volo result;
		result = em.createNamedQuery("Volo.findbyId", Volo.class).setParameter("d", id).getSingleResult();
		return VoloToDTOExtended(result);
	}
	
	public VoloDTO VoloToDTOExtended(Volo v) {
		VoloDTO vdto = new VoloDTO();
		vdto.setId(v.getId());
		vdto.setCompagnia(v.getCompagnia());
		vdto.setCosto(v.getCosto());
		vdto.setData(v.getData());
		vdto.setLuogo_arrivo(v.getLuogo_arrivo());
		vdto.setLuogo_partenza(v.getLuogo_partenza());
		vdto.setImmagine(v.getImmagine());
		vdto.setId(v.getId());
		vdto.setPacchettos(Converter.EntitytoDTOPacchetto(v.getPacchettos()));
		return vdto;
	}
	

	public void modificaVolo(VoloDTO v)
	{
		Volo result;
		result = em.createNamedQuery("Volo.findbyId", Volo.class).setParameter("d", v.getId()).getSingleResult();
		result.setCompagnia(v.getCompagnia());
		result.setCosto(v.getCosto());
		result.setData(v.getData());
		result.setId(v.getId());
		result.setImmagine(v.getImmagine());
		result.setLuogo_arrivo(v.getLuogo_arrivo());
		result.setLuogo_partenza(v.getLuogo_partenza());
		em.merge(result);
	}
	
	public void eliminaVolo(int id)
	{
		List<Pacchetto> pacchetti = em.createNamedQuery("Pacchetto.findAll", Pacchetto.class).getResultList();
		for (Pacchetto pacchetto :pacchetti){
			List<Volo> toremoveVolo =new ArrayList<Volo>();
			Iterator<Volo> iterator =pacchetto.getVolos().iterator();
			while (iterator.hasNext()) {
				Volo volo = (Volo) iterator.next();
				if (volo.getId()==id) {
					toremoveVolo.add(volo);
				}
			}
			for (Volo vremove : toremoveVolo){
				pacchetto.getVolos().remove(vremove);
			}
			
		}
		em.remove(em.createNamedQuery("Volo.findbyId", Volo.class).setParameter("d", id).getSingleResult());
		
	}
	
	
	public ArrayList<EscursioneDTO> getAllEscursione()
	{
		List <Escursione> myList;
		ArrayList <EscursioneDTO> myDTOlist = new ArrayList <EscursioneDTO> ();
		myList = em.createNamedQuery("Escursione.findAll", Escursione.class).getResultList();
		for (Escursione e : myList)
		    {
			 myDTOlist.add(this.EscursioneToDTOExtended(e));
		    }
		return myDTOlist;
	}

	public EscursioneDTO EscursioneToDTOExtended(Escursione e) {
		EscursioneDTO edto = new EscursioneDTO();
		edto.setId(e.getId());
		edto.setCosto(e.getCosto());
		edto.setData(e.getData());
		edto.setLuogo(e.getLuogo());
		edto.setNome(e.getNome());
		edto.setImmagine(e.getImmagine());
		edto.setPacchettos(Converter.EntitytoDTOPacchetto(e.getPacchettos()));
		return edto;
	}
	
	public void modificaEscursione(EscursioneDTO e)
	{
		Escursione result;
		result = em.createNamedQuery("Escursione.findbyId", Escursione.class).setParameter("d", e.getId()).getSingleResult();
		result.setCosto(e.getCosto());
		result.setData(e.getData());
		result.setId(e.getId());
		result.setImmagine(e.getImmagine());
		result.setLuogo(e.getLuogo());
		result.setNome(e.getNome());
		em.merge(result);
	}
	
	public void eliminaEscursione(int id)
	{

		List<Pacchetto> pacchetti = em.createNamedQuery("Pacchetto.findAll", Pacchetto.class).getResultList();
		for (Pacchetto pacchetto :pacchetti){
			List<Escursione> toremoveEsc =new ArrayList<Escursione>();
			Iterator<Escursione> iterator =pacchetto.getEscursiones().iterator();
			while (iterator.hasNext()) {
				Escursione esc = (Escursione) iterator.next();
				if (esc.getId()==id) {
					toremoveEsc.add(esc);
				}
			}
			for (Escursione eremove : toremoveEsc){
				pacchetto.getEscursiones().remove(eremove);
			}
			
		}
		em.remove(em.createNamedQuery("Escursione.findbyId", Escursione.class).setParameter("d",id).getSingleResult());
		
	}
	public EscursioneDTO getEscursioneById(int id)
	{
		Escursione result;
		result = em.createNamedQuery("Escursione.findbyId", Escursione.class).setParameter("d", id).getSingleResult();
		return EscursioneToDTOExtended(result);
	}


	
}
