package it.polimi.flat.table.support;

import java.io.Serializable;
import java.net.InetAddress;

/*
 * This message is sent from the group member 
 * to the group controller in order to inform it 
 * of its own listening socket
 * */
public class NetInfoGroupMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2432791115605410018L;
	
	private InetAddress ipAddress;
	private Integer port;
	
	
	public NetInfoGroupMember(InetAddress ipAddress, Integer port) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	
	public InetAddress getIpAddress() {
		return ipAddress;
	}


	public Integer getPort() {
		return port;
	}
	
	
	
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}


	public void setPort(Integer port) {
		this.port = port;
	}
	
	

	
	
}
