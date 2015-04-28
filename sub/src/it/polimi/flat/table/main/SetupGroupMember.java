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

		//ricevamo l id come input			
		//BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		//System.out.println("\nEnter the process ID: ");			
		//String id = bufferRead.readLine();
		//riceviamo la porta come input
		//System.out.println("\nEnter the port: ");			
		//String port = bufferRead.readLine();
		//creiamo ilnuovo group member
		GroupMember gm = new GroupMember(Integer.parseInt(args[0]),Integer.parseInt(args[1]),args[2],args[3]);
		//run
		gm.run();

	}
	

}
