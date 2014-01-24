package com.traveldream.gestionecomponente.web;




import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;


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

@ManagedBean(name="ComponentBean") 
@ViewScoped
public class ComponentBean {

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
    
    
    
	public ComponentBean() {
		hotel = new HotelDTO();
		volo  = new VoloDTO();
		escursione = new EscursioneDTO();
	}

	public void initBean()
	{
		setHotelModels(new HotelDataModel(CMB.getAllHotel()));	
		setVoloModels(new VoloDataModel(CMB.getAllVolo()));
		setEscModels(new EscDataModel(CMB.getAllEscursione()));
	}


	
	public void validate_Date(FacesContext context,UIComponent component,Object value) throws ValidatorException{
		UIInput datainizio = (UIInput)component.getAttributes().get("dates");
		Date dataInizio = (Date)datainizio.getValue();
		Date dataFine = (Date)value;
		if (dataFine.before(dataInizio)){
                throw new ValidatorException(new FacesMessage("La data di fine validita' deve essere successiva a quella di inizio"));
        }
	}
	
//-------------------------CREAZIONE COMPONENTI------------------------------
	public String createHotel() throws IOException{
		
		  String filename = FilenameUtils.getName(imgHotel.getFileName());
		  if(filename.equals(""))
		    {
			 hotel.setHotelImg("Hdefault.jpeg");}
		  else
		    {
		  InputStream inputStr = null;
		    try {
		        inputStr = imgHotel.getInputstream();
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
		hotel.setHotelImg(imgHotel.getFileName());
		    }
		CMB.saveHotel(hotel);
		FacesMessage msg = new FacesMessage("Hotel is added");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		return "addHotel.xhtml?faces-redirect=true";
	}
		
	public String createVolo(){
		
		String filename = FilenameUtils.getName(imgVolo.getFileName());
		  
		 if(filename.equals(""))
		    {
		  volo.setImmagine("Vdefault.jpg");
		    }
		 else
		 {
		  InputStream inputStr = null;
		    try {
		        inputStr = imgVolo.getInputstream();
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
			volo.setImmagine(imgVolo.getFileName());
		    }
		CMB.saveVolo(volo);
		return "addVolo.xhtml?faces-redirect=true";
	}
	
	public String createEscursione(){
		 
		String filename = FilenameUtils.getName(imgEscursione.getFileName());
		  
		 if(filename.equals(""))
		    {
		 escursione.setImmagine("Edefault.jpg");
		    }
		 else
		 {
		  InputStream inputStr = null;
		    try {
		        inputStr = imgEscursione.getInputstream();
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
			escursione.setImmagine(imgEscursione.getFileName());

		    }
		CMB.saveEscursione(escursione);
		return "addEscursione.xhtml?faces-redirect=true";
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
	
	public void getHotelById(int id)
	{   
		System.out.println("refreshing hotel");
		this.hotel = CMB.getHotelById(id);
	
	}
	
	public String modifyHotel()
	{ 
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
  		for(PacchettoDTO p : CMB.getHotelById(hotel.getId()).getPacchettos())
  		   {
  			//se c'è una situazione di incoerenza nelle date o nel luogo elimino l'hotel dal pacchetto, 
  			//controllo se nel pacchetto ci sono altre componenti e nel caso update o elimino pack

  			if(hotel.getData_inizio().after(p.getData_fine()) || (hotel.getLuogo().equals(p.getDestinazione())==false))
  			{
  				if(p.getLista_hotel().size()== 1)// se nel pacchetto c'è solo un hotel, in questo caso è proprio quello da eliminare, butto quindi il pacchetto
  				 {
  					PMB.deletePacchetto(p.getId());
  				 }
  				else
  				   { // se non era l'unico hotel, rimuovo dalla lista del pacchetto e update pacchetto
  					 ArrayList <HotelDTO> phdto = (ArrayList<HotelDTO>) p.getLista_hotel();
  					 
  					 for(HotelDTO hdto: phdto)
  					    {
  						 if(hdto.getId() == hotel.getId())
  						  phdto.remove(hdto);
  					    }

  					 p.setLista_hotel(phdto); // modifico la lista degli hotel al pacchetto corrente
  					 PMB.modifyPacchetto(p);
  				   }
  			 }
  		   }
    
    CMB.modificaHotel(hotel);

	return "toHotel.xhtml?faces-redirect=true";
	}
	
	public String deleteHotel(int id)
	{ 
		for(PacchettoDTO p : CMB.getHotelById(id).getPacchettos())
		   {
				if(p.getLista_hotel().size()== 1)// se nel pacchetto c'è solo un hotel, in questo caso è proprio quello da eliminare
				 {
					PMB.deletePacchetto(p.getId());
				 }
				else
				   { // se non era l'unico hotel, rimuovo dalla lista del pacchetto e elimina pacchetto
					 ArrayList <HotelDTO> phdto = (ArrayList<HotelDTO>) p.getLista_hotel();
					 ArrayList <HotelDTO> new_phdto = new ArrayList <HotelDTO>();
					 for(HotelDTO hdto: phdto)
					    {
						 if(hdto.getId() != id)
						  new_phdto.add(hdto);
					    }
					 p.setLista_hotel(new_phdto); // modifico la lista degli hotel al pacchetto corrente
					 PMB.modifyPacchetto(p);
				   }
			 }
		CMB.eliminaHotel(id);
	return "toHotel.xhtml?faces-redirect=true";
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
	
	public void getVoloById(int id)
	{   
		this.volo = CMB.getVoloById(id);
	
	}
	
	public String modificaVolo()
	{     
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
		for(PacchettoDTO p : CMB.getVoloById(volo.getId()).getPacchettos())
		   {
			if(volo.getData().before(p.getData_inizio()) || (volo.getData().after(p.getData_fine()) || 
			 (  (volo.getLuogo_arrivo().equals(p.getDestinazione())==false) && (volo.getLuogo_partenza().equals(p.getDestinazione())==false))))
			{   
				       { // se i voli erano di più, rimuovo dalla lista del pacchetto e update pacchetto
					    ArrayList <VoloDTO> pvdto = (ArrayList<VoloDTO>) p.getLista_voli();
					    ArrayList <VoloDTO> new_pvdto = new ArrayList <VoloDTO>();
					    for(VoloDTO vdto: pvdto)
					    {
						 if(vdto.getId() != volo.getId())
							 new_pvdto.add(vdto); //new_pvdto è la nuova list voli del pack
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
					    if(andata>=1 && ritorno>=1) // se ho ancora abbastanza voli salvo la nuova lista e aggiorno il pack
					    {
					      p.setLista_voli(new_pvdto); 
						  PMB.modifyPacchetto(p);
					    }
					    else
					    	PMB.deletePacchetto(p.getId());
				       }
			 }
		   }
    
	CMB.modificaVolo(volo);

	
	return "toVolo.xhtml?faces-redirect=true";

	
	}
	
	public String eliminaVolo(int id)
	{ 
		for(PacchettoDTO p : CMB.getVoloById(id).getPacchettos())
		   {
			  ArrayList <VoloDTO> pvdto = (ArrayList<VoloDTO>) p.getLista_voli();
			    ArrayList <VoloDTO> new_pvdto = new ArrayList <VoloDTO>();
			    for(VoloDTO vdto: pvdto)
			    {
				 if(vdto.getId() != id)
					 new_pvdto.add(vdto); //new_pvdto è la nuova list voli del pack
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
			    if(andata>=1 && ritorno>=1) // se ho ancora abbastanza voli salvo la nuova lista e aggiorno il pack
			    {
			      p.setLista_voli(new_pvdto); 
				  PMB.modifyPacchetto(p);
			    }
			    else
			    	PMB.deletePacchetto(p.getId());
		       }
		CMB.eliminaVolo(id);
	  return "toVolo.xhtml?faces-redirect=true";
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
	
	public void getEscursioneById(int id)
	{   
		this.escursione = CMB.getEscursioneById(id);
	
	}
	
	public String modificaEscursione()
	{ 
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
 
//Prima di modificare con le nuove date controllo se ci sono pacchetti che hanno questa esc dentro che potrebbero diventare incoerenti
		for(PacchettoDTO p : CMB.getEscursioneById(escursione.getId()).getPacchettos())
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
		CMB.modificaEscursione(escursione);

	return "toEscursione.xhtml?faces-redirect=true";

	}
	
	
	public String eliminaEscursione(int id)
	{ 
		for(PacchettoDTO p : CMB.getEscursioneById(id).getPacchettos())
		   {
					ArrayList <EscursioneDTO> pedto = (ArrayList<EscursioneDTO>) p.getLista_escursioni();
					if(!pedto.isEmpty()) 
					  {
					   for(EscursioneDTO edto: pedto)
					    {
						 if(edto.getId() == id)
						  pedto.remove(edto);
					    }

					    p.setLista_escursioni(pedto); // modifico la lista degli hotel al pacchetto corrente
					    PMB.modifyPacchetto(p);
					  }
		   }
			 	   
      CMB.eliminaEscursione(id);
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

}
