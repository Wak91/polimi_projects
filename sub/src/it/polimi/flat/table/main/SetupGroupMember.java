package it.polimi.flat.table.main;

import it.polimi.flat.table.GroupMember;


/*
 * Launch a separate process for every member you want to add in the group
 * (MAX 8)
 * */
public class SetupGroupMember {
	
	public static void main (String args[]){
		
		//System.out.println("Creating the GroupMember");
		GroupMember gm = new GroupMember(Integer.parseInt("2"),5558);
		gm.run();
		//GroupMember gm1 = new GroupMember(Integer.parseInt("1"),5556);
		//gm1.start();
		/*
	    Integer id = 4;
	    String binaryId = id.toBinaryString(id);
	    
	    System.out.println(binaryId.charAt(0));
	    System.out.println(binaryId.charAt(1));

	    System.out.println(binaryId.charAt(2));
	    */

	}
	

}
