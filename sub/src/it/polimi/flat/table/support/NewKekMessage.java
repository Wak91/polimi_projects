package it.polimi.flat.table.support;

import java.io.Serializable;
import java.util.ArrayList;

public class NewKekMessage implements Serializable,Message {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 7954886678926904742L;
	protected ArrayList<byte[]> newKekList; //action that a member want to perform: "leave", "common", "getMember"
	
	public NewKekMessage(){
		
	}
	
	public ArrayList<byte[]> getnewKekList(){
		return this.newKekList;
	}
	
	public void setnewKekList(ArrayList<byte[]> newDekList){
		this.newKekList = newDekList;
	}

	
	

}
