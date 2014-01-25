package com.traveldream.condivisione.web;
import java.util.List;

import javax.faces.model.ListDataModel;

import org.primefaces.model.SelectableDataModel;  


public class amiciDatamodel extends ListDataModel <String> implements SelectableDataModel <String> {

	public amiciDatamodel(){}
	
	public amiciDatamodel(List <String> predto)
	{super(predto);}
	
	public String getRowData(String arg0) {
		List<String> gdto = ((List<String>) getWrappedData());  
        for(String g : gdto) {  
            if((g).equals(arg0))  
                return g;  
        }  
          
        return null;
	}


	public Object getRowKey(String arg0) {
		return arg0;
	}

}