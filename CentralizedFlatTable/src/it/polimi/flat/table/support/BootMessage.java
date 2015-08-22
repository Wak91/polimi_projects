package it.polimi.flat.table.support;

import java.io.Serializable;
import java.security.Key;

public class BootMessage implements Serializable,Message {

	
	private static final long serialVersionUID = -7274017849932116650L;
	
	private String id;
	private Key PublicKey;
	private NetInfoGroupMember nigm;


	public BootMessage(String id, Key PublicKey, NetInfoGroupMember nigm){
		
		this.id = id;
		this.PublicKey = PublicKey;
		this.nigm = nigm;
	}
	

	
	public String getId() {
		return id;
	}

	public Key getPublicKey() {
		return PublicKey;
	}



	public NetInfoGroupMember getNigm() {
		return nigm;
	}

}
