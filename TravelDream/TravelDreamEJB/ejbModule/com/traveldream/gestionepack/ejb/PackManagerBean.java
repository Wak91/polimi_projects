package com.traveldream.gestionepack.ejb;

import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;






import model.Escursione;
import model.Hotel;
import model.Pacchetto;
//import model.Pacchetto;
import model.Volo;

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

}
