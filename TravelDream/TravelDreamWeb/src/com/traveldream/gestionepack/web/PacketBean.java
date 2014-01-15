package com.traveldream.gestionepack.web;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

import com.traveldream.gestionecomponente.ejb.*;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;

@ManagedBean(name="PacketBean") 
@ViewScoped
public class PacketBean {

	@EJB
	private PackManagerBeanLocal PMB;
	@EJB
	private ComponentManagerBeanLocal CMB;
	
	private ArrayList <HotelDTO> selectedHotels;
	private ArrayList <VoloDTO>  selectedVolo;
	private ArrayList <EscursioneDTO> selectedEsc;
	
	private PacchettoDTO packet;
	private ArrayList <PacchettoDTO> packlist; 
    private UploadedFile imgPack;
	
	private ArrayList<HotelDTO> filteredHotels;
	private ArrayList<EscursioneDTO> filteredEscursiones;
	private ArrayList<VoloDTO> filteredVolos;
  

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
	
	public void getPacchettoById(int id)
	{	initBean();
		this.packet = PMB.getPacchettoByID(id);
		this.selectedEsc = (ArrayList<EscursioneDTO>) packet.getLista_escursioni();
		this.selectedHotels = (ArrayList<HotelDTO>) packet.getLista_hotel();
		
		for (HotelDTO hotelDTO : selectedHotels) {
			System.out.println("Selezionato hotel" + hotelDTO.getNome());
			
		}
		this.selectedVolo = (ArrayList<VoloDTO>) packet.getLista_voli();
		
	}
	
	
	public String PrelevaSelezionatiECrea()
	{
		packet.setLista_escursioni(selectedEsc);
		packet.setLista_hotel(selectedHotels);
		packet.setLista_voli(selectedVolo);
		
		 String filename = FilenameUtils.getName(imgPack.getFileName());
		  if(filename.equals(""))
		    {
			 packet.setPathtoImage("Pdefault.jpg");}
		  else
		    {
		  InputStream inputStr = null;
		    try {
		        inputStr = imgPack.getInputstream();
		    } catch (IOException e) {
		        //log error
		    }

		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    String directory = externalContext.getInitParameter("uploadDirectory");
		    File destFile = new File(directory, filename);

		    //use org.apache.commons.io.FileUtils to copy the File
		    try {
		        FileUtils.copyInputStreamToFile(inputStr, destFile);
		    } catch (IOException e) {
		        //log error
		    }
		packet.setPathtoImage(imgPack.getFileName());
		    }
		
		PMB.createPacket(packet);
		return "impadd.xhtml?faces-redirect=true";
		
	}
	
	public void deletePacchetto(int id)
	{ 	
		PMB.deletePacchetto(id);
	}
	
	public String  modificaPacchetto(int id) {
		packet.setLista_escursioni(selectedEsc);
		packet.setLista_hotel(selectedHotels);
		packet.setLista_voli(selectedVolo);
		System.out.println("ID packet "+packet.getId());
		PMB.modifyPacchetto(packet);
		return "impack.xhtml?faces-redirect=true";
	}
	

	public ArrayList<EscursioneDTO> getFilteredEscursiones() {
		return filteredEscursiones;
	}

	public void setFilteredEscursiones(ArrayList<EscursioneDTO> filteredEscursiones) {
		this.filteredEscursiones = filteredEscursiones;
	}

	public ArrayList<VoloDTO> getFilteredVolos() {
		return filteredVolos;
	}

	public void setFilteredVolos(ArrayList<VoloDTO> filteredVolos) {
		this.filteredVolos = filteredVolos;
	}

	public UploadedFile getImgPack() {
		return imgPack;
	}

	public void setImgPack(UploadedFile imgPack) {
		this.imgPack = imgPack;
	}
}
