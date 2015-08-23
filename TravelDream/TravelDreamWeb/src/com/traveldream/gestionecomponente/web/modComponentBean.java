package com.traveldream.gestionecomponente.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.traveldream.gestionecomponente.ejb.ComponentManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;
import com.traveldream.gestionepack.web.EscDataModel;
import com.traveldream.gestionepack.web.HotelDataModel;
import com.traveldream.gestionepack.web.VoloDataModel;

@ManagedBean(name="modComponentBean") 
@SessionScoped
public class modComponentBean {

	@EJB
	private ComponentManagerBeanLocal CMB;
	
	@EJB
	private PackManagerBeanLocal PMB;
	
    private HotelDTO hotel; 
    private VoloDTO  volo;
    private EscursioneDTO escursione;
    
    private ArrayList<HotelDTO> filteredHotels;
    private ArrayList<VoloDTO> filteredVoli;
    private ArrayList<EscursioneDTO> filteredEscursioni;
    

	private HotelDataModel hotelModels;
    private EscDataModel escModels;
    private VoloDataModel voloModels;

    private UploadedFile imgHotel;
    private UploadedFile imgVolo;
    private UploadedFile imgEscursione;

	private int hid_target;
	private int vid_target;
	private int eid_target;
    
    
    
	public modComponentBean() {
		hotel = new HotelDTO();
		volo  = new VoloDTO();
		escursione = new EscursioneDTO();
	}
	
	public void validate_Date(FacesContext context,UIComponent component,Object value) throws ValidatorException{
		UIInput datainizio = (UIInput)component.getAttributes().get("dates");
		Date dataInizio = (Date)datainizio.getValue();
		Date dataFine = (Date)value;
		if (dataFine.before(dataInizio)){
                throw new ValidatorException(new FacesMessage("La data di fine validita' deve essere successiva a quella di inizio"));
        }
	}
	
//--------------------------GETTER_SETTER_HOTELS--------------------------------------
	

	public UploadedFile getImgVolo() {
		return imgVolo;
	}

	public void setImgVolo(UploadedFile imgVolo) {
		this.imgVolo = imgVolo;
	}

	public void setHotel(HotelDTO hoteldto) {
		this.hotel = hoteldto;
	}

	public HotelDTO getHotel() {
		return hotel;
	}

	public HotelDataModel getHotelModels() {
		return hotelModels;
	}

	public void setHotelModels(HotelDataModel hotelModels) {
		this.hotelModels = hotelModels;
	}
	
    public ArrayList<HotelDTO> getFilteredHotels() {
		return filteredHotels;
	}

	public void setFilteredHotels(ArrayList<HotelDTO> filteredHotels) {
		this.filteredHotels = filteredHotels;
	}
	
	public void getHotelById()
	{   
		this.hotel = CMB.getHotelById(this.hid_target);
	}
	
	public String modifyHotel()
	{ 
		  if(hotel.getCosto_giornaliero() <= 0 )
		    {
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Prezzo giornaliero deve essere > 0" ));
		        return "";
			  
		    }
		
		  if(!imgHotel.getFileName().equals(""))
		  { InputStream inputStr = null;
		    try {
		        inputStr = imgHotel.getInputstream();
		    } catch (IOException e) {
		        //log error
		    }

		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    String directory = externalContext.getInitParameter("uploadDirectory");
		    String filename = FilenameUtils.getName(imgHotel.getFileName());
		    File destFile = new File(directory, filename);

		    //use org.apache.commons.io.FileUtils to copy the File
		    try {
		        FileUtils.copyInputStreamToFile(inputStr, destFile);
		    } catch (IOException e) {
		        //log error
		    }
    hotel.setHotelImg(imgHotel.getFileName());
		  }
  //Prima di modificare con le nuove date controllo se ci sono pacchetti che hanno questo hotel dentro che potrebbero diventare incoerenti
  		ArrayList <PacchettoDTO> hotel_pack_list = CMB.getHotelById(hotel.getId()).getPacchettos();
		int pack_removed=0;
  		if(!hotel_pack_list.isEmpty()){  
  		for(PacchettoDTO p : hotel_pack_list)
  		   {
  			//se c'è una situazione di incoerenza nelle date o nel luogo elimino l'hotel dal pacchetto, 
  			//controllo se nel pacchetto ci sono altre componenti e nel caso update o elimino pack

  			//System.out.println("" + hotel.getData_inizio());
  			//System.out.println(""+hotel.getData_fine());
  			//System.out.println(""+p.getData_inizio());
  			//System.out.println(""+p.getData_fine());
  			if( ( ( hotel.getData_inizio().after(p.getData_fine()) ) ||
  				  (hotel.getData_fine().before(p.getData_inizio()))	
  					
  			     )    || 
  					
  					(hotel.getLuogo().equals(p.getDestinazione())==false))
  			{ 
  				if(p.getLista_hotel().size()== 1)// se nel pacchetto c'è solo un hotel, in questo caso è proprio quello da eliminare, butto quindi il pacchetto
  				 {
  					PMB.deletePacchetto(p.getId());
  					pack_removed++;
  				 }
  				else
  				   { // se non era l'unico hotel, rimuovo dalla lista del pacchetto e update pacchetto
  					ArrayList <HotelDTO> phdto = (ArrayList<HotelDTO>) p.getLista_hotel();
					ArrayList <HotelDTO> new_phdto = new ArrayList <HotelDTO> ();
					
  					 for(HotelDTO hdto: phdto)
  					    {
  						 if(hdto.getId() != hotel.getId())
  						  new_phdto.add(hdto);
  					    }

  					 p.setLista_hotel(new_phdto); // modifico la lista degli hotel al pacchetto corrente
  					 PMB.modifyPacchetto(p);
  				   }
  			 }
  		   }
		}
    
	if(pack_removed>=1)
	   {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Con questa modifica " +pack_removed+" pacchetto/i sono stati eliminati per incompatibilità" ));  	
	   }
    
    CMB.modificaHotel(hotel);

	return "toHotel.xhtml";
	}
	
//------------------------GETTER_SETTER_VOLO------------------------------------
	
	public VoloDTO getVolo() {
		return volo;
	}

	public void setVolo(VoloDTO volo) {
		this.volo = volo;
	}
	
	public VoloDataModel getVoloModels() {
		return voloModels;
	}

	public void setVoloModels(VoloDataModel voloModels) {
		this.voloModels = voloModels;
	}
	
	public ArrayList<VoloDTO> getFilteredVoli() {
		return filteredVoli;
	}

	public void setFilteredVoli(ArrayList<VoloDTO> filteredVoli) {
		this.filteredVoli = filteredVoli;
	}
	
	public void getVoloById()
	{   
		this.volo = CMB.getVoloById(this.vid_target);
	
	}
	
	public String modificaVolo()
	{    
	

		  if(volo.getCosto() <= 0 )
		    {
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Prezzo deve essere > 0" ));
		        return "";
			  
		    }	
		
    if(!imgVolo.getFileName().equals(""))
	{InputStream inputStr = null;
    try {
        inputStr = imgVolo.getInputstream();
    } catch (IOException e) {
        //log error
    }

    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
    String directory = externalContext.getInitParameter("uploadDirectory");
    String filename = FilenameUtils.getName(imgVolo.getFileName());
    File destFile = new File(directory, filename);

    //use org.apache.commons.io.FileUtils to copy the File
    try {
        FileUtils.copyInputStreamToFile(inputStr, destFile);
    } catch (IOException e) {
        //log error
    }
    volo.setImmagine(imgVolo.getFileName());
	}
    ArrayList <PacchettoDTO> voli_packet_list = CMB.getVoloById(volo.getId()).getPacchettos(); //safety
    int pr=0;
    if(!voli_packet_list.isEmpty()){
		for(PacchettoDTO p : voli_packet_list)
		   {
			 ArrayList <VoloDTO> pvdto = (ArrayList<VoloDTO>) p.getLista_voli();
			 ArrayList <VoloDTO> new_pvdto = new ArrayList <VoloDTO>();
			if(volo.getData().before(p.getData_inizio()) || (volo.getData().after(p.getData_fine()) || 
			 (  (volo.getLuogo_arrivo().equals(p.getDestinazione())==false) && (volo.getLuogo_partenza().equals(p.getDestinazione())==false))))
			{   
				       // se i voli erano di più, rimuovo dalla lista del pacchetto e update pacchetto
					   
					    for(VoloDTO vdto: pvdto)
					    {
						 if(vdto.getId() != volo.getId())
						 new_pvdto.add(vdto); //new_pvdto è la nuova list voli del pack
					    }	
					  
			 }
			
			  int temporal=0; //Controllo che ci sia ancora un volo di andata prima di uno di ritorno nel pacchetto
			    for(VoloDTO vdto: new_pvdto) 
				   {
					if(vdto.getLuogo_arrivo().equals(p.getDestinazione()))
					  {
						Date date_ref = vdto.getData(); 
					    for(VoloDTO vdto2: new_pvdto)
					      {
						    if(vdto2.getLuogo_partenza().equals(p.getDestinazione()))
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
			    
			    int andata=0,ritorno=0; //controllo nella lista nuova se esiste almeno un volo di andata e uno di ritorno
			    for(VoloDTO vdto2 : new_pvdto)
			       {
			    	if(vdto2.getLuogo_partenza().equals(p.getDestinazione()))
			    		andata++;
			    	else
			    		if(vdto2.getLuogo_arrivo().equals(p.getDestinazione()))
			    			ritorno++;
			       }
			  
			    if(andata>=1 && ritorno>=1 &&temporal>=1) // se ho ancora abbastanza voli salvo la nuova lista e aggiorno il pack
			    {
			      p.setLista_voli(new_pvdto); 
				  PMB.modifyPacchetto(p);
			    }
			    else
			    	{PMB.deletePacchetto(p.getId()); pr++;}
		    } // fine controllo per tutti i pacchetti
			
		   } //fine if di volo contenuto in nessun pacchetto
    
    if(pr>=1)
    {        
    	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Con questa modifica " +pr+" pacchetto/i sono stati eliminati per incompatibilità" ));  	
    }
    
	CMB.modificaVolo(volo);

	
	return "toVolo.xhtml";
    }
    


	
	
	
//-------------------------GETTER_SETTER_ESCURSIONE--------------------------------


	public EscDataModel getEscModels() {
		return escModels;
	}

	public void setEscModels(EscDataModel escModels) {
		this.escModels = escModels;
	}

	public EscursioneDTO getEscursione() {
		return escursione;
	}

	public void setEscursione(EscursioneDTO escursione) {
		this.escursione = escursione;
	}
	
	public ArrayList<EscursioneDTO> getFilteredEscursioni() {
		return filteredEscursioni;
	}

	public void setFilteredEscursioni(ArrayList<EscursioneDTO> filteredEscursioni) {
		this.filteredEscursioni = filteredEscursioni;
	}
	
	public void getEscursioneById()
	{   
		this.escursione = CMB.getEscursioneById(this.eid_target);
	
	}
	
	public String modificaEscursione()
	{ 
		 if(escursione.getCosto() <= 0 )
		    {
		        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "Prezzo deve essere > 0" ));
		        return "";
			  
		    }	
		
		 if(!imgEscursione.getFileName().equals("")){
		 InputStream inputStr = null;
		    try {
		        inputStr = imgEscursione.getInputstream();
		    } catch (IOException e) {
		        //log error
		    }

		    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		    String directory = externalContext.getInitParameter("uploadDirectory");
		    String filename = FilenameUtils.getName(imgEscursione.getFileName());
		    File destFile = new File(directory, filename);

		    //use org.apache.commons.io.FileUtils to copy the File
		    try {
		        FileUtils.copyInputStreamToFile(inputStr, destFile);
		    } catch (IOException e) {
		        //log error
		    }
 escursione.setImmagine(imgEscursione.getFileName());
 }
 
		 ArrayList <PacchettoDTO> esc_packet_list = CMB.getEscursioneById(escursione.getId()).getPacchettos();
//Prima di modificare con le nuove date controllo se ci sono pacchetti che hanno questa esc dentro che potrebbero diventare incoerenti
		 if(!esc_packet_list.isEmpty()){
		 for(PacchettoDTO p : esc_packet_list)
		   {
			//se c'è una situazione di incoerenza nelle date o nel luogo elimino l'escursione dal pacchetto, 
			//controllo se nel pacchetto ci sono altre componenti e nel caso update pack ( posso avere zero esc )

			if(escursione.getData().before(p.getData_inizio()) || (escursione.getData().after(p.getData_fine()) || (escursione.getLuogo().equals(p.getDestinazione())==false)))
			{
					ArrayList <EscursioneDTO> pedto = (ArrayList<EscursioneDTO>) p.getLista_escursioni();
					ArrayList <EscursioneDTO> new_pedto = new ArrayList <EscursioneDTO>();
					 for(EscursioneDTO edto: pedto)
					    {
						 if(edto.getId() != escursione.getId())
						  new_pedto.add(edto);
					    }

					 p.setLista_escursioni(new_pedto); // modifico la lista delle esc al pacchetto corrente
					 PMB.modifyPacchetto(p);	   
			 }
		   }
		 }
		 
		 
		CMB.modificaEscursione(escursione);

	return "toEscursione.xhtml?faces-redirect=true";

	}

	public UploadedFile getImgHotel() {
		return imgHotel;
	}

	public void setImgHotel(UploadedFile imgHotel) {
		this.imgHotel = imgHotel;
	}

	public UploadedFile getImgEscursione() {
		return imgEscursione;
	}

	public void setImgEscursione(UploadedFile imgEscursione) {
		this.imgEscursione = imgEscursione;
	}
	
	
	//----Passing POST
	public String houtcome(int id)
	{
		this.setHid_target(id);
		return "modifyHotel.xhtml";
		
	}
	
	public String voutcome(int id)
	{
		this.setVid_target(id);
		return "modifyVolo.xhtml";
		
	}
	
	public String eoutcome(int id)
	{
		this.setEid_target(id);
		return "modifyEscursione.xhtml";
		
	}

	public int getHid_target() {
		return hid_target;
	}

	public void setHid_target(int hid_target) {
		this.hid_target = hid_target;
	}

	public int getVid_target() {
		return vid_target;
	}

	public void setVid_target(int vid_target) {
		this.vid_target = vid_target;
	}

	public int getEid_target() {
		return eid_target;
	}

	public void setEid_target(int eid_target) {
		this.eid_target = eid_target;
	}

}
