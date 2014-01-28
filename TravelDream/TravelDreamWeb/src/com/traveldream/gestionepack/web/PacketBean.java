package com.traveldream.gestionepack.web;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.StreamedContent;
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
    
    private ArrayList <VoloDTO> lista_voli_dialog;
    
    private PacchettoDTO selectedPackDTO;
    
   
    	
    //---INIZIALIZZAZIONE BEAN---
    
   

	public PacketBean()
	{

	selectedHotels  = new ArrayList <HotelDTO>(); // questo per tenere traccia di quelli selezionati
	selectedVolo = new ArrayList <VoloDTO>();
	selectedEsc = new ArrayList <EscursioneDTO>();
	}
	
	public void initBean()
	{
		 packet = new PacchettoDTO();
		 setHotelModels(new HotelDataModel(CMB.getAllHotel()));	
		 setVoloModels(new VoloDataModel(CMB.getAllVolo()));
		 setEscModels(new EscDataModel(CMB.getAllEscursione()));
		 setPackModels(new PacchettoDataModel(PMB.getAllPack()));
		 selectedHotels.clear();
		 selectedVolo.clear();
		 selectedEsc.clear();
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
	  
	  public UploadedFile getImgPack() {
			return imgPack;
		}

		public void setImgPack(UploadedFile imgPack) {
			this.imgPack = imgPack;
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
	//FILTRI
	//---------------------------------------------------------------

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
	//---------------------------------------------------------------
	//END FILTRI
	//---------------------------------------------------------------
	
	//BEAN CORE
	public String PrelevaSelezionatiECrea()
	{
		//check della destinazione perche ho dovuto togliere l'attributo not empty dal DTO e poi non c'e nessun controlo sugli hotel e vli
				if(packet.getDestinazione()==null || packet.getDestinazione().isEmpty() || selectedVolo.isEmpty() || selectedHotels.isEmpty()){
					System.out.println("stop packet");
			        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Qualche campo è rimasto vuoto..."));  	
			        return "addPacket.xhtml";

				}
				int andata=0, ritorno=0;

				//almeno un volo di andata e uno di ritorno 
				for(VoloDTO vdto : selectedVolo)
				   {
					
					if(vdto.getLuogo_arrivo().equals(packet.getDestinazione()))
						andata++;
					else
						if(vdto.getLuogo_partenza().equals(packet.getDestinazione()))
							ritorno++;				
				   }
				if(andata<1 || ritorno <1)
				   {
					//MESSAGGIO DI ERRORE!!
			        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Deve esserci almeno un volo di andata e uno di ritorno"));  	
			        return "addPacket.xhtml";
				   }
				//almeno un volo di andata prima di un volo di ritorno ( altrimenti è impossibile creare un viaggio )
				int temporal=0;
				for(VoloDTO vdto: selectedVolo) //gira come O(n^2)...
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
			            return "addPacket.xhtml";
				   }
				
				if(packet.getData_inizio().after(packet.getData_fine()) || packet.getData_inizio().equals(packet.getData_fine()) )
				{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "La data di inizio deve essere prima di quella di fine!"));  	
		            return "addPacket.xhtml";
				}
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
		
		//---Se si vuole usare la post questo bean deve essere session scoped e questi 
		//   valori vanno resettati quando si esce dalla creazione della pagina 
		// vanno quindi messi anche in caso di errore altrimenti rimangono le cose filtrate prima
		//----------------------------------------------------------------------
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Pacchetto aggiunto!" ));  	
		return "impack.xhtml";
		
	}
	
	public void deletePacchetto(int id)
	{ 	
		PMB.deletePacchetto(id);
	}	

	//-------------------------------------------------------
	//UTILITY PER DIALOG
	//-------------------------------------------------------

    public void caricaDialogVoli()
    {
    	setVoloModels(new VoloDataModel(selectedPackDTO.getLista_voli()));
      
    }
    
    public void caricaDialogHotel()
    {
    	setHotelModels(new HotelDataModel(selectedPackDTO.getLista_hotel()));
      
    }
    
    public void caricaDialogEsc()
    {
    	setEscModels(new EscDataModel(selectedPackDTO.getLista_escursioni()));
      
    }

	public ArrayList <VoloDTO> getLista_voli_dialog() {
		return lista_voli_dialog;
	}

	public void setLista_voli_dialog(ArrayList <VoloDTO>lista_voli_dialog) {
		this.lista_voli_dialog = lista_voli_dialog;
	}

	public PacchettoDTO getSelectedPackDTO() {
		return selectedPackDTO;
	}

	public void setSelectedPackDTO(PacchettoDTO selectedPackDTO) {
		this.selectedPackDTO = selectedPackDTO;
	}
	
	
	
	
}
