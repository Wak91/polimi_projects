package com.traveldream.viaggio.web;

import java.util.ArrayList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.condivisione.web.GiftListBean;
import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.HotelDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;
import com.traveldream.gestionepack.ejb.PacchettoDTO;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;
/**
 * 
 */
import com.traveldream.gestionepack.web.EscDataModel;
import com.traveldream.gestionepack.web.HotelDataModel;
import com.traveldream.gestionepack.web.VoloDataModel;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;
import com.traveldream.util.web.FacesUtil;


@ManagedBean(name="ViaggioBean") 
@ViewScoped
public class ViaggioBean {
	
	@EJB
	private PackManagerBeanLocal PMB;
	
	@EJB 
	private UserMgr UMG;
	
	
	public HotelDTO getSelectedHotels() {
		return selectedHotels;
	}

	public ArrayList<EscursioneDTO> getSelectedEsc() {
		return selectedEsc;
	}

	private HotelDTO selectedHotels;
	private VoloDTO selectedVolo_a;
	private VoloDTO selectedVolo_r;
	private ArrayList<EscursioneDTO> selectedEsc;
	
	
	private HotelDataModel hotelModels;
    private VoloDataModel  voloModels_a;
    private VoloDataModel  voloModels_r;
    private EscDataModel escModels;
    
    private ArrayList<HotelDTO> filteredHotels;
	private ArrayList<EscursioneDTO> filteredEscursiones;
	private ArrayList<VoloDTO> filteredVolos;
    
	private PacchettoDTO packet;

    private ViaggioDTO viaggio;
	
	public ViaggioBean() {
		
		 packet = new PacchettoDTO();
		 viaggio = new ViaggioDTO();
		
	}

	public HotelDataModel getHotelModels() {
		return hotelModels;
	}

	public void setHotelModels(HotelDataModel hotelModels) {
		this.hotelModels = hotelModels;
	}

	public EscDataModel getEscModels() {
		return escModels;
	}

	public void setEscModels(EscDataModel escModels) {
		this.escModels = escModels;
	}

	public PacchettoDTO getPacket() {
		return packet;
	}

	public void setPacket(PacchettoDTO packet) {
		this.packet = packet;
	}
	

	public void getPacchettoById(int id)
	{	 
		 this.packet = PMB.getPacchettoByID(id);
		 setHotelModels(new HotelDataModel(packet.getLista_hotel()));	
		 setVoloModels_a(new VoloDataModel(packet.getLista_voli_andata()));
		 setVoloModels_r(new VoloDataModel(packet.getLista_voli_ritorno()));
		 setEscModels(new EscDataModel(packet.getLista_escursioni()));
		
	}
	
	public VoloDTO getSelectedVolo_a() {
		return selectedVolo_a;
	}

	public void setSelectedVolo_a(VoloDTO selectedVolo_a) {
		this.selectedVolo_a = selectedVolo_a;
	}

	public VoloDTO getSelectedVolo_r() {
		return selectedVolo_r;
	}

	public void setSelectedVolo_r(VoloDTO selectedVolo_r) {
		this.selectedVolo_r = selectedVolo_r;
	}

	public void setSelectedHotels(HotelDTO selectedHotels) {
		this.selectedHotels = selectedHotels;
	}

	public void setSelectedEsc(ArrayList<EscursioneDTO> selectedEsc) {
		this.selectedEsc = selectedEsc;
	}

	public ArrayList<HotelDTO> getFilteredHotels() {
		return filteredHotels;
	}

	public void setFilteredHotels(ArrayList<HotelDTO> filteredHotels) {
		this.filteredHotels = filteredHotels;
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

	public VoloDataModel getVoloModels_a() {
		return voloModels_a;
	}

	public void setVoloModels_a(VoloDataModel voloModels_a) {
		this.voloModels_a = voloModels_a;
	}

	public VoloDataModel getVoloModels_r() {
		return voloModels_r;
	}

	public void setVoloModels_r(VoloDataModel voloModels_r) {
		this.voloModels_r = voloModels_r;
	}
	
	public String acquista_paga()
	{
		if(selectedHotels == null || selectedVolo_a == null || selectedVolo_r == null)
		  {
			return null;
		  }
		
		//creazione viaggio bla bla bla...
		
		return "pagamento.xhtml?faces-redirect=true"; 	
	}
	
	
	//---QUESTI VANNO NEL BEAN GESTIONE GIFT LIST E GESTIONE INVITO 
	public String aggiungi_gift()
	{

		if(selectedHotels == null || selectedVolo_a == null || selectedVolo_r == null)
		  {
			return null;
		  }
		viaggio.setVolo_andata(selectedVolo_a);
		viaggio.setVolo_ritorno(selectedVolo_r);
		viaggio.setHotel(selectedHotels);
		viaggio.setLista_escursioni(selectedEsc);
		
		GiftListDTO gift= new GiftListDTO();
		gift.setViaggio(viaggio);
		gift.setUtente(UMG.getUserDTO());
		gift.setId(2);
		FacesUtil.setSessionMapValue("GiftDTO", gift);		
		//creazione entita gift
		
		
		return "invitogift.xhtml?faces-redirect=true";
	
	}
	
	
	public String invita()
	{

		if(selectedHotels == null || selectedVolo_a == null || selectedVolo_r == null)
		  {
			return null;
		  }
		viaggio.setVolo_andata(selectedVolo_a);
		viaggio.setVolo_ritorno(selectedVolo_r);
		viaggio.setHotel(selectedHotels);
		viaggio.setLista_escursioni(selectedEsc);
		
		InvitoDTO invito = new InvitoDTO();
		invito.setViaggio(viaggio);
		invito.setUtente(UMG.getUserDTO());
		invito.setId(2);
		FacesUtil.setSessionMapValue("SharedDTO", invito);	
		
		//creazione entita invito
		
		
		return "invitoviaggio.xhtml?faces-redirect=true";
		
		
	}

	public ViaggioDTO getViaggio() {
		return viaggio;
	}

	public void setViaggio(ViaggioDTO viaggio) {
		this.viaggio = viaggio;
	}
}
