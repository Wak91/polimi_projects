package com.traveldream.gestionepack.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.gestionecomponente.ejb.VoloDTO;

public class VoloDataModel  extends ListDataModel <VoloDTO> implements SelectableDataModel <VoloDTO> {

	public VoloDataModel()
	{}
	
	public VoloDataModel(List <VoloDTO> vdto)
	{ super(vdto); }
	
	@Override
	public VoloDTO getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(VoloDTO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
