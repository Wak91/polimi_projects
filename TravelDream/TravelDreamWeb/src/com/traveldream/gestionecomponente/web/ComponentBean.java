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
		CMB.saveHotel(hotel);
		return "impadd.xhtml?faces-redirect=true";
	}
		
	public String createVolo(){
		CMB.saveVolo(volo);
		return "impadd.xhtml?faces-redirect=true";
	}
	
	public String createEscursione(){
		CMB.saveEscursione(escursione);
		return "impadd.xhtml?faces-redirect=true";
	}
	
//--------------------------GETTER_SETTER_HOTELS--------------------------------------
	

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
		this.hotel = CMB.getHotelById(id);
	
	}
	
	public void modifyHotel()
	{ CMB.modificaHotel(hotel);}
	
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
	
	public void modificaVolo()
	{ CMB.modificaVolo(volo);}
	
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
	
	public void modificaEscursione()
	{ CMB.modificaEscursione(escursione);}
	
	public String eliminaEscursione(int id)
	{ 
      CMB.eliminaEscursione(id);
	  return "toEscursione.xhtml?faces-redirect=true";
	}

	public void handleFileUpload(FileUploadEvent event) {
	    //get uploaded file from the event
	    UploadedFile uploadedFile = (UploadedFile) event.getFile();
	    //create an InputStream from the uploaded file
	    InputStream inputStr = null;
	    try {
	        inputStr = uploadedFile.getInputstream();
	    } catch (IOException e) {
	        //log error
	    }

	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    String directory = externalContext.getInitParameter("uploadDirectory");
	    String filename = FilenameUtils.getName(uploadedFile.getFileName());
	    File destFile = new File(directory, filename);

	    //use org.apache.commons.io.FileUtils to copy the File
	    try {
	        FileUtils.copyInputStreamToFile(inputStr, destFile);
	    } catch (IOException e) {
	        //log error
	    }
	    FacesMessage msg = new FacesMessage(event.getFile().getFileName() + " is uploaded.");
	    FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
