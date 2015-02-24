package it.polimi.flat.table.support;

import java.io.Serializable;

public class CommMessage implements Serializable{

	private String idSender; //this will not be encrypted
	private byte[] text; //this will be encrypted
	
	
	
	public CommMessage() {
		super();
	}



	public byte[] getText() {
		return text;
	}



	public void setText(byte[] text) {
		this.text = text;
	}


	public String getIdSender() {
		return idSender;
	}



	public void setIdSender(String idSender) {
		this.idSender = idSender;
	}
	
	

	
	
	
	
	
}
