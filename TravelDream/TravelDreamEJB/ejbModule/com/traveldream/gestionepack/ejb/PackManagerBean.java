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
	/**
	 * Metodo che restituisce la lista degli hotel compatibili con le date e la citta'  crea una query in modo dinamico 
	 * in funzione del fatto che i campi inizio fine e citta siano nulli
	 * la citta deve essere la stessa del pacchetto
	 * l'inizio deve essere succissivo e la fine precedente
	 */
	public ArrayList<HotelDTO> getListaHotelCompatibili(String citta, Date inizio, Date fine) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Hotel> c  = qb.createQuery(Hotel.class);
		Root<Hotel> hotel = c.from(Hotel.class);
	   List<Predicate> predicates = new ArrayList<Predicate>(); 
	    if (citta != null && !citta.isEmpty()) {
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
	/**
	 * Metodo che restituisce la lista delle Escursioni compatibili con le date e la citta'  crea una query in modo dinamica 
	 * in funzione del fatto che i campi inizio fine e citta siano nulli
	 * la citta deve essere la stessa del pacchetto
	 * l'inizio deve essere succissivo e la fine precedente
	 */
	public ArrayList<EscursioneDTO> getListaEscursioniCompatibili(String citta, Date inizio, Date fine) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Escursione> c  = qb.createQuery(Escursione.class);
		Root<Escursione> escursione = c.from(Escursione.class);
	   List<Predicate> predicates = new ArrayList<Predicate>(); 
	    if (citta != null && !citta.isEmpty()) {
	        predicates.add(qb.equal(escursione.get("luogo"), citta));
	    }
	  
	    if (inizio != null){
	    	predicates.add(
	    			qb.greaterThanOrEqualTo(escursione.<Date>get("data"),inizio));   	
	    }
	    if (fine != null){
	    	predicates.add(
	    			qb.lessThanOrEqualTo(escursione.<Date>get("data"),fine));   	
	    }
	    
	    c.where(predicates.toArray(new Predicate[]{}));
	    TypedQuery<Escursione> q = em.createQuery(c);
	    List<Escursione> escursiones = q.getResultList();
	    return EntitytoDTOEscursione(escursiones);
	}
	
	public ArrayList<VoloDTO> getListaVoliCompatibili(String citta, Date inizio, Date fine) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Volo> c  = qb.createQuery(Volo.class);
		Root<Volo> volo = c.from(Volo.class);
	   List<Predicate> predicates = new ArrayList<Predicate>(); 
	    if (citta != null && !citta.isEmpty()) {
	    	Predicate partenza = qb.equal(volo.get("luogo_partenza"), citta);
	    	Predicate arrivo = qb.equal(volo.get("luogo_arrivo"), citta);
	    	Predicate partenzaOrArrivo = qb.or(partenza,arrivo);
	        predicates.add(partenzaOrArrivo);
	    }
	  
	    if (inizio != null){
	    	predicates.add(
	    			qb.greaterThanOrEqualTo(volo.<Date>get("data"),inizio));   	
	    }
	    if (fine != null){
	    	predicates.add(
	    			qb.lessThanOrEqualTo(volo.<Date>get("data"),fine));   	
	    }
	    
	    c.where(predicates.toArray(new Predicate[]{}));
	    TypedQuery<Volo> q = em.createQuery(c);
	    List<Volo> volos = q.getResultList();
	    return EntitytoDTOVolo(volos);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<HotelDTO> getListaHotelCompatibili2(String citta, Date inizio, Date fine) {
		List<Hotel> hotels = em.createQuery("SELECT h FROM Hotel h WHERE h.luogo LIKE citta and h.data_inizio BETWEEN :start AND :end AND"
        		+ " h.data_fine BETWEEN :start AND :end")
                    .setParameter("citta", citta)
                    .setParameter("start", inizio, TemporalType.TIMESTAMP)
                    .setParameter("end", fine, TemporalType.TIMESTAMP)
                    .getResultList();
        ArrayList<HotelDTO> listaHotel = EntitytoDTOHotels(hotels);
        return listaHotel;
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
	  
	  private ArrayList<EscursioneDTO> EntitytoDTOEscursione(List<Escursione> escursioni){
          ArrayList<EscursioneDTO> listaesc = new ArrayList<EscursioneDTO>();
          for(Escursione e:escursioni){
                  EscursioneDTO nuovo = EscursioneToDTO(e);
                  listaesc.add(nuovo);
          }
          return listaesc;
  }
	  
	  private ArrayList<VoloDTO> EntitytoDTOVolo(List<Volo> voli){
          ArrayList<VoloDTO> listavolo = new ArrayList<VoloDTO>();
          for(Volo v:voli){
                  VoloDTO nuovo = VoloToDTO(v);
                  listavolo.add(nuovo);
          }
          return listavolo;
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
		public HotelDTO HotelToDTO(Hotel h) {
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
		
		public VoloDTO VoloToDTO(Volo v) {
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
		
		public EscursioneDTO EscursioneToDTO(Escursione e) {
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
