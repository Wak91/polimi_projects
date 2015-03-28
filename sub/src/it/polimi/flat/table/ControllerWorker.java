package it.polimi.flat.table;

import it.polimi.flat.table.support.ActionMessage;
import it.polimi.flat.table.support.BootMessage;
import it.polimi.flat.table.support.CrashReportMessage;
import it.polimi.flat.table.support.GroupMessage;
import it.polimi.flat.table.support.Message;
import it.polimi.flat.table.support.NetInfoGroupMember;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class ControllerWorker extends Thread{
	
	private GroupController groupController;
	private Socket handledSocket;
	private SecretKey dek;
	private Cipher DesCipher;
	
	
	public ControllerWorker(GroupController gc , Socket handledSocket , SecretKey dek){
		
		this.groupController = gc;
		this.handledSocket = handledSocket;
		this.dek = dek;
		
		try {
			DesCipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		
		System.out.println("Thread spawned for taking care of connection");
		
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		try {
	
			ois = new ObjectInputStream(handledSocket.getInputStream());
			
			System.out.println("fatto ois");
			
			oos = new ObjectOutputStream(handledSocket.getOutputStream());
			Message m = (Message)ois.readObject();
			
			System.out.println(m.getClass().getSimpleName());
			
			 if(m.getClass().getSimpleName().equals("BootMessage")){
			    	
				    System.out.println("[INFO]A boot message has been received");
			    	BootMessage bm = (BootMessage)m;
			    	this.groupController.HandleAddMember(bm,oos);
			    	System.out.println("[INFO]Correctly added the new member");
			    }
			    
			    else //is an ActionMessage ( leave, getGroup, common )
			    	if(m.getClass().getSimpleName().equals("ActionMessage") || m.getClass().getSimpleName().equals("CrashReportMessage")){
			    		
			    		ActionMessage am = (ActionMessage)m;
			    		
			    		System.out.println("message is a " + m.getClass().getSimpleName());
			    		
			    		String nodeId = "";
			    		String action="";
			    		
			    		try {
			    			DesCipher.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
			    			} catch (InvalidKeyException e) {
			    				e.printStackTrace();
			    			}
			    		
			    		 try {
			    				byte[] decryptedId = DesCipher.doFinal(am.getnodeId()); //decrypting the message  
			    				nodeId = new String(decryptedId);
			    				
			    				byte[] decryptedAction = DesCipher.doFinal(am.getAction()); //decrypting the message  
			    				action = new String(decryptedAction);
			    				//System.out.println("action is " + action);
			    				
			    			} catch (IllegalBlockSizeException | BadPaddingException e) {
			    				System.out.println("[ERROR]Something went wrong during decryption of text");
			    				e.printStackTrace();
			    			}
			    		 
			    		 switch(action){
			    		 
			    		 case "leave": {

			    			System.out.println("[INFO]Leave request");
			    			this.groupController.HandleLeavingMember(am);
			    			System.out.println("[INFO]Handled the leave request");

			    		 };break;
			    		 
			    		 case "getGroup": {
			    			 
			    			 System.out.println("[INFO]getGroup request");
			    			 
			    			 GroupMessage gm = new GroupMessage(this.groupController.GetGroup(nodeId,this));
			    			 
			    			 try {
								oos.writeObject(gm); //send in plain the group structure 
							} catch (IOException e) {
								e.printStackTrace();
							}
			    			 
			    			System.out.println("[INFO]Group sended");

			    			 
			    		 };break;
			    		 
			    		 case "broadcastdone":{ //signal a broadcastdone and decrement BroadcastLock
			    			 System.out.println("[INFO]Received a broadcastdone");
			    			 this.groupController.SignalBroadcastDone();
			    			 
			    		 };break;
			    		 
			    		 case "crashreport":{    
			    			 
			    			 CrashReportMessage crm = (CrashReportMessage)am;
			    			 ArrayList <NetInfoGroupMember> crashedMember = crm.getCrashedMembers();
			    			 System.out.println("[INFO]Received a crash report from a member");
			    			 
			    			 ArrayList <String> toDelete = this.groupController.HandleCrashedMembers(crashedMember);					
			    				//rimuoviamo i membri 
			    			 this.groupController.HandleLeavingMember(toDelete);
			    			 System.out.println("Ended crash report\n");  			 		 
			    		 };break;
			    		 
			    	 }//end switch   			
			    } //end ActionMessage if
			 
			 handledSocket.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} 	
	}
}
