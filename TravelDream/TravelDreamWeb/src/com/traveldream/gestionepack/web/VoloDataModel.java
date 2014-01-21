package com.traveldream.gestionepack.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;
import com.traveldream.gestionecomponente.ejb.VoloDTO;

public class VoloDataModel  extends ListDataModel <VoloDTO> implements SelectableDataModel <VoloDTO> {

	public VoloDataModel()
	{}
	
	public VoloDataModel(List <VoloDTO> vdto)
	{ super(vdto); }
	
	public VoloDTO getRowData(String arg0) {
		List<VoloDTO> vdto = (List<VoloDTO>) getWrappedData();  
        for(VoloDTO v : vdto) {  
            if((v.getId()+"").equals(arg0))  
                return v;  
        }  
          
        return null;
	}

	@Override
	public Object getRowKey(VoloDTO arg0) {
		return arg0.getId();
	}

}
