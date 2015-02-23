package it.polimi.flat.table.support;

import java.io.Serializable;
import java.net.InetAddress;

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
	
	
	
	

	
	
}