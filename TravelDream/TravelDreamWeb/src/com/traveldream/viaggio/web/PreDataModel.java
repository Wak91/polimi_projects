package com.traveldream.viaggio.web;
import java.util.List;
import javax.faces.model.ListDataModel;
import org.primefaces.model.SelectableDataModel;  
import com.traveldream.gestioneprenotazione.ejb.PrenotazioneDTO;

public class PreDataModel extends ListDataModel <PrenotazioneDTO> implements SelectableDataModel <PrenotazioneDTO> {

	public PreDataModel(){}
	
	public PreDataModel(List <PrenotazioneDTO> predto)
	{super(predto);}
	
	public PrenotazioneDTO getRowData(String arg0) {
		List<PrenotazioneDTO> predto = ((List<PrenotazioneDTO>) getWrappedData());  
        for(PrenotazioneDTO p : predto) {  
            if((p.getId()+"").equals(arg0))  
                return p;  
        }  
          
        return null;
	}


	public Object getRowKey(PrenotazioneDTO arg0) {
		return arg0.getId();
	}

}
