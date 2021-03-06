package com.traveldream.util;

import java.util.ArrayList;
import java.util.List;
import model.Escursione;
import model.EscursioneSalvata;
import model.Hotel;
import model.HotelSalvato;
import model.Invito;
import model.Pacchetto;
import model.Utente;
import model.Viaggio;
import model.Volo;
import model.VoloSalvato;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;

public class Converter {


	
	//-----------------------SINGLE ELEMENT ENTITY TO DTO CONVERTER--------------	
	
	
		public static PacchettoDTO PacchettoToDTO(Pacchetto p)
		{
			PacchettoDTO pdto = new PacchettoDTO();
			pdto.setData_fine(p.getData_fine());
			pdto.setData_inizio(p.getData_inizio());
			pdto.setDestinazione(p.getDestinazione());
			pdto.setNome(p.getNome());
			pdto.setPathtoImage(p.getImmagine());
			pdto.setId(p.getId());	
			pdto.setLista_hotel(EntitytoDTOHotels(p.getHotels()));
			pdto.setLista_escursioni(EntitytoDTOEscursione(p.getEscursiones()));
			pdto.setLista_voli(EntitytoDTOVolo(p.getVolos()));

			return pdto;
		}
		
		public static HotelDTO HotelToDTOSimple(Hotel h) {
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
		
		public static HotelDTO HotelToDTOSimple(HotelSalvato h) {
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
		
		public static HotelDTO HotelToDTOExtended(Hotel h) {
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
			hdto.setPacchettos(EntitytoDTOPacchetto(h.getPacchettos()));
			return hdto;
		}
		
		public static VoloDTO VoloToDTO(Volo v) {
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
		
		public static VoloDTO VoloToDTO(VoloSalvato v) {
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
		
		public static EscursioneDTO EscursioneToDTO(Escursione e) {
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
		
		public static EscursioneDTO EscursioneToDTO(EscursioneSalvata e) {
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
		
		
		
	///------------------LIST ENTITY TO DTO CONVERTER-----------
		public static ArrayList<PacchettoDTO> EntitytoDTOPacchetto(List<Pacchetto> pacchettos){
			ArrayList<PacchettoDTO> listaPacchetti = new ArrayList<PacchettoDTO>();
			for (Pacchetto pacchetto: pacchettos) {
				PacchettoDTO nuovoDto = PacchettoToDTO(pacchetto);
				listaPacchetti.add(nuovoDto);
			}
			return listaPacchetti;
		}
		
		public static ArrayList<HotelDTO> EntitytoDTOHotels(List<Hotel> hotels){
	          ArrayList<HotelDTO> listaHotel = new ArrayList<HotelDTO>();
	          for(Hotel h:hotels){
	                  HotelDTO nuovo = HotelToDTOSimple(h);
	                  listaHotel.add(nuovo);
	          }
	          return listaHotel;
	  }
		  
		public static ArrayList<EscursioneDTO> EntitytoDTOEscursioneS(List<EscursioneSalvata> list){
	          ArrayList<EscursioneDTO> listaesc = new ArrayList<EscursioneDTO>();
	          for(EscursioneSalvata e:list){
	                  EscursioneDTO nuovo = EscursioneToDTO(e);
	                  listaesc.add(nuovo);
	          }
	          return listaesc;
	  }
		  
		public static ArrayList<EscursioneDTO> EntitytoDTOEscursione(List<Escursione> escursioni){
	          ArrayList<EscursioneDTO> listaesc = new ArrayList<EscursioneDTO>();
	          for(Escursione e:escursioni){
	                  EscursioneDTO nuovo = EscursioneToDTO(e);
	                  listaesc.add(nuovo);
	          }
	          return listaesc;
	  }
		
		  public static ArrayList<VoloDTO> EntitytoDTOVolo(List<Volo> voli){
	          ArrayList<VoloDTO> listavolo = new ArrayList<VoloDTO>();
	          for(Volo v:voli){
	                  VoloDTO nuovo = VoloToDTO(v);
	                  listavolo.add(nuovo);
	          }
	          return listavolo;
	  }
		  
		 // ------------------------------------- Viaggio -----------------------------------------------
		  
		  public static ViaggioDTO ViaggioToDTO(Viaggio v)
			{
				ViaggioDTO vdto = new ViaggioDTO();
				vdto.setId(v.getId());	
				vdto.setData_fine(v.getData_fine());
				vdto.setData_inizio(v.getData_inizio());
				vdto.setNome(v.getNome());
				vdto.setHotel(HotelToDTOSimple(v.getHotelSalvato()));
				vdto.setVolo_andata(VoloToDTO(v.getVoloSalvato1()));
				vdto.setVolo_ritorno(VoloToDTO(v.getVoloSalvato2()));
				vdto.setLista_escursioni(EntitytoDTOEscursioneS(v.getEscursioneSalvatas()));

				return vdto;
			}
		  
		  public static InvitoDTO InvitoToDTO(Invito i){
			  InvitoDTO idto = new InvitoDTO();
			  idto.setId(i.getId());
			  idto.setStatus(i.getStatus());
			  idto.setUtente(UserToDTO(i.getUtenteBean()));
			  idto.setViaggio(ViaggioToDTO(i.getViaggioBean()));
			  idto.setAmico(i.getAmico());
			return idto;
		  }
		  
		 //------------------------------ Utente -------------------------------- 
		  public static UserDTO UserToDTO(Utente user) {
				UserDTO udto = new UserDTO();
				udto.setUsername(user.getUsername());
				udto.setFirstName(user.getNome());
				udto.setLastName(user.getCognome());
				udto.setEmail(user.getEmail());
				udto.setData(user.getData_di_nascita());
				return udto;
			}
		

	}


