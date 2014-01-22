package com.traveldream.viaggio.web;

import java.util.ArrayList;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.gestionecomponente.ejb.ComponentManagerBeanLocal;
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
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;
import com.traveldream.util.web.FacesUtil;


@ManagedBean(name="ViaggioBean") 
@SessionScoped
public class ViaggioBean {
	
	@EJB
	private PackManagerBeanLocal PMB;
    
	@EJB
	private BookManagerBeanLocal BMB; 
	
	@EJB
	private ComponentManagerBeanLocal CMB;
	
	@EJB
	private UserMgr userMgr;
	
	private HotelDTO selectedHotels;
	private VoloDTO selectedVolo_a;
	private VoloDTO selectedVolo_r;
	private ArrayList <EscursioneDTO> selectedEsc;
	private PrenotazioneDTO selectedpre;
	
	
	private HotelDataModel hotelModels;
    private VoloDataModel  voloModels_a;
    private VoloDataModel  voloModels_r;
    private EscDataModel escModels;
    private PreDataModel premodels;
    
 
	private ArrayList<HotelDTO> filteredHotels;
	private ArrayList<EscursioneDTO> filteredEscursiones;
	private ArrayList<VoloDTO> filteredVolos;
    
	private PacchettoDTO packet;

    private ViaggioDTO viaggio;
    private PrenotazioneDTO prenotazione;
    
    private int n_partecipanti;
    private int quotacomplessiva;
    private int quotapp;
   

	private String password1;
	
	public ViaggioBean() {
		
		 packet = new PacchettoDTO();
		 viaggio = new ViaggioDTO();
		 prenotazione = new PrenotazioneDTO();
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
	
	//Riempie la struttura premodels per permettere di visualizzare nella view tutte le prenotazioni di un utente
	public void getPrenotazioni()
	{
		UserDTO current_user = userMgr.getUserDTO();
		setPremodels(new PreDataModel(BMB.cercaPrenotazione(current_user)));
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

	public HotelDTO getSelectedHotels() {
		return selectedHotels;
	}


	public ArrayList<EscursioneDTO> getSelectedEsc() {
		return selectedEsc;
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
	
	
    public PreDataModel getPremodels() {
			return premodels;
		}

    public void setPremodels(PreDataModel premodels) {
			this.premodels = premodels;
		}

		
	public String acquista_paga()
	{
		if(selectedHotels==null || selectedVolo_a == null || selectedVolo_r == null)
		  {
			return "userhome.xhtml?faces-redirect=true";
		  }
		
		viaggio.setHotel(selectedHotels);
		viaggio.setVolo_andata(selectedVolo_a);
		viaggio.setVolo_ritorno(selectedVolo_r);
		viaggio.setLista_escursioni(selectedEsc);
		
		return "pagamento.xhtml?faces-redirect=true"; 	
	}
	
	public void calcoloquota()
	{
      int cg = viaggio.getHotel().getCosto_giornaliero();
      
      int duration = (int) (( viaggio.getData_fine().getTime() - viaggio.getData_inizio().getTime() ) / (1000 * 60 * 60 * 24));
	
      int hotel_stay = cg * duration;
      
      int costi_voli = viaggio.getVolo_andata().getCosto() + viaggio.getVolo_ritorno().getCosto();
      
      int costi_escursioni=0;
      
      for(EscursioneDTO edto: viaggio.getLista_escursioni())
         {
    	  costi_escursioni+=edto.getCosto();
         }
     this.setQuotapp(cg+costi_voli+costi_escursioni+hotel_stay);
	 this.setQuotacomplessiva(this.getQuotapp());
	}
	
	public void updatePrice()
	{
		this.setQuotacomplessiva(this.getN_partecipanti() * this.getQuotapp());
	}
	
	public String creaPrenotazione()
	{
		ArrayList <Integer> id_escursioni = new ArrayList <Integer> ();
		int id_e;
		// salvo le copie degli elementi selezionati per la creazione del viaggio, controllando se esistono già
		int id_h = BMB.cercaHotelSalvato(selectedHotels);
		if(id_h == -1 )
		   {
			id_h  = CMB.saveHotelSalvato(selectedHotels); //recupero gli id delle copie appena salvate
		   }
		int id_vsa = BMB.cercaVoloSalvato(selectedVolo_a);
		if(id_vsa == -1)
		   {
			id_vsa = CMB.saveVoloSalvato(selectedVolo_a);
		   }
		int id_vsr = BMB.cercaVoloSalvato(selectedVolo_r);
		if(id_vsr == -1)
		  {
			id_vsr = CMB.saveVoloSalvato(selectedVolo_r);
		  }
		
		for(EscursioneDTO edto: selectedEsc)
		   {
			id_e = BMB.cercaEscursioneSalvata(edto);
			if(id_e == -1 )
			  {
				id_e = BMB.saveEscursioneSalvata(edto);
			  }
			id_escursioni.add(id_e); // salvo tutti gli id delle escursioni in entrambi i casi ( salvate o no )
		    edto.setId(id_e); // già che sto ciclando sulle selectedEsc metto a posto il DTO con il nuovo ID
		   }
		
	    selectedHotels.setId(id_h); //aggiorno gli id dei DTO, solo quelli perchè gli altri campi sono gia' a posto
	    selectedVolo_a.setId(id_vsa);
	    selectedVolo_r.setId(id_vsr);
	    
			
	    viaggio.setHotel(selectedHotels); // ora selectedHotels ecc.. si riferiscono a entity delle tabelle XSalvato
	    viaggio.setVolo_andata(selectedVolo_a);
	    viaggio.setVolo_ritorno(selectedVolo_r);
	    viaggio.setLista_escursioni(selectedEsc);
	    
	    int id = BMB.cercaViaggio(viaggio); // vado alla ricerca di possibili duplicati del viaggio appena creato
	    if(id == -1 )
	       id= BMB.saveViaggio(viaggio);
	  
	    viaggio.setId(id);
				       
		prenotazione.setViaggio(viaggio);
		prenotazione.setNumero_persone(n_partecipanti);
		prenotazione.setUtente(userMgr.getUserDTO());
		prenotazione.setCosto(quotacomplessiva);
		
	    BMB.savePrenotazione(prenotazione);
	    
		return "imieiviaggi.xhtml?faces-redirect=true"; 	
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
		gift.setUtente(userMgr.getUserDTO());
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
		
		
		//creazione entita invito
		
		
		return "invitoviaggio.xhtml?faces-redirect=true";
		
		
	}

	public ViaggioDTO getViaggio() {
		return viaggio;
	}

	public void setViaggio(ViaggioDTO viaggio) {
		this.viaggio = viaggio;
	}

	public int getN_partecipanti() {
		return n_partecipanti;
	}

	public void setN_partecipanti(int n_partecipanti) {
		this.n_partecipanti = n_partecipanti;
	}


	public int getQuotacomplessiva() {
		return quotacomplessiva;
	}

	public void setQuotacomplessiva(int quotacomplessiva) {
		this.quotacomplessiva = quotacomplessiva;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	 public int getQuotapp() {
			return quotapp;
		}

		public void setQuotapp(int quotapp) {
			this.quotapp = quotapp;
		}

		public PrenotazioneDTO getPrenotazione() {
			return prenotazione;
		}

		public void setPrenotazione(PrenotazioneDTO prenotazione) {
			this.prenotazione = prenotazione;
		}

		public PrenotazioneDTO getSelectedpre() {
			return selectedpre;
		}

		public void setSelectedpre(PrenotazioneDTO selectedpre) {
			this.selectedpre = selectedpre;
		}
			
	/*
	 * dovrebbe servire per filtrare i risultati in base alle date di inzio e fine del viaggio ( TODO )
	public void filterComponents(){
		
		 filteredEscursiones = PMB.getListaEscursioniCompatibili(packet.getDestinazione(), this.data_inizio, this.data_fine);
		 filteredHotels = PMB.getListaHotelCompatibili(packet.getDestinazione(), this.data_inizio, this.data_fine);
		 
		 filteredVolos = PMB.getListaVoliCompatibili(packet.getDestinazione(), this.data_inizio, this.data_fine);
		
		 ArrayList <VoloDTO> filteredVolo_a = new ArrayList <VoloDTO>();
		 ArrayList <VoloDTO> filteredVolo_r = new ArrayList <VoloDTO> ();
		 
		 for(VoloDTO v: filteredVolos)
		    {
			 if(v.getLuogo_partenza().equals(this.packet.getDestinazione()))
				 filteredVolo_a.add(v);
			 else
				 
		    }
		 
		 setHotelModels(new HotelDataModel(filteredHotels));	
		 setVoloModels(new VoloDataModel(filteredVolos));
		 setEscModels(new EscDataModel(filteredEscursiones));
	}
	*/
}
