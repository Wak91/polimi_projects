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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getRowKey(HotelDTO arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
