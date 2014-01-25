package com.traveldream.gestionepack.web;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

import com.traveldream.gestionecomponente.ejb.*;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;

@ManagedBean(name="modPacketBean") 
@SessionScoped
public class modPacketBean {

	@EJB
	private PackManagerBeanLocal PMB;
	@EJB
	private ComponentManagerBeanLocal CMB;
	
	private ArrayList <HotelDTO> selectedHotels;
	private ArrayList <VoloDTO>  selectedVolo;
	private ArrayList <EscursioneDTO> selectedEsc;
	
	private PacchettoDTO packet; // pacchetto che verrà modificato
    private UploadedFile imgPack;
	
	private ArrayList<HotelDTO> filteredHotels;
	private ArrayList<EscursioneDTO> filteredEscursiones;
	private ArrayList<VoloDTO> filteredVolos;
  

	private HotelDataModel hotelModels;
    private VoloDataModel  voloModels;
    private EscDataModel escModels;
	
    
    private int target_id;
       
    	
    //---INIZIALIZZAZIONE BEAN---
    
   

	public modPacketBean()
	{
	selectedHotels  = new ArrayList <HotelDTO>(); 
	selectedVolo = new ArrayList <VoloDTO>();
	selectedEsc = new ArrayList <EscursioneDTO>();
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
	
    //----GETTER/SETTER PER PACKET--------------------------------------
	public UploadedFile getImgPack() {
		return imgPack;
	}

	public void setImgPack(UploadedFile imgPack) {
		this.imgPack = imgPack;
	}
	
	public PacchettoDTO getPacket() {
		return packet;
	}

	public void setPacket(PacchettoDTO packet) {
		this.packet = packet;
	}
	
	//---------------------------------------------------------------
	//FILTRI
	//---------------------------------------------------------------
	//Funzione per filtrare i componenti in ajax
	public void filterComponents(){
		
	
		if(packet.getDestinazione()==null){
			System.out.println("null destination");
		}
		else{
			
			System.out.println(packet.getDestinazione());
			if(packet.getDestinazione()==""){
				System.out.println("empty destination");
			}
		}
	
		if(packet.getData_fine()!=null & packet.getData_inizio()!=null){
			System.out.println(packet.getData_fine());
			System.out.println(packet.getData_inizio());
			System.out.println(packet.getDestinazione());
			if(packet.getDestinazione()==""){
				System.out.println("empty destination");
			}
		}
		 filteredEscursiones = PMB.getListaEscursioniCompatibili(packet.getDestinazione(), packet.getData_inizio(), packet.getData_fine());
		 filteredHotels = PMB.getListaHotelCompatibili(packet.getDestinazione(), packet.getData_inizio(), packet.getData_fine());
		 for (EscursioneDTO  escursioneDTO : filteredEscursiones) {
			System.out.println("data escursione"+escursioneDTO.getData());
			System.out.println("luogo escursione "+escursioneDTO.getLuogo());

		}
		 filteredVolos = PMB.getListaVoliCompatibili(packet.getDestinazione(), packet.getData_inizio(), packet.getData_fine());
		 for (VoloDTO voloDTO : filteredVolos) {
			System.out.println("data volo"+voloDTO.getData());
		}
		 setHotelModels(new HotelDataModel(filteredHotels));	
		 setVoloModels(new VoloDataModel(filteredVolos));
		 setEscModels(new EscDataModel(filteredEscursiones));
	}
	
	//Funzione per pescare il pacchetto giusto da modificare e 
	// tutti gli elementi già selezionati
	public void getPacchettoById()
	{	
		this.packet = PMB.getPacchettoByID(target_id);
		
		//Recupero tutti i componenti compatibili con questo pacchetto
		filtraHotel();
		filtraVoli();
		filtraEscursioni();
		
		//Seleziono di default quelli già dentro nel pack 
		this.selectedEsc = (ArrayList<EscursioneDTO>) packet.getLista_escursioni();
		this.selectedHotels = (ArrayList<HotelDTO>) packet.getLista_hotel();
		this.selectedVolo = (ArrayList<VoloDTO>) packet.getLista_voli();
	
		
	}
	
	//Funzione utilizzata per visualizzare nella tabella di modifica di un pacchetto
	//tutti i compatibili con questo pacchetto.
	//Setta in pratica i datamodel che utilizza la tabella per riempirsi
	private void filtraHotel()
	{
		ArrayList <HotelDTO> hdtolist = new ArrayList <HotelDTO>();
		for(HotelDTO hdto: CMB.getAllHotel()  )
		   {
			if(hdto.getLuogo().equals(packet.getDestinazione()))
				hdtolist.add(hdto);
		   }
		 setHotelModels(new HotelDataModel(hdtolist));	
	}
	
	private void filtraVoli()
	{
		ArrayList <VoloDTO> vdtolist = new ArrayList <VoloDTO>();
		for(VoloDTO vdto: CMB.getAllVolo()  )
		   {
			if(
			   ( vdto.getLuogo_partenza().equals(packet.getDestinazione()) || 
			     vdto.getLuogo_arrivo().equals(packet.getDestinazione()) )   
			   &&
			    ( ( vdto.getData().after(packet.getData_inizio()) ) || ( vdto.getData().equals(packet.getData_inizio())) && ( vdto.getData().before(packet.getData_fine()) || (vdto.getData().equals(packet.getData_fine()) )))
			   
			  )
				vdtolist.add(vdto);
		   }
		 setVoloModels(new VoloDataModel(vdtolist));	
		
	}
	
	private void filtraEscursioni()
	{
		ArrayList <EscursioneDTO> edtolist = new ArrayList <EscursioneDTO>();
		for(EscursioneDTO edto: CMB.getAllEscursione()  )
		   {
			if(edto.getLuogo().equals(packet.getDestinazione()) && 
			   edto.getData().after(packet.getData_inizio()) && edto.getData().before(packet.getData_fine())
			  )
				edtolist.add(edto);
		   }
		 setEscModels(new EscDataModel(edtolist));
		
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
	//END FILTRI----------------------------------------------------------
	
	//BEAN CORE
	public String  modificaPacchetto(int id) {
		
		if(selectedHotels.isEmpty() || selectedVolo.isEmpty())
		  {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Non puoi togliere tutti gli hotel/voli dal pack"));  	
			 return "modifypacket.xhtml";
		  }
		
		//Ora controllo che sia possibile creare almeno un viaggio con questo pack
	    //Cioè che ci sia un volo di andata ed uno di ritorno per quella destinazione
	    //Gli hotel inseriti sono per semplicità dati 
				
		int andata=0, ritorno=0;
		for(VoloDTO vdto : selectedVolo)
				   {
					
					
					if(vdto.getLuogo_arrivo().equals(packet.getDestinazione()))
						andata++;
					if(vdto.getLuogo_partenza().equals(packet.getDestinazione()))
						ritorno++;			
				   }
		if(andata == 0 || ritorno == 0)
		   {
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Non esiste più un volo di andata/ritorno per questo pack"));  	
			return "modifypacket.xhtml";
		   }
		
		int temporal=0;
		for(VoloDTO vdto: selectedVolo) 
		   {
			if(vdto.getLuogo_arrivo().equals(packet.getDestinazione()))
			  {
				Date date_ref = vdto.getData(); 
			    for(VoloDTO vdto2: selectedVolo)
			      {
				    if(vdto2.getLuogo_partenza().equals(packet.getDestinazione()))
				      {
				    	if(vdto2.getData().after(date_ref))
				    	  {
				    		temporal++;
				    		break;
				    	  }
				      }
			      }
			
		      }
		   }
		if(temporal<1)
		   {
			//MESSAGGIO DI ERRORE!!
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Deve esserci almeno un volo di andata precedente ad uno di ritorno"));  	
	            return "modifiypacket.xhtml";
		   }
		
		packet.setLista_escursioni(selectedEsc);
		packet.setLista_hotel(selectedHotels);
		packet.setLista_voli(selectedVolo);
		
		 if(!imgPack.getFileName().equals("")){
		InputStream inputStr = null;
	    try {
	        inputStr = imgPack.getInputstream();
	    } catch (IOException e) {
	        //log error
	    }

	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    String directory = externalContext.getInitParameter("uploadDirectory");
	    String filename = FilenameUtils.getName(imgPack.getFileName());
	    File destFile = new File(directory, filename);

	    //use org.apache.commons.io.FileUtils to copy the File
	    try {
	        FileUtils.copyInputStreamToFile(inputStr, destFile);
	    } catch (IOException e) {
	        //log error
	    }
	    packet.setPathtoImage(imgPack.getFileName());
	    }
		PMB.modifyPacchetto(packet);
		return "impack.xhtml?faces-redirect=true";
	}
	

	//--- Funzioni per passare id del pack da modificare in POST------

	public String outcome(int id)
	{
		this.setTarget_id(id);
        return "modifypacket.xhtml";
	}

	public int getTarget_id() {
		return target_id;
	}

	public void setTarget_id(int target_id) {
		this.target_id = target_id;
	}
	//----------------------------------------
}
