package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;

import model.Escursione;
import model.Hotel;
import model.Pacchetto;
//import model.Pacchetto;
import model.Volo;

import com.traveldream.gestionecomponente.ejb.ComponentManagerBean;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

/**
 * Session Bean implementation class PackManagerBean
 */
@Stateless
public class PackManagerBean implements PackManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;


	@Override
	public void createPacket(PacchettoDTO packetDTO) {

		Pacchetto pacchetto = new Pacchetto();
		pacchetto.setNome(packetDTO.getNome());
		pacchetto.setDestinazione(packetDTO.getDestinazione());
		pacchetto.setData_inizio(packetDTO.getData_inizio());
		pacchetto.setData_fine(packetDTO.getData_fine());
		pacchetto.setImmagine(packetDTO.getPathtoImage());
		pacchetto.setHotels(DTOtoEntityHotel(packetDTO.getLista_hotel()));
		pacchetto.setEscursiones(DTOtoEntityEscursione(packetDTO.getLista_escursioni()));
		pacchetto.setVolos(DTOtoEntityVolo(packetDTO.getLista_voli()));
		em.persist(pacchetto);
		em.flush();
		
		
	}	
	
	
	
	public ArrayList <PacchettoDTO> getAllPack()
	{
		List <Pacchetto> mylist;
		ArrayList <PacchettoDTO> pdto = new ArrayList <PacchettoDTO> ();
		
		mylist = em.createNamedQuery("Pacchetto.findAll", Pacchetto.class).getResultList();
		for(Pacchetto p : mylist)
		   {
			pdto.add(PacchettoToDTO(p));
		   }
		return pdto;
		
	}
	
	public PacchettoDTO getPacchettoByID(int id)
	{
		return PacchettoToDTO(em.find(Pacchetto.class, id));
		
	}
	
	private PacchettoDTO PacchettoToDTO(Pacchetto p)
	{
		ArrayList <HotelDTO> hdto_list = new ArrayList <HotelDTO>();
		ArrayList <EscursioneDTO> edto_list = new ArrayList <EscursioneDTO>();
		ArrayList <VoloDTO> vdto_list = new ArrayList <VoloDTO>();
		
		PacchettoDTO pdto = new PacchettoDTO();
		pdto.setData_fine(p.getData_fine());
		pdto.setData_inizio(p.getData_inizio());
		pdto.setDestinazione(p.getDestinazione());
		pdto.setNome(p.getNome());
		pdto.setPathtoImage(p.getImmagine());
		pdto.setId(p.getId());	
		
		for(Escursione e: p.getEscursiones())
		   {
			 edto_list.add(EscursioneToDTO(e));
		   }

		for(Hotel h: p.getHotels())
		   {
			 hdto_list.add(HotelToDTO(h));
		   }
		
		for(Volo v: p.getVolos())
		   {
			 vdto_list.add(VoloToDTO(v));
		   }
		
		pdto.setLista_hotel(hdto_list);
		pdto.setLista_voli(vdto_list);
		pdto.setLista_escursioni(edto_list);
		
		return pdto;
	}
	
	public void deletePacchetto(int id)
	{
		em.remove(em.find(Pacchetto.class,id));
		
	}
	
	public HotelDTO HotelToDTO(Hotel h) {
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
		return hdto;
 
	}
	
	public VoloDTO VoloToDTO(Volo v) {
		VoloDTO vdto = new VoloDTO();
		vdto.setId(v.getId());
		vdto.setCompagnia(v.getCompagnia());
		vdto.setCosto(v.getCosto());
		vdto.setData(v.getData());
		vdto.setLuogo_arrivo(v.getLuogo_arrivo());
		vdto.setLuogo_partenza(v.getLuogo_partenza());
		vdto.setImmagine(v.getImmagine());
		vdto.setId(v.getId());
		return vdto;
	}
	
	public EscursioneDTO EscursioneToDTO(Escursione e) {
		EscursioneDTO edto = new EscursioneDTO();
		edto.setId(e.getId());
		edto.setCosto(e.getCosto());
		edto.setData(e.getData());
		edto.setLuogo(e.getLuogo());
		edto.setNome(e.getNome());
		edto.setImmagine(e.getImmagine());
		edto.setId(e.getId());
		return edto;
	}

	@Override
	public void modifyPacchetto(PacchettoDTO packetDTO) {
		Pacchetto pacchetto = em.find(Pacchetto.class, packetDTO.getId());
		pacchetto.setNome(packetDTO.getNome());
		pacchetto.setDestinazione(packetDTO.getDestinazione());
		pacchetto.setData_inizio(packetDTO.getData_inizio());
		pacchetto.setData_fine(packetDTO.getData_fine());
		pacchetto.setImmagine(packetDTO.getPathtoImage());
		
		pacchetto.setHotels(DTOtoEntityHotel(packetDTO.getLista_hotel()));
		pacchetto.setEscursiones(DTOtoEntityEscursione(packetDTO.getLista_escursioni()));
		pacchetto.setVolos(DTOtoEntityVolo(packetDTO.getLista_voli()));
		em.merge(pacchetto);
		em.flush();
		
	}
	public ArrayList<HotelDTO> getListaHotelCompatibili(String citta, Date inizio, Date fine) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Hotel> c  = qb.createQuery(Hotel.class);
		Root<Hotel> hotel = c.from(Hotel.class);
	   List<Predicate> predicates = new ArrayList<Predicate>(); 
	    System.out.println("citta in bean"+citta);
	    if (citta != null && !citta.isEmpty()) {
	    	System.out.println("adding citta predicate");
	        predicates.add(qb.equal(hotel.get("luogo"), citta));
	    }
	  
	    if (inizio != null){
	    	predicates.add(
	    			qb.greaterThanOrEqualTo(hotel.<Date>get("data_inizio"),inizio));   	
	    }
	    if (fine != null){
	    	predicates.add(
	    			qb.lessThanOrEqualTo(hotel.<Date>get("data_fine"),fine));   	
	    }
	    
	    c.where(predicates.toArray(new Predicate[]{}));

	    TypedQuery<Hotel> q = em.createQuery(c);

	    List<Hotel> hotels = q.getResultList();
	    return EntitytoDTOHotels(hotels);
	}
		
///------------------DTO CONVERTER--------
	  private ArrayList<HotelDTO> EntitytoDTOHotels(List<Hotel> hotels){
          ArrayList<HotelDTO> listaHotel = new ArrayList<HotelDTO>();
          for(Hotel h:hotels){
                  HotelDTO nuovo = HotelToDTO(h);
                  listaHotel.add(nuovo);
          }
          return listaHotel;
  }
	
	
	private List<Escursione> DTOtoEntityEscursione(List<EscursioneDTO> escursioneDTOs){
         ArrayList<Escursione> listaEscursioni = new ArrayList<Escursione>();
         for (EscursioneDTO escursioneDTO :escursioneDTOs){
                 Escursione nuovaesc = em.find(Escursione.class, escursioneDTO.getId());
                 listaEscursioni.add(nuovaesc);
         }
         return listaEscursioni;
 }
	 private List<Volo> DTOtoEntityVolo(List<VoloDTO> voloDTOs){
         ArrayList<Volo> listavolo = new ArrayList<Volo>();
         for (VoloDTO voloDTO :voloDTOs){
                 Volo nuovovolo = em.find(Volo.class, voloDTO.getId());
                 listavolo.add(nuovovolo);
         }
         return listavolo;
 }
	  private List<Hotel> DTOtoEntityHotel(List<HotelDTO> hotelDTOs){
          ArrayList<Hotel> listaHotel = new ArrayList<Hotel>();
          for (HotelDTO hotelDTO : hotelDTOs){
                  Hotel nuovohotel = em.find(Hotel.class, hotelDTO.getId());
                  listaHotel.add(nuovohotel);
          }
          return listaHotel;
  }
	

}
