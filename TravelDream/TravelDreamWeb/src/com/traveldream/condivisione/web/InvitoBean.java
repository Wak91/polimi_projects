package com.traveldream.condivisione.web;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.Min;

import org.primefaces.context.RequestContext;

import com.traveldream.autenticazione.ejb.UserDTO;
import com.traveldream.autenticazione.ejb.UserMgr;
import com.traveldream.condivisione.ejb.GiftListDTO;
import com.traveldream.condivisione.ejb.InvitoDTO;
import com.traveldream.condivisione.ejb.InvitoManagerBeanLocal;
import com.traveldream.gestionepack.ejb.PackManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.BookManagerBeanLocal;
import com.traveldream.gestioneprenotazione.ejb.ViaggioDTO;
import com.traveldream.util.web.FacesUtil;
import com.traveldream.viaggio.web.PreDataModel;

@ManagedBean(name = "InvitoBean") 
@SessionScoped
public class InvitoBean {
	
	InvitoDTO invito;
	ViaggioDTO viaggio;
	String amico;
	
	@Min(1)
	int id_amico;
	
	private String mail;
	
    
	private InvDataModel invmodels;

	
	@EJB
	private InvitoManagerBeanLocal IMB;
	
	@EJB
	private BookManagerBeanLocal BMB;
	
	@EJB 
	private UserMgr userMgr;
	
	public InvitoBean(){
	}
	
	
	@PostConstruct
    public void init(){
		
		invito = (InvitoDTO)FacesUtil.getSessionMapValue("InvDTO");

		if (invito==null){
			System.out.println("XXXXXXXXXXXXXXinding cazzi");
		}
    }
	
	public String reinit(){  
        amico = new String();           
        return null;  
    }  
	
	public String getAmico(){
		return amico;
	}
	public void setAmico(String amico){
		this.amico = amico;
	}

	public InvitoDTO getInvito(){
		return invito;
	}

	public void setInvito(InvitoDTO invito){
		this.invito = invito;
	}
	
	public String submit(){
		if (invito==null) {
			System.out.println("null");
		} else {
		
		invito.setAmico(amico);
		invito.getViaggio().getHotel().getNome();
		ViaggioDTO v_dto = BMB.saveViaggio(invito.getViaggio());
		invito.getViaggio().setId(v_dto.getId()); 
		IMB.submit(getInvito());
		}
		
		return "/utente/imieiviaggi.xhtml?faces-redirect=true";
	}

	public void getInviti()
	{ 
		UserDTO current_user = userMgr.getUserDTO();
		System.out.println("Current:" +current_user.getUsername());
		
		setInvmodels(new InvDataModel(IMB.cercaInvito(current_user)));
	}
	
    public InvDataModel getInvmodels() {
			return invmodels;
		}
    
    public void setInvmodels(InvDataModel invmodels) {
		this.invmodels = invmodels;
	}

	/**
	 * @return the id_amico
	 */
	public int getId_amico() {
		return id_amico;
	}

	/**
	 * @param id_amico the id_amico to set
	 */
	public void setId_amico(int id_amico) {
		this.id_amico = id_amico;
	}
	
	public String searchInv(){
		try {
			InvitoDTO invito_dto = IMB.cercaInvitoById(getId_amico(), getMail());
			setInvito(invito_dto);
			System.out.println(invito_dto.getViaggio().getHotel().getNome());
			System.out.println(invito_dto.getAmico());
			System.out.println(invito_dto.getStatus());
			return "/amico/viewInvito.xhtml?faces-redirect=true";
		} catch (NullPointerException e){
			System.out.println("null");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
	        FacesContext.getCurrentInstance().addMessage("null", message);
			return "amico/idForm.xhtml?faces-redirect=true";
		}	
	}
	
	public String partecipa(){
		invito.setStatus(true);
		IMB.cambiaStato(invito);
		System.out.println("stato cambiato");
		System.out.println(invito.getStatus());
		return "/homepage.xhtml?faces-redirect=true";
	}


	/**
	 * @return the mail
	 */
	public String getMail() {
		return mail;
	}


	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String searchInvForConfirm(){
		try {
			InvitoDTO invito_dto = IMB.cercaInvitoById(getId_amico(), getMail());
			setInvito(invito_dto);
			System.out.println(invito_dto.getViaggio().getHotel().getNome());
			System.out.println(invito_dto.getAmico());
			System.out.println(invito_dto.getStatus());
			return "/utente/confermaInvito.xhtml?faces-redirect=true";
		} catch (NullPointerException e){
			System.out.println("null");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
	        FacesContext.getCurrentInstance().addMessage("null", message);
			return "/utente/userhome.xhtml?faces-redirect=true";
		}

		
	}
	
	public String searchInvById(int id){
		try {
			InvitoDTO invito_dto = IMB.cercaInvitoById(id);
			invito_dto.setStatus(false);
			setInvito(invito_dto);
			return "/utente/shareInvito.xhtml?faces-redirect=true";
		} catch (NullPointerException e){
			System.out.println("null");
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "What we do in life", "Echoes in eternity.");
	        FacesContext.getCurrentInstance().addMessage("null", message);
			return "/utente/imieiviaggi.xhtml?faces-redirect=true";
		}	
	}
	
}
	
	
	
	
	
	
	
	

