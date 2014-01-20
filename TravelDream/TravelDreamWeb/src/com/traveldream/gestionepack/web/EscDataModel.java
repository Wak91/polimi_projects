package com.traveldream.gestionepack.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;

public class EscDataModel extends ListDataModel <EscursioneDTO> implements SelectableDataModel <EscursioneDTO> {

	public EscDataModel(){}
	
	public EscDataModel(List <EscursioneDTO> escdto)
	{super(escdto);}
	
	@Override
	public EscursioneDTO getRowData(String arg0) {
		List<EscursioneDTO> edto = ((List<EscursioneDTO>) getWrappedData());  
        for(EscursioneDTO e : edto) {  
            if(e.getNome().equals(arg0))  
                return e;  
        }  
          
        return null;
	}

	@Override
	public String getRowKey(EscursioneDTO arg0) {
		return arg0.getNome();
	}

}
