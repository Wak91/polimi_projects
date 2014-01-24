package com.traveldream.condivisione.web;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.traveldream.condivisione.ejb.InvitoDTO;

public class InvDataModel extends ListDataModel <InvitoDTO> implements SelectableDataModel <InvitoDTO> {

public InvDataModel(){}
	
	public InvDataModel(List <InvitoDTO> invdto)
	{super(invdto);}
	
	public InvitoDTO getRowData(String arg0) {
		List<InvitoDTO> invdto = ((List<InvitoDTO>) getWrappedData());  
        for(InvitoDTO i : invdto) {  
            if((i.getId()+"").equals(arg0))  
                return i;  
        }  
          
        return null;
	}

	@Override
	public Object getRowKey(InvitoDTO arg0) {
		// TODO Auto-generated method stub
		return arg0.getId();
	}
}
