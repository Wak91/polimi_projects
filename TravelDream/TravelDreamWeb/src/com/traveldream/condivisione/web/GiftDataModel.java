package com.traveldream.condivisione.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.condivisione.ejb.GiftListDTO;

public class GiftDataModel extends ListDataModel <GiftListDTO> implements SelectableDataModel <GiftListDTO> {

	public GiftDataModel(){}
	
	public GiftDataModel(List <GiftListDTO> predto)
	{super(predto);}
	
	public GiftListDTO getRowData(String arg0) {
		List<GiftListDTO> gdto = ((List<GiftListDTO>) getWrappedData());  
        for(GiftListDTO g : gdto) {  
            if((g.getId()+"").equals(arg0))  
                return g;  
        }  
          
        return null;
	}


	public Object getRowKey(GiftListDTO arg0) {
		return arg0.getId();
	}

}