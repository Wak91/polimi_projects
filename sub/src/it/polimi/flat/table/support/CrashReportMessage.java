package it.polimi.flat.table.support;

import java.io.Serializable;
import java.util.ArrayList;

public class CrashReportMessage extends ActionMessage implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -877948167814327832L;
	
	private ArrayList <NetInfoGroupMember> crashedMembers;
	

	public CrashReportMessage(){
		super();
		setCrashedMembers(new ArrayList<NetInfoGroupMember>());
	}


	public ArrayList <NetInfoGroupMember> getCrashedMembers() {
		return crashedMembers;
	}


	public void setCrashedMembers(ArrayList <NetInfoGroupMember> crashedMembers) {
		this.crashedMembers = crashedMembers;
	}
	
	
	
}
