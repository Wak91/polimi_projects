package com.traveldream.gestionepack.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.gestionecomponente.ejb.HotelDTO;

public class HotelDataModel extends ListDataModel <HotelDTO> implements SelectableDataModel <HotelDTO> {

	public HotelDataModel()
	{}
	
	public HotelDataModel(List <HotelDTO> hoteldto){
		super(hoteldto);
	}

	@Override
	public HotelDTO getRowData(String arg0) {
		
		List<HotelDTO> hdto = (List<HotelDTO>) getWrappedData();  
        for(HotelDTO h : hdto) {  
            if(h.getNome().equals(arg0))  
                return h;  
        }  
          
        return null;
	}

	@Override
	public String getRowKey(HotelDTO arg0) {
		return arg0.getNome();
	}
	
}
