package it.polimi.flat.table.support;

import java.io.Serializable;

public class ActionMessage implements Serializable,Message {
	
	/**
	 * 
	 */
	protected static final long serialVersionUID = 7954886678926904742L;
	protected byte[] nodeId;
	protected byte[] action; //action that a member want to perform: "leave", "common", "getMember"
	
	public ActionMessage(){
		
	}
	
	public byte[] getnodeId(){
		return this.nodeId;
	}
	
	public void setnodeId(byte[] nodeId){
		this.nodeId = nodeId;
	}

	public byte[] getAction() {
		return action;
	}

	public void setAction(byte[] action) {
		this.action = action;
	}
	

}
