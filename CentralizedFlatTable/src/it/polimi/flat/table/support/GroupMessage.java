package it.polimi.flat.table.support;

import java.io.Serializable;
import java.util.HashMap;

public class GroupMessage implements Serializable, Message{

	private static final long serialVersionUID = 7695252900800603833L;
	
	private HashMap <String,NetInfoGroupMember> group;
	
	public GroupMessage(HashMap <String,NetInfoGroupMember> group ){
		this.setGroup(group);
	}

	public HashMap <String,NetInfoGroupMember> getGroup() {
		return group;
	}

	public void setGroup(HashMap <String,NetInfoGroupMember> group) {
		this.group = group;
	}
	
}
