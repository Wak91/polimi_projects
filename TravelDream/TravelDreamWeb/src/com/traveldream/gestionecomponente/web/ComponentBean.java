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
import com.traveldream.gestionepack.web.EscDataModel;
import com.traveldream.gestionepack.web.HotelDataModel;
import com.traveldream.gestionepack.web.VoloDataModel;

@ManagedBean(name="ComponentBean") 
@ViewScoped
public class ComponentBean {

	@EJB
	private ComponentManagerBeanLocal CMB;
	
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
		return "impadd.xhtml?faces-redirect=true";
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
		return "impadd.xhtml?faces-redirect=true";
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
		return "impadd.xhtml?faces-redirect=true";
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
		  InputStream inputStr = null;
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
    CMB.modificaHotel(hotel);
    
    if (hotel.getPacchettos().isEmpty()){
    	System.out.println("hotel non inserito in nessun pacchetto");
    }
    for (PacchettoDTO pacchettoDTO : hotel.getPacchettos()) {
		System.out.println("hotel "+hotel.getNome()+" e' nel pacchetto "+pacchettoDTO.getNome());
	}

	return "toHotel.xhtml?faces-redirect=true";
	}
	
	public String deleteHotel(int id)
	{ CMB.eliminaHotel(id);
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
	{ CMB.modificaVolo(volo);

    if (volo.getPacchettos().isEmpty()){
    	System.out.println("volo non inserito in nessun pacchetto");
    }
    for (PacchettoDTO pacchettoDTO : volo.getPacchettos()) {
		System.out.println("volo "+volo.getCompagnia()+" e' nel pacchetto "+pacchettoDTO.getNome());
	}
	return "toVolo.xhtml?faces-redirect=true";

	
	}
	
	public String eliminaVolo(int id)
	{ CMB.eliminaVolo(id);
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
	{ CMB.modificaEscursione(escursione);

    if (escursione.getPacchettos().isEmpty()){
    	System.out.println("escursione non inserito in nessun pacchetto");
    }
    for (PacchettoDTO pacchettoDTO : escursione.getPacchettos()) {
		System.out.println("escursione "+escursione.getNome()+" e' nel pacchetto "+pacchettoDTO.getNome());
	}
	return "toEscursione.xhtml?faces-redirect=true";

	}
	
	
	public String eliminaEscursione(int id)
	{ 
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
