package com.traveldream.gestionepack.web;
import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import com.traveldream.gestionecomponente.ejb.*;

@ManagedBean(name="tableBean") 
@RequestScoped 
public class tableBean {

	@EJB
	private ComponentManagerBeanLocal CMB;
	
	private ArrayList <HotelDTO> selectedHotel;
	private ArrayList <VoloDTO>  selectedVolo;
	private ArrayList <EscursioneDTO> selectedEsc;
    
    private HotelDataModel hotelModels;
    private VoloDataModel  voloModels;
    private EscDataModel escModels;
	
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

	public tableBean()
	{
	selectedHotel  = new ArrayList <HotelDTO>(); // questo per tenere traccia di quelli selezionati
	// MAI METTERE CMB QUA DENTRO, NON E' ANCORA STATO CREATO E QUINDI QUALSIASI COSA
	// FAI TI SBATTE UN SIMPATICO NULL POINTER IN FACCIA 
	}
	
	public void initBean()
	{
		 setHotelModels(new HotelDataModel(CMB.getAllHotel()));	
		 setVoloModels(new VoloDataModel(CMB.getAllVolo()));
		 setEscModels(new EscDataModel(CMB.getAllEscursione()));
	}
	
	public ArrayList<HotelDTO> getSelectedHotel() {
		return selectedHotel;
	}

	public void setSelectedHotel(ArrayList<HotelDTO> selectedHotel) {
		this.selectedHotel = selectedHotel;
	}

	public HotelDataModel getHotelModels() {
		return hotelModels;
	}

	public void setHotelModels(HotelDataModel hotelModels) {
		this.hotelModels = hotelModels;
	}

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
	

}
