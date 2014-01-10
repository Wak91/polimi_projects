package com.traveldream.gestionepack.web;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.traveldream.gestionecomponente.ejb.*;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;

@ManagedBean(name="PacketBean") 
@SessionScoped
public class PacketBean {

	@EJB
	private PackManagerBeanLocal PMB;
	@EJB
	private ComponentManagerBeanLocal CMB;
	
	private ArrayList <HotelDTO> selectedHotels;
	private ArrayList<HotelDTO> filteredHotels;
	

	private ArrayList <VoloDTO>  selectedVolo;
	private ArrayList <EscursioneDTO> selectedEsc;
	
	private PacchettoDTO packet;
	private ArrayList <PacchettoDTO> packlist; 
	
  

	private HotelDataModel hotelModels;
    private VoloDataModel  voloModels;
    private EscDataModel escModels;
    private PacchettoDataModel packModels;
	
    //---INIZIALIZZAZIONE BEAN---
    
   

	public PacketBean()
	{

	selectedHotels  = new ArrayList <HotelDTO>(); // questo per tenere traccia di quelli selezionati
	// MAI METTERE CMB QUA DENTRO, NON E' ANCORA STATO CREATO E QUINDI QUALSIASI COSA
	// FAI TI SBATTE UN SIMPATICO NULL POINTER IN FACCIA 
	}
	
	public void initBean()
	{
	     packet = new PacchettoDTO();
		 setHotelModels(new HotelDataModel(CMB.getAllHotel()));	
		 setVoloModels(new VoloDataModel(CMB.getAllVolo()));
		 setEscModels(new EscDataModel(CMB.getAllEscursione()));
		 setPackModels(new PacchettoDataModel(PMB.getAllPack()));
		 packlist = PMB.getAllPack();
	}
	//---FUNZIONI PER PACCHETTI---------------------
	 public PacchettoDataModel getPackModels() {
			return packModels;
		}

	 public void setPackModels(PacchettoDataModel packModels) {
			this.packModels = packModels;
		}
	  public PacchettoDTO getPacket() {
			return packet;
		}

	  public void setPacket(PacchettoDTO packet) {
			this.packet = packet;
		}
	  public ArrayList <PacchettoDTO> getPacklist() {
			return packlist;
		}

	  public void setPacklist(ArrayList <PacchettoDTO> packlist) {
			this.packlist = packlist;
		}
	
	//---FUNZIONI PER I VOLI-----------------------------------------
	
	public ArrayList<VoloDTO> getSelectedVolo() {
		return selectedVolo;
	}

	public void setSelectedVolo(ArrayList<VoloDTO> selectedVolo) {
		this.selectedVolo = selectedVolo;
	}

	public VoloDataModel getVoloModels() {
		return voloModels;
	}

	public void setVoloModels(VoloDataModel voloModels) {
		this.voloModels = voloModels;
	}
	//---------------------------------------------------------------
	
	//---FUNZIONI PER HOTEL-----------------------------------------
	
	public ArrayList<HotelDTO> getSelectedHotels() {
		return selectedHotels;
	}

	public void setSelectedHotels(ArrayList<HotelDTO> selectedHotels) {
		this.selectedHotels = selectedHotels;
	}
	
	public ArrayList<HotelDTO> getFilteredHotels() {
		return filteredHotels;
	}

	public void setFilteredHotels(ArrayList<HotelDTO> filteredHotels) {
		this.filteredHotels = filteredHotels;
	}

	public HotelDataModel getHotelModels() {
		return hotelModels;
	}

	public void setHotelModels(HotelDataModel hotelModels) {
		this.hotelModels = hotelModels;
	}
	//---------------------------------------------------------------
	
	//---FUNZIONI PER ESCURSIONI-----------------------------------------
	
	public ArrayList <EscursioneDTO> getSelectedEsc() {
		return selectedEsc;
	}

	public void setSelectedEsc(ArrayList <EscursioneDTO> selectedEsc) {
		this.selectedEsc = selectedEsc;
	}

	public EscDataModel getEscModels() {
		return escModels;
	}

	public void setEscModels(EscDataModel escModels) {
		this.escModels = escModels;
	}
	//---------------------------------------------------------------
	
	public void PrelevaSelezionatiECrea()
	{
		packet.setLista_escursioni(selectedEsc);
		packet.setLista_hotel(selectedHotels);
		packet.setLista_voli(selectedVolo);
		
		PMB.createPacket(packet);
		
	}
	
	

	public void getPacchettoById(int id)
	{
		
		
	}
}
