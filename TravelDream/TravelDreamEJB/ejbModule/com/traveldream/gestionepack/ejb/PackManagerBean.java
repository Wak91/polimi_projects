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
import com.traveldream.util.Converter;;
/**
 * Session Bean implementation class PackManagerBean
 */
@Stateless
public class PackManagerBean implements PackManagerBeanLocal {

	@PersistenceContext
    private EntityManager em;
	
	@Resource
	private EJBContext context;

	
	public ArrayList <PacchettoDTO> getAllPack()
	{
		List <Pacchetto> mylist;
		ArrayList <PacchettoDTO> pdto = new ArrayList <PacchettoDTO> ();
		
		mylist = em.createNamedQuery("Pacchetto.findAll", Pacchetto.class).getResultList();
		for(Pacchetto p : mylist)
		   {
			pdto.add(Converter.PacchettoToDTO(p));
		   }
		return pdto;
	}
	
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
		for (HotelDTO hotelDTO : packetDTO.getLista_hotel()) {
			Hotel hotel =em.find(Hotel.class, hotelDTO.getId());
			hotel.getPacchettos().add(pacchetto);
		}
		for (VoloDTO voloDTO : packetDTO.getLista_voli()) {
			Volo volo =em.find(Volo.class, voloDTO.getId());
			volo.getPacchettos().add(pacchetto);
		}
		
		for (EscursioneDTO escursioneDTO : packetDTO.getLista_escursioni()) {
			Escursione escursione =em.find(Escursione.class, escursioneDTO.getId());
			escursione.getPacchettos().add(pacchetto);
		}
		em.persist(pacchetto);
		em.flush();
		
		
	}	
		
	
	public PacchettoDTO getPacchettoByID(int id)
	{
		return Converter.PacchettoToDTO(em.find(Pacchetto.class, id));
		
	}

	
	public void deletePacchetto(int id)
	{
		Pacchetto pacchetto = em.find(Pacchetto.class,id);
		for(Hotel h : pacchetto.getHotels()){
			h.getPacchettos().remove(pacchetto);
		}
		for(Volo volo :pacchetto.getVolos()){
			volo.getPacchettos().remove(pacchetto);
		}
		for (Escursione escursione: pacchetto.getEscursiones()){
			escursione.getPacchettos().remove(escursione);
		}

		em.remove(pacchetto);
		em.flush();
		
	}


	@Override
	/**
	 * Modifica il pacchetto e aggiorna la lista di pacchetti a cui si riferiscono i vari componenti che lo compongono(Volo.Hotel,Escursione)
	 */
	public void modifyPacchetto(PacchettoDTO packetDTO) {
		Pacchetto pacchetto = em.find(Pacchetto.class, packetDTO.getId());
		
		pacchetto.setNome(packetDTO.getNome());
		pacchetto.setDestinazione(packetDTO.getDestinazione());
		pacchetto.setData_inizio(packetDTO.getData_inizio());
		pacchetto.setData_fine(packetDTO.getData_fine());
		pacchetto.setImmagine(packetDTO.getPathtoImage());	
		
		//riaggiorno la lista di pacchetti presente negli hotel (che indica in che pacchetti è inserito) del pacchetto(Hotel.pacchettos)
		System.out.println("in modify packet ");
		for (Hotel hotel : pacchetto.getHotels()) {
			System.out.println("in modify packet rimuovo "+hotel.getNome());
			hotel.getPacchettos().remove(pacchetto);
		}
		for (Hotel hotel :DTOtoEntityHotel(packetDTO.getLista_hotel()) ){
			System.out.println("in modify packet aggiungo "+hotel.getNome());

			hotel.getPacchettos().add(pacchetto);
		}		
		pacchetto.setHotels(DTOtoEntityHotel(packetDTO.getLista_hotel()));

		
		//riaggiorno la lista di pacchetti contenuta in escursione(che indica in che pacchetti e' inserita) prima rimuovo quelli vecchi poi aggiungo quelli nuovi
		//faccio in questo modo considerando che il numero di hotel in un pacchetto non sia troppo grosso
		
		for(Escursione escursione:pacchetto.getEscursiones()){
			escursione.getPacchettos().remove(pacchetto);
		}
		for (Escursione escursione : DTOtoEntityEscursione(packetDTO.getLista_escursioni())) {
			escursione.getPacchettos().add(pacchetto);
		}
		pacchetto.setEscursiones(DTOtoEntityEscursione(packetDTO.getLista_escursioni()));

		
		for (Volo volo : pacchetto.getVolos()) {
			volo.getPacchettos().remove(pacchetto);
		}
		for (Volo volo:DTOtoEntityVolo(packetDTO.getLista_voli())){
			volo.getPacchettos().add(pacchetto);
		}
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
	  
	    if (inizio != null && fine==null){
	    	predicates.add(
	    			qb.lessThanOrEqualTo(hotel.<Date>get("data_inizio"),inizio));   	
	    }
	    if (fine != null && inizio ==null){
	    	predicates.add(
	    			qb.greaterThanOrEqualTo(hotel.<Date>get("data_fine"),fine));   	
	    }
	    if(fine !=null && inizio !=null){
	    	Predicate inizioHotelInMezzoPacchetto = qb.between(hotel.<Date>get("data_inizio"), inizio,fine);
	        Predicate fineHotelInMezzoPacchetto = qb.between(hotel.<Date>get("data_fine"), inizio,fine);
	    	Predicate inizioHotelPrimaInizioPacchettoPredicate = qb.lessThanOrEqualTo(hotel.<Date>get("data_inizio"),inizio); 
	    	Predicate fineHoteldopofinePacchettoPredicate = qb.greaterThanOrEqualTo(hotel.<Date>get("data_fine"),fine); 

	    	Predicate HotelContienePacchetto = qb.and(inizioHotelPrimaInizioPacchettoPredicate,fineHoteldopofinePacchettoPredicate);
	        Predicate inizioOFineHotelInPredicateMezzoPacchettoPredicate = qb.or(inizioHotelInMezzoPacchetto,fineHotelInMezzoPacchetto);

	        Predicate DataOK = qb.or(HotelContienePacchetto,inizioOFineHotelInPredicateMezzoPacchettoPredicate);
	        predicates.add(DataOK);
	    }
	    
	    c.where(predicates.toArray(new Predicate[]{}));
	    TypedQuery<Hotel> q = em.createQuery(c);
	    List<Hotel> hotels = q.getResultList();
	    return Converter.EntitytoDTOHotels(hotels);
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
	    return Converter.EntitytoDTOEscursione(escursiones);
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
	    return Converter.EntitytoDTOVolo(volos);
	}
	
	
	///------------------LIST DTO TO ENTITY CONVERTER-----------
//non puo essere spostato in converter perche c'e la necessita di chiamare Entity manager
	
	private List<Escursione> DTOtoEntityEscursione(List<EscursioneDTO> escursioneDTOs){
         ArrayList<Escursione> listaEscursioni = new ArrayList<Escursione>();
         for (EscursioneDTO escursioneDTO :escursioneDTOs){
                 Escursione nuovaesc = em.find(Escursione.class, escursioneDTO.getId());
                 listaEscursioni.add(nuovaesc);
         }
         return listaEscursioni;
 }
	  private  List<Volo> DTOtoEntityVolo(List<VoloDTO> voloDTOs){
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
        	  	System.out.println(hotelDTO.getNome()+" "+hotelDTO.getId());
                  Hotel nuovohotel = em.find(Hotel.class, hotelDTO.getId());
                  listaHotel.add(nuovohotel);
          }
          return listaHotel;
  }

	@Override
	public ArrayList<PacchettoDTO> getFilteredPacchetti(String destinazione, Date data_inizio,Date data_fine) {
		System.out.println("inizio viaggio"+data_inizio);
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Pacchetto> c  = qb.createQuery(Pacchetto.class);
		Root<Pacchetto> pacchetto = c.from(Pacchetto.class);
	   List<Predicate> predicates = new ArrayList<Predicate>(); 
	    if (destinazione != null && !destinazione.isEmpty()) {
	    	Predicate LuogoOK = qb.like(pacchetto.<String>get("destinazione"), destinazione+"%");

	        predicates.add(LuogoOK);
	    }
	  
	    System.out.println("data inizio pack "+pacchetto.<Date>get("data_inizio"));
	    if (data_inizio != null){
	    	predicates.add(
	    			qb.lessThanOrEqualTo(pacchetto.<Date>get("data_inizio"),data_inizio));   	
	    }
	    if (data_fine != null){
	    	predicates.add(
	    			qb.greaterThanOrEqualTo(pacchetto.<Date>get("data_fine"),data_fine));   	
	    }
	    
	    c.where(predicates.toArray(new Predicate[]{}));
	    TypedQuery<Pacchetto> q = em.createQuery(c);
	    List<Pacchetto> pacchettos = q.getResultList();
	    return Converter.EntitytoDTOPacchetto(pacchettos);
	}
	
}
