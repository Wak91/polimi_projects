package com.traveldream.condivisione.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.EscursionePagataDTO;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.GiftListManagerBeanLocal;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;
import com.traveldream.util.web.FacesUtil;
import com.traveldream.viaggio.web.PreDataModel;



@ManagedBean(name="GiftListBean") 
@SessionScoped
public class GiftListBean {

	private String amico;
	private amiciDatamodel amiciDatamodel;

	
	private GiftDataModel giftDataModel;
	private GiftListDTO selectedGiftListDTO;	//gift list per la visualizzazione
	
	
	private EscursionePagataDatamodel escursionePagataDatamodel;
	private ArrayList<GiftListDTO> filteredGift;
	
	private String codiceGift;
	
	@EJB
	private UserMgr userMgr;
	
	@EJB
	private BookManagerBeanLocal BMB; 
	
	@EJB
	private GiftListManagerBeanLocal GLM;

	private GiftListDTO giftListDTO; //gift list per la creazione
	
	private GiftListDTO giftListDTOAmicoDto; //gift list vista dall'amico
	
	boolean currentHotelPag;
	boolean currentVoloaPag;
	boolean currentVolorPag;
	ArrayList<EscursionePagataDTO> currentEscPag;

	int costocomplessivo;
	String password;

	@PostConstruct
    public void init() {
		giftListDTO = (GiftListDTO)FacesUtil.getSessionMapValue("GiftDTO");
    }
	
	public String reinit() {  
        amico = new String();           
        return null;  
    }  
	
	public String getAmico() {
		return amico;
	}
	public void setAmico(String amico) {
		this.amico = amico;
	}

	public GiftListDTO getGiftListDTO() {
		return giftListDTO;
	}

	public void setGiftListDTO(GiftListDTO giftListDTO) {
		this.giftListDTO = giftListDTO;
	}
	
	public String creaGift(){
		giftListDTO.setVoloAPag(false);
		giftListDTO.setVoloRPag(false);
		giftListDTO.setHotelPag(false);
		for (EscursioneDTO escursioneDTO : giftListDTO.getViaggio().getLista_escursioni()) {
			System.out.println("Aggiungo escursioni a pagata"+escursioneDTO.getId()+escursioneDTO.getNome());
			EscursionePagataDTO escursionePagata = new EscursionePagataDTO();
			escursionePagata.setEscPagata(false);
			escursionePagata.setEscursione(escursioneDTO);
			System.out.println("aggiunta "+escursionePagata.getEscursione().getNome());
			giftListDTO.getEscursionePagata().add(escursionePagata);
		}
		System.out.println("aggiunta "+giftListDTO.getViaggio().getData_fine());

		GLM.addToGiftList(giftListDTO);
		for (String string : getGiftListDTO().getAmico() ) {
			System.out.println("gift "+string);
		}	
		return "giftlist.xhtml?faces-redirect=true";
	}
	
	public void eliminaGift() {
		GLM.removeFromGift(selectedGiftListDTO);
		UserDTO current_user = userMgr.getUserDTO();

		setGiftDataModel(new GiftDataModel(GLM.getGiftListDTO(current_user)));


	}
	public void setupamicidialog() {
		amiciDatamodel = new amiciDatamodel(selectedGiftListDTO.getAmico());

	}
	
	public void setupEscursioniPagatedialog(){
		System.out.println("ID selected"+selectedGiftListDTO.getId());
		escursionePagataDatamodel= new EscursionePagataDatamodel(selectedGiftListDTO.getEscursionePagata());
	}
	
	public void getGiftList() {
		UserDTO current_user = userMgr.getUserDTO();
		setGiftDataModel(new GiftDataModel(GLM.getGiftListDTO(current_user)));
	}

	//parte answer gift list
	
	public String verifyCode(String codice) {
		System.out.println(codice);
		GiftListDTO gift=GLM.findGiftByHash(codice);
		if (gift==null){
			System.out.println("sono nel if");
			FacesContext.getCurrentInstance().addMessage("codice", new FacesMessage("Il codice inserito non e' corretto"));
			return null;
		}
		System.out.println("sono fuori nel if");

		giftListDTOAmicoDto=gift;
		return "answergift.xhtml?faces-redirect=true&id="+codice;

	}
	
	public void loadGiftFromHash(String codice){
		System.out.println(codice);
		GiftListDTO gift=GLM.findGiftByHash(codice);
		if (gift==null){
			System.out.println("sono nel if");
			FacesContext.getCurrentInstance().addMessage("null", new FacesMessage("Il codice inserito non e' corretto"));
		}
		giftListDTOAmicoDto=gift;
		currentEscPag=new ArrayList<EscursionePagataDTO>();
		//set up info per valutare queli checkbox l'itente seleziona
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			EscursionePagataDTO escPagata =new EscursionePagataDTO();
			escPagata.setId(escursionePagataDTO.getId());
			escPagata.setEscursione(escursionePagataDTO.getEscursione());
			escPagata.setEscPagata(false);
			currentEscPag.add(escPagata);
		}

		
	}
	
	public boolean renderEsc(int id){
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			System.out.println("escursioni in esc amico"+escursionePagataDTO.getId()+escursionePagataDTO.getEscursione().getNome());
			if (escursionePagataDTO.getId()==id & escursionePagataDTO.getEscPagata()) {
				System.out.println("id esc"+escursionePagataDTO.getId()+" pagata? "+escursionePagataDTO.getEscPagata());
				return false;
			}
		}
		return true;
	}
	
	//calcolo costi
	
	public int calcolaCostoHotel(GiftListDTO giftListDTO){
		int costoGiornaliero =giftListDTO.getViaggio().getHotel().getCosto_giornaliero();
		int duration = (int) (( giftListDTO.getViaggio().getHotel().getData_fine().getTime() - giftListDTO.getViaggio().getHotel().getData_inizio().getTime() ) / (1000 * 60 * 60 * 24));
		int numPers =giftListDTO.getNpersone();
		return numPers*duration*costoGiornaliero;
	}

	
		
	
	public String paga(){
		costocomplessivo=0;
		if (currentHotelPag){
			costocomplessivo+=calcolaCostoHotel(giftListDTOAmicoDto);
		}
		if (currentVoloaPag) {
			costocomplessivo+=giftListDTOAmicoDto.getViaggio().getVolo_andata().getCosto()*giftListDTOAmicoDto.getNpersone();
		}
		if (currentVolorPag) {
			costocomplessivo+=giftListDTOAmicoDto.getViaggio().getVolo_ritorno().getCosto()*giftListDTOAmicoDto.getNpersone();
		}
		for (EscursionePagataDTO escursionePagataDTO : currentEscPag) {
			if (escursionePagataDTO.getEscPagata()) {
				costocomplessivo+=escursionePagataDTO.getEscursione().getCosto();
			}
		}
		System.out.println("hotel "+currentHotelPag+"volo a"+currentVoloaPag+"volo r "+currentVolorPag);
		System.out.println("costo comp"+costocomplessivo);
		return "pagamentogift.xhtml?faces-redirect=true";
	}

	public void salvaPagamentoGift(){

		

		if(giftListDTOAmicoDto.isHotelPag()==false){	//se l'hotel non è stato ancora pagato lo aggiorno con il valore della scelta attuale se è false rimane uguale altrimenti significa che è stato pagato
		giftListDTOAmicoDto.setHotelPag(currentHotelPag);
		}
		if(giftListDTOAmicoDto.isVoloAPag()==false){	//come per hotel
			giftListDTOAmicoDto.setVoloAPag(currentVoloaPag);
			}
		if(giftListDTOAmicoDto.isVoloRPag()==false){	//come per hotel
			giftListDTOAmicoDto.setVoloRPag(currentVolorPag);
			}
		for (EscursionePagataDTO escursionePagataDTO : giftListDTOAmicoDto.getEscursionePagata()) {
			for (EscursionePagataDTO currentEsc : currentEscPag) {
				if (escursionePagataDTO.getId()==currentEsc.getId() & escursionePagataDTO.getEscPagata()==false){
					escursionePagataDTO.setEscPagata(currentEsc.getEscPagata());
				}
			}
		}
		GLM.aggiornaGift(giftListDTOAmicoDto);
		
	}
	
	
	public String confermaGift(){
		costocomplessivo=0;
		if (selectedGiftListDTO.isHotelPag()==false){
			costocomplessivo+=calcolaCostoHotel(selectedGiftListDTO);
		}
		if (selectedGiftListDTO.isVoloAPag()==false) {
			costocomplessivo+=selectedGiftListDTO.getViaggio().getVolo_andata().getCosto()*selectedGiftListDTO.getNpersone();
		}
		if (selectedGiftListDTO.isVoloRPag()==false) {
			costocomplessivo+=selectedGiftListDTO.getViaggio().getVolo_ritorno().getCosto()*selectedGiftListDTO.getNpersone();
		}
		for (EscursionePagataDTO escursionePagataDTO : selectedGiftListDTO.getEscursionePagata()) {
			if (escursionePagataDTO.getEscPagata()==false) {
				costocomplessivo+=escursionePagataDTO.getEscursione().getCosto();
			}
		}
		return "pagamentogift.xhtml?faces-redirect=true";
	}
	
	public String AcquistaGift(){
		PrenotazioneDTO prenotazione = new PrenotazioneDTO();
		prenotazione.setCosto(costocomplessivo);
		prenotazione.setNumero_persone(selectedGiftListDTO.getNpersone());
		prenotazione.setUtente(selectedGiftListDTO.getUtente());
		prenotazione.setViaggio(selectedGiftListDTO.getViaggio());
		BMB.savePrenotazione(prenotazione);
		
		GLM.removeFromGift(selectedGiftListDTO);
		return "imieiviaggi.xhtml?faces-redirect=true";
	}
	
	
	public GiftDataModel getGiftDataModel() {
		return giftDataModel;
	}

	public void setGiftDataModel(GiftDataModel giftDataModel) {
		this.giftDataModel = giftDataModel;
	}

	public GiftListDTO getSelectedGiftListDTO() {
		return selectedGiftListDTO;
	}

	public void setSelectedGiftListDTO(GiftListDTO selectedGiftListDTO) {
		this.selectedGiftListDTO = selectedGiftListDTO;
	}

	
	public ArrayList<GiftListDTO> getFilteredGift() {
		return filteredGift;
	}

	public void setFilteredGift(ArrayList<GiftListDTO> filteredGift) {
		this.filteredGift = filteredGift;
	}

	public EscursionePagataDatamodel getEscursionePagataDatamodel() {
		return escursionePagataDatamodel;
	}

	public void setEscursionePagataDatamodel(
			EscursionePagataDatamodel escursionePagataDatamodel) {
		this.escursionePagataDatamodel = escursionePagataDatamodel;
	}

	public amiciDatamodel getAmiciDatamodel() {
		return amiciDatamodel;
	}

	public void setAmiciDatamodel(amiciDatamodel amiciDatamodel) {
		this.amiciDatamodel = amiciDatamodel;
	}

	public String getCodiceGift() {
		return codiceGift;
	}

	public void setCodiceGift(String codiceGift) {
		this.codiceGift = codiceGift;
	}

	public GiftListDTO getGiftListDTOAmicoDto() {
		return giftListDTOAmicoDto;
	}

	public void setGiftListDTOAmicoDto(GiftListDTO giftListDTOAmicoDto) {
		this.giftListDTOAmicoDto = giftListDTOAmicoDto;
	}

	public boolean isCurrentHotelPag() {
		return currentHotelPag;
	}

	public void setCurrentHotelPag(boolean currentHotelPag) {
		this.currentHotelPag = currentHotelPag;
	}

	public boolean isCurrentVoloaPag() {
		return currentVoloaPag;
	}

	public void setCurrentVoloaPag(boolean currentVoloaPag) {
		this.currentVoloaPag = currentVoloaPag;
	}

	public boolean isCurrentVolorPag() {
		return currentVolorPag;
	}

	public void setCurrentVolorPag(boolean currentVolorPag) {
		this.currentVolorPag = currentVolorPag;
	}

	public ArrayList<EscursionePagataDTO> getCurrentEscPag() {
		return currentEscPag;
	}

	public void setCurrentEscPag(ArrayList<EscursionePagataDTO> currentEscPag) {
		this.currentEscPag = currentEscPag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getCostocomplessivo() {
		return costocomplessivo;
	}

	public void setCostocomplessivo(int costocomplessivo) {
		this.costocomplessivo = costocomplessivo;
	}

	
	
	
	
}
