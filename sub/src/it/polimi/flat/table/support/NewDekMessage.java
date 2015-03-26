package it.polimi.flat.table.support;

import java.io.Serializable;
import java.util.ArrayList;

public class NewDekMessage implements Serializable,Message {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 7954886678926904742L;
	protected ArrayList<byte[]> newDekList; 
	
	public NewDekMessage(){
		
	}
	
	public ArrayList<byte[]> getnewDekList(){
		return this.newDekList;
	}
	
	public void setnewDekList(ArrayList<byte[]> newDekList){
		this.newDekList = newDekList;
	}

	
	

}
