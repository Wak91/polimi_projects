package com.traveldream.condivisione.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.condivisione.ejb.EscursionePagataDTO;

public class EscursionePagataDatamodel extends ListDataModel <EscursionePagataDTO> implements SelectableDataModel <EscursionePagataDTO> {

	public EscursionePagataDatamodel(){}
	
	public EscursionePagataDatamodel(List <EscursionePagataDTO> predto)
	{super(predto);}
	
	public EscursionePagataDTO getRowData(String arg0) {
		List<EscursionePagataDTO> gdto = ((List<EscursionePagataDTO>) getWrappedData());  
        for(EscursionePagataDTO g : gdto) {  
            if((g.getId()+"").equals(arg0))  
                return g;  
        }  
          
        return null;
	}


	public Object getRowKey(EscursionePagataDTO arg0) {
		return arg0.getId();
	}

}