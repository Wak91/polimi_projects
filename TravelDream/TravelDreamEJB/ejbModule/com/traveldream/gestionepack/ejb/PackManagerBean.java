package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
		Hotel hotel;
		Volo volo;
		Escursione escursione;
		Pacchetto pacchetto = new Pacchetto();
		pacchetto.setNome(packetDTO.getNome());
		pacchetto.setDestinazione(packetDTO.getDestinazione());
		pacchetto.setData_inizio(packetDTO.getData_inizio());
		pacchetto.setData_fine(packetDTO.getData_fine());
		pacchetto.setImmagine(packetDTO.getPathtoImage());
		em.persist(pacchetto);
		em.flush();
		pacchetto = em.find(Pacchetto.class, pacchetto.getId());
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXPacchettoID ="+pacchetto.getId());

		for (HotelDTO hotelDTO : packetDTO.getLista_hotel()) {
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXHotelID ="+hotelDTO.getId());
			hotel = em.find(Hotel.class, hotelDTO.getId());
			
			pacchetto.addHotel(hotel);
			hotel.addPacchetto(pacchetto);
		}
		for (VoloDTO voloDTO : packetDTO.getLista_voli()) {
			volo = em.find(Volo.class, voloDTO.getId());
			pacchetto.addVolo(volo);
			volo.addPacchetto(pacchetto);
		}
		for (EscursioneDTO escursioneDTO :packetDTO.getLista_escursioni()){
			escursione = em.find(Escursione.class, escursioneDTO.getId());
			pacchetto.addEscursione(escursione);
			escursione.addPacchetto(pacchetto);
		}
		
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
