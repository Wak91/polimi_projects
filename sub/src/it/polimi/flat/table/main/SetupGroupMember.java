package it.polimi.flat.table.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import it.polimi.flat.table.GroupMember;


/*
 * Launch a separate process for every member you want to add in the group
 * (MAX 8)
 * */
public class SetupGroupMember {

	public static void main (String args[]){
		
		//System.out.println("Creating the GroupMember");
		System.out.println("GROUP MEMBER");
		 
		try{
			//ricevamo l id come input			
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    System.out.println("\nEnter the process ID: ");			
		    String id = bufferRead.readLine();
		    //riceviamo la porta come input
		    System.out.println("\nEnter the port: ");			
		    String port = bufferRead.readLine();
		    //creiamo ilnuovo group member
		    GroupMember gm = new GroupMember(Integer.parseInt(id),Integer.parseInt(port),0);
		    //run
			gm.run();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	
		
		/*
	    Integer id = 4;
	    String binaryId = id.toBinaryString(id);
	    
	    System.out.println(binaryId.charAt(0));
	    System.out.println(binaryId.charAt(1));

	    System.out.println(binaryId.charAt(2));
	    */

	}
	

}
