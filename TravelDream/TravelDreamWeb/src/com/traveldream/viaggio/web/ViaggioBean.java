package com.traveldream.viaggio.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Min;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.condivisione.ejb.InvitoManagerBeanLocal;
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
	private InvitoManagerBeanLocal IMB;
	
	@EJB
	private UserMgr userMgr;
	
	private HotelDTO selectedHotels;
	private VoloDTO selectedVolo_a;
	private VoloDTO selectedVolo_r;
	private ArrayList <EscursioneDTO> selectedEsc;
	private EscursioneDTO esc;				//temporary value to store selected esc
	private PrenotazioneDTO selectedpre;
	
	
	private HotelDataModel hotelModels;
    private VoloDataModel  voloModels_a;
    private VoloDataModel  voloModels_r;
    private EscDataModel escModels;
    private PreDataModel premodels;
    
 
	private ArrayList<HotelDTO> filteredHotels;
	private ArrayList<EscursioneDTO> filteredEscursiones;
	private ArrayList<VoloDTO> filteredVolos;
	private ArrayList <VoloDTO> filteredVolosRitorno;
    
	private PacchettoDTO packet;

    private ViaggioDTO viaggio;
    private PrenotazioneDTO prenotazione;
    
	@Min(1)
    private int n_partecipanti;
    private int quotacomplessiva;
    private int quotapp;
    private int last_id;
    
    private ArrayList <PrenotazioneDTO> lista_prenotazioni;
   
    //filtri
    String partenza;
 
    Integer stelle;


    

	private String password1;
	
	public ViaggioBean() {
		
		 packet = new PacchettoDTO();
		 viaggio = new ViaggioDTO();
		 prenotazione = new PrenotazioneDTO();
		 selectedEsc=new ArrayList<EscursioneDTO>();
	}

	
	public void filterViaggio(){
		if (viaggio.getData_inizio()!=null &&viaggio.getData_fine()!=null &&viaggio.getData_inizio().after(viaggio.getData_fine())){
			  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,"Info message", "La data di fine deve essere successiva a quella di inizio"));  	

		}
		filteredVolos=filterVoliA(packet);
		filteredVolosRitorno=filterVoliR(packet);
		filteredHotels=filterHotels(packet);
		filteredEscursiones=filterEscursioni(packet);
		restoreSelected();
		
	}
	
	public ArrayList<VoloDTO> filterVoliA(PacchettoDTO packet) {
		ArrayList<VoloDTO> voli =new ArrayList<VoloDTO>();
		for (VoloDTO voloDTO : packet.getLista_voli_andata()) {
			if((viaggio.getData_inizio()==null || voloDTO.getData().equals(viaggio.getData_inizio())) &&
					(partenza==null || partenza.equals("") || partenza.matches(voloDTO.getLuogo_partenza()+"*"))){
				voli.add(voloDTO);
			}
		}
		return voli;
	}
	public ArrayList<VoloDTO> filterVoliR(PacchettoDTO packet) {
		ArrayList<VoloDTO> voli =new ArrayList<VoloDTO>();
		for (VoloDTO voloDTO : packet.getLista_voli_ritorno()) {
			if((viaggio.getData_fine()==null || voloDTO.getData().equals(viaggio.getData_fine())) &&
					(partenza==null || partenza.matches(voloDTO.getLuogo_arrivo()+"*") ||  partenza.equals("")) ){
				voli.add(voloDTO);
			}
			
		}
		return voli;
	}
	private boolean after_equal(Date date1,Date date2) {
		return (date1.after(date2) || date1.equals(date2));
	}
	private boolean before_equal(Date date1,Date date2) {
		return (date1.before(date2) || date1.equals(date2));
	}
	
	public ArrayList<EscursioneDTO> filterEscursioni(PacchettoDTO pacchettoDTO){
		ArrayList<EscursioneDTO> filtered = new ArrayList<EscursioneDTO>();
		for (EscursioneDTO escursioneDTO : pacchettoDTO.getLista_escursioni()) {
			if(viaggio.getData_inizio()==null || viaggio.getData_fine()==null || 
			(after_equal(escursioneDTO.getData(), viaggio.getData_inizio()) && before_equal(escursioneDTO.getData(), viaggio.getData_fine()))){
				filtered.add(escursioneDTO);
			}
		}
		return filtered;
	}
	public ArrayList<HotelDTO> filterHotels(PacchettoDTO packeDto){
		ArrayList<HotelDTO> filtered=new ArrayList<HotelDTO>();
		for (HotelDTO hotelDTO : packeDto.getLista_hotel()) {
			if (hotelDTO.getStelle() == stelle ||stelle==null &&
					( viaggio.getData_inizio()==null || after_equal(viaggio.getData_inizio(), hotelDTO.getData_inizio()) ) &&
					(viaggio.getData_fine()==null || before_equal(viaggio.getData_fine(), hotelDTO.getData_fine()))){
				filtered.add(hotelDTO);
			}
		}
		
		return filtered;
	}
	
	public void showAll(){
		 filteredHotels=(ArrayList<HotelDTO>)packet.getLista_hotel();	
		 filteredVolos=(ArrayList<VoloDTO>)packet.getLista_voli_andata();	
		 filteredVolosRitorno=(ArrayList<VoloDTO>)packet.getLista_voli_ritorno();	
		 filteredEscursiones=(ArrayList<EscursioneDTO>) packet.getLista_escursioni();
		 restoreSelected();
		 viaggio.setData_inizio(null);
		 viaggio.setData_fine(null);
		 System.out.println();
	}
	

	public void selezionaEsc(){
		if(!selectedEsc.contains(esc)){
		selectedEsc.add(esc);
		}
		for (EscursioneDTO escursioneDTO : selectedEsc) {
			System.out.println("esc"+escursioneDTO.getNome());
		}
	}
	
	public void deselezionaEsc() {
		if (selectedEsc.contains(esc)){
		selectedEsc.remove(esc);
		}
	}
	
	private void restoreSelected(){
		 selectedEsc=new ArrayList<EscursioneDTO>();
		 selectedHotels=null;
		 selectedVolo_a=null;
		 selectedVolo_r=null;
		 
		 

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
	

	public void getPacchettoById(int id) throws IOException
	{	 if (last_id==0) {
		last_id=id;
	}
	try {
 		 this.packet = PMB.getPacchettoByID(id);
 		 last_id = packet.getId();

	} catch (Exception e) {
		try {
			this.packet = PMB.getPacchettoByID(last_id);
			last_id = packet.getId();
		} catch (Exception e2) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("userhome.xhtml");
			return;
		} 

	}
		 filteredHotels=(ArrayList<HotelDTO>)packet.getLista_hotel();	
		 filteredVolos=(ArrayList<VoloDTO>)packet.getLista_voli_andata();	
		 filteredVolosRitorno=(ArrayList<VoloDTO>)packet.getLista_voli_ritorno();	
		 filteredEscursiones=(ArrayList<EscursioneDTO>) packet.getLista_escursioni();
		 viaggio.setData_inizio(null);
		 viaggio.setData_fine(null);

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
		int id_pack = packet.getId();

		if(selectedHotels==null || selectedVolo_a == null || selectedVolo_r == null 
			|| viaggio.getData_fine() == null || viaggio.getData_inizio() == null)
		  {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore: hai lasciato qualche campo vuoto, controlla i dati inseriti" ));  	

			return "creaviaggio.xhtml?id=id_pack";
		  }
		
		System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS hotel "+selectedHotels.getNome());
		System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS andata"+selectedVolo_a.getCompagnia());
		System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS ritorno "+selectedVolo_r.getCompagnia());
		viaggio.setHotel(selectedHotels);
		viaggio.setVolo_andata(selectedVolo_a);
		viaggio.setVolo_ritorno(selectedVolo_r);
		viaggio.setLista_escursioni(selectedEsc);
		viaggio.setNome(packet.getNome());
		for (EscursioneDTO escursioneDTO : viaggio.getLista_escursioni()) {
			System.out.println("esc in viaggio "+escursioneDTO.getNome());
		}
		//Controllo che le date dei voli scelti, e delle escursioni siano a posto.
		// Gli hotel vengono filtrati in base alla disponibilità, si da per scontato
		//per semplicità che l'hotel sia sempre disponibile nelle date scelte del viaggio
		//Controllo anche che le date del viaggio non sparino fuori dalla disponibilità del pack
		
		//Possibili errori durante la creazione---------------------------------

		if(big_check()==0)
		{  
			//MESSAGGIO ERRORE, CONTROLLA I DATI INSERITI
			//DEBUG
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_fine()+"");
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_inizio()+"");
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_a.getData()+"");
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_r.getData()+"");
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_inizio()+"");
			//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_fine()+"");
			restoreSelected();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore durante la creazione del tuo viaggio, controlla i dati inseriti" ));  	
			return "creaviaggio.xhtml?id=id_pack";

		}
		
		return "pagamento.xhtml?faces-redirect=true"; 	
	}
	
	@SuppressWarnings("deprecation")
	private int check_giorni_coperti()
	{
		java.util.Date andata_volo = selectedVolo_a.getData();
		java.util.Date ritorno_volo = selectedVolo_r.getData();
		java.util.Date inizio_viaggio = viaggio.getData_inizio();
		java.util.Date fine_viaggio = viaggio.getData_fine();
		java.util.Date inizio_hotel = selectedHotels.getData_inizio();
		java.util.Date fine_hotel = selectedHotels.getData_fine();

		
		if(
		    andata_volo.equals(inizio_viaggio) &&
		    ritorno_volo.equals(fine_viaggio) &&
		    ( inizio_hotel.after( inizio_viaggio ) == false ) && // rispetta requisito "niente barbonate"
		    ( fine_hotel.before(fine_viaggio)) == false 
		    
		   )
		  {
			return 1;
		  }
		else
			return 0;
		
		
	}
	
	private int escOutOfData()
	{
		for(EscursioneDTO edto: selectedEsc)
		  {
			if(edto.getData().before(viaggio.getData_inizio()) || edto.getData().after(viaggio.getData_fine()))
			  {
				return 1;
			  }
		  }
		return 0;
		
	}
	
	
	private int big_check()
	{
	 if( viaggio.getData_fine()==null || viaggio.getData_inizio()==null || 
			   check_giorni_coperti()== 0  ||
			   escOutOfData() == 1 ||
			   viaggio.getData_inizio().before(packet.getData_inizio()) || 
			   viaggio.getData_fine().after(packet.getData_fine())
	)	
	  {
		 return 0; // problemi nella creazione del viaggio
	  }
	 else 
		 return 1; //viaggio tutto ok
		
	}
	
	
	public void updateQuotaComp(){
		quotacomplessiva = n_partecipanti*quotapp;
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
     this.quotacomplessiva = this.getN_partecipanti() * this.getQuotapp();
  	}
	
	public String creaPrenotazione()
	{	
		this.calcoloquota();
	    ViaggioDTO viaggio_creato = BMB.saveViaggio(viaggio);	// salvo il viaggio se non esiste già

		for (EscursioneDTO escursioneDTO : viaggio.getLista_escursioni()) {
			System.out.println("viaggio  "+escursioneDTO.getNome());
		}
	    for (EscursioneDTO escursioneDTO : viaggio_creato.getLista_escursioni()) {
			System.out.println("viaggio creato "+escursioneDTO.getNome());
		}
		prenotazione.setViaggio(viaggio_creato);
		prenotazione.setNumero_persone(n_partecipanti);
		prenotazione.setUtente(userMgr.getUserDTO());
		prenotazione.setCosto(quotacomplessiva);
		
	    BMB.savePrenotazione(prenotazione);
	    
		 restoreSelected();

	    
		return "imieiviaggi.xhtml?faces-redirect=true";
		
	}
	
	
	
	
	//---QUESTI VANNO NEL BEAN GESTIONE GIFT LIST E GESTIONE INVITO 
	public String aggiungi_gift()
	{
		int id_pack = packet.getId();
		
		if(selectedHotels==null || selectedVolo_a == null || selectedVolo_r == null)
		  {
			restoreSelected();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore durante la creazione del tuo viaggio, controlla i dati inseriti" ));  	
			return "creaviaggio.xhtml?id=id_pack";
		  }
		

		if(big_check()==0)
				{  
					//MESSAGGIO ERRORE, CONTROLLA I DATI INSERITI
					//DEBUG
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_fine()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_inizio()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_a.getData()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_r.getData()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_inizio()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_fine()+"");
					restoreSelected();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore durante la creazione del tuo viaggio, controlla i dati inseriti" ));  	
					return "creaviaggio.xhtml?id=id_pack";

				}
		
         viaggio.setHotel(selectedHotels);
         viaggio.setVolo_andata(selectedVolo_a);
         viaggio.setVolo_ritorno(selectedVolo_r);
		 viaggio.setLista_escursioni(selectedEsc);
		viaggio.setNome(packet.getNome());

		GiftListDTO gift= new GiftListDTO();
		gift.setViaggio(viaggio);
		gift.setUtente(userMgr.getUserDTO());
		FacesUtil.setSessionMapValue("GiftDTO", gift);	
		
		//creazione entita gift
		
		 restoreSelected();

		return "invitogift.xhtml?faces-redirect=true";
	
	}
	
	
	public String invita()
	{
		int id_pack = packet.getId();

		if(selectedHotels == null || selectedVolo_a == null || selectedVolo_r == null )
		  {
			restoreSelected();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore durante la creazione del tuo viaggio, controlla i dati inseriti" ));  	

			return "creaviaggio.xhtml?id=id_pack";
		  }
		

		if(big_check()==0)
				{  
					//MESSAGGIO ERRORE, CONTROLLA I DATI INSERITI
					//DEBUG
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_fine()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +viaggio.getData_inizio()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_a.getData()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +selectedVolo_r.getData()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_inizio()+"");
					//System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" +packet.getData_fine()+"");
					restoreSelected();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Info message", "Errore durante la creazione del tuo viaggio, controlla i dati inseriti" ));  	
					return "creaviaggio.xhtml?id=id_pack";

				}
		
		viaggio.setVolo_andata(selectedVolo_a);
		viaggio.setVolo_ritorno(selectedVolo_r);
		viaggio.setHotel(selectedHotels);
		viaggio.setLista_escursioni(selectedEsc);
		viaggio.setNome(packet.getNome());
		
		InvitoDTO invito = new InvitoDTO();
		
		invito.setViaggio(viaggio);
		invito.setStatus(false);
		invito.setUtente(userMgr.getUserDTO());
		invito.setId(viaggio.getId());
		FacesUtil.setSessionMapValue("InvDTO", invito);	

		
		restoreSelected();

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
		
		public String searchViaggioById(int id){
			try {
				ViaggioDTO viaggio_dto = BMB.cercaViaggioById(id);
				setViaggio(viaggio_dto);
				return "/utente/pagamentoDaMieiViaggi.xhtml?faces-redirect=true";
			} catch (NullPointerException e){
				System.out.println("null");
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
		        FacesContext.getCurrentInstance().addMessage("null", message);
				return "/utente/imieiviaggi.xhtml?faces-redirect=true";
			}	
		}


		public String getPartenza() {
			return partenza;
		}


		public void setPartenza(String partenza) {
			this.partenza = partenza;
		}




		public Integer getStelle() {
			return stelle;
		}


		public void setStelle(Integer stelle) {
			this.stelle = stelle;
		}

		public ArrayList <PrenotazioneDTO> getLista_prenotazioni() {
			return lista_prenotazioni;
		}

		public void setLista_prenotazioni(ArrayList <PrenotazioneDTO> lista_prenotazioni) {
			this.lista_prenotazioni = lista_prenotazioni;
		}
			
       public String poutcome(int id)
       {
  		 this.packet = PMB.getPacchettoByID(id);
  		 return "creaviaggio.xhtml";
       }

	public ArrayList <VoloDTO> getFilteredVolosRitorno() {
		return filteredVolosRitorno;
	}

		public EscursioneDTO getEsc() {
			return esc;
		}


		public void setEsc(EscursioneDTO esc) {
			this.esc = esc;
		}
		
		
}
