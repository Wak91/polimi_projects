package com.traveldream.gestionepack.web;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;

import com.traveldream.gestionepack.ejb.PacchettoDTO;



public class PacchettoDataModel extends ListDataModel <PacchettoDTO> implements SelectableDataModel <PacchettoDTO>{

	
	public PacchettoDataModel()
	{}
	
	
	public PacchettoDataModel(List<PacchettoDTO> pdto) {
		super(pdto);
	}
	@Override
	public PacchettoDTO getRowData(String arg0) {
		List<PacchettoDTO> pdto = (List<PacchettoDTO>) getWrappedData();  
        for(PacchettoDTO pacchettoDTO : pdto) {  
            if(pacchettoDTO.getNome().equals(arg0))  
                return pacchettoDTO;  
        }  
		return null;
	}

	@Override
	public Object getRowKey(PacchettoDTO arg0) {
		return arg0.getNome();
	}

}
