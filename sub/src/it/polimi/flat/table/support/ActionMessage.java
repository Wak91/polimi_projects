package it.polimi.flat.table.support;

public class ActionMessage implements Message {
	
	private byte[] nodeId;
	private byte[] action; //action that a member want to perform: "leave", "common", "getMember"
	
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
