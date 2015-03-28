package it.polimi.flat.table.support;

import java.io.Serializable;

public class ActionMessage implements Serializable,Message {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 7954886678926904742L;
	protected String nodeId;
	protected String action; //action that a member want to perform: "leave", "common", "getMember"
	
	public ActionMessage(){
		
	}
	
	public String getnodeId(){
		return this.nodeId;
	}
	
	public void setnodeId(String nodeId){
		this.nodeId = nodeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	

}
