package com.traveldream.autenticazione.web;

import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  

import com.traveldream.autenticazione.ejb.UserDTO;

public class UserDataModel extends ListDataModel<UserDTO> implements SelectableDataModel<UserDTO> {

	public UserDataModel(){
	}
	
	public UserDataModel(List<UserDTO> userdto){
		super(userdto);
	}

	@Override
	public UserDTO getRowData(String arg0) {
		
		@SuppressWarnings("unchecked")
		List<UserDTO> udto = (List<UserDTO>) getWrappedData();  
        for(UserDTO u : udto) {  
            if(u.getUsername().equals(arg0))  
                return u;  
        }  
          
        return null;
	}
	
	@Override
	public String getRowKey(UserDTO arg0) {
		return arg0.getUsername();
	}
	
}
