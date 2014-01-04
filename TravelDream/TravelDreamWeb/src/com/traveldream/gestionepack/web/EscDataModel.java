package com.traveldream.gestionepack.web;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.gestionecomponente.ejb.EscursioneDTO;

public class EscDataModel extends ListDataModel <EscursioneDTO> implements SelectableDataModel <EscursioneDTO> {

	public EscDataModel(List <EscursioneDTO> escdto)
	{super(escdto);}
	
	@Override
	public EscursioneDTO getRowData(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(EscursioneDTO arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
