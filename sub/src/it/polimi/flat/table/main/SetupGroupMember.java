package it.polimi.flat.table.main;

import it.polimi.flat.table.GroupMember;


/*
 * Launch a separate process for every member you want to add in the group
 * (MAX 8)
 * */
public class SetupGroupMember {

	public static void main (String args[]){
		
		//System.out.println("Creating the GroupMember");
		GroupMember gm = new GroupMember(Integer.parseInt("0"),5599);

		gm.run();

		
		/*
	    Integer id = 4;
	    String binaryId = id.toBinaryString(id);
	    
	    System.out.println(binaryId.charAt(0));
	    System.out.println(binaryId.charAt(1));

	    System.out.println(binaryId.charAt(2));
	    */

	}
	

}
