package it.polimi.flat.table;

import it.polimi.flat.table.support.ActionMessage;
import it.polimi.flat.table.support.BootMessage;
import it.polimi.flat.table.support.CommMessage;
import it.polimi.flat.table.support.Message;
import it.polimi.flat.table.support.NetInfoGroupMember;
import it.polimi.flat.table.support.StartConfigMessage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * This is the class for the group controller
 * of the centralized flat table.
 * It handle all the join and leaving of 
 * the group member.
 * */
public class GroupController extends Thread {
	
	
	private Integer port=56520;
	private ServerSocket mySocket;
	
	private SecretKey dek; //the DEK of the group
	private HashMap <String,SecretKey> table; //hashmap that store the flatTable 
	private HashMap <String,NetInfoGroupMember> group; //table to keep track of the 'sockets' of the member in the group
	private Integer lock; //this lock handle the concurrency on the group structure 
	
	/*
	 * Constructor of the GroupController, it creates the first
	 * GroupKey with DES and populate the table with the KEK of every bit.
	 * 
	 * The table will be something like this:
	 * ------
	 * 00 K00 ( KeK for the value 0 of the bit 0 )
	 * 01 K01 ( Kek for the value 0 of the bit 1 )
	 * 02 K02 ( Kek for the value 0 of the bit 2 )
	 * 10 K10
	 * 11 K11
	 * 12 K12
	 * ------
	 * 
	 * Obviously with the convention of the bits like this:
	 * 
	 * id    bit2  bit1  bit0
	 * 5   =  1     0     1 
	 * 
	 * */
	public GroupController(){
				
		group = new HashMap <String,NetInfoGroupMember>();
		table = new HashMap<String,SecretKey>();
		lock=0;
		
		KeyGenerator keyGen=null;
		
		try {
			keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56); //key of 56 bits 
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error no such algorithm for DES");
			e.printStackTrace();
		}
		SecretKey key = null;
		String tableIndex="";
		
		dek = keyGen.generateKey(); //Generate the group key 
		
		//Initialize the centralized flat table with the keys
		for(int i=0;i<2;i++)
			for(int j=0;j<3;j++){
				key = keyGen.generateKey();
				tableIndex = ""+i+""+j; //this is for example 00 01 02 10 11 12 ( first is the KEK for the bit
									    //0 of the first bit of the key...
				table.put(tableIndex, key);
			}	
	}
	
	
	public void run(){
		
		try {
			startServer();
		} catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong in the server startup");
			e.printStackTrace();
		}
		
		while(true){
			
			Socket clientSocket = null;
			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
			
			  try {
				  clientSocket = mySocket.accept(); //wait untill a client connect 				  
			} catch (IOException e) {
				System.out.println("An error during the accept has occured");
				e.printStackTrace();
			}
			  
			   
			try {
				ois = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} 
		    try {
				oos = new ObjectOutputStream(clientSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		    Message m=null;
		    
			try {
				m = (Message)ois.readObject();
			} catch (ClassNotFoundException | IOException e) {	
				e.printStackTrace();
			}
		    
		    if(m.getClass().isInstance(BootMessage.class)){
		    	//TODO somebody is trying to enter the group 
		    }
		    
		    else //is an ActionMessage ( leave, getGroup, common )
		    	if(m.getClass().isInstance(ActionMessage.class)){
		    		
		    		ActionMessage am = (ActionMessage)m;
		    		Cipher c=null;
		    		
		    		try {
		    			c = Cipher.getInstance("DES");
		    		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
		    			// TODO Auto-generated catch block
		    			e.printStackTrace();
		    		}
		    		
		    		try {
		    			c.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
		    			} catch (InvalidKeyException e) {
		    				// TODO Auto-generated catch block
		    				e.printStackTrace();
		    			}
		    		
		    		String nodeId = "";
		    		String action="";
		    		
		    		 try {
		    				byte[] decryptedId = c.doFinal(am.getnodeId()); //decrypting the message  
		    				nodeId = new String(decryptedId);
		    				
		    				byte[] decryptedAction = c.doFinal(am.getAction()); //decrypting the message  
		    				action = new String(decryptedAction);
		    				
		    			} catch (IllegalBlockSizeException | BadPaddingException e) {
		    				System.out.println("Something went wrong during decryption of text");
		    				e.printStackTrace();
		    			}
		    		 
		    		 switch(action){
		    		 
		    		 case "leave": //Handle leaving member
		    		 case "getGroup": //handle get of the current view of the group
		    		 case "common": //a common text message 
		    		 
		    		 }
		    		
		    		
		    	}
		    	
		}
			
	}
	
	


	/*
	 * This method start the GroupController, it wait n° MemberGroup and perform the first
	 * handshake with them
	 * */
	private void startServer() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		int cont=2; //wait untill all 8 clients connect to server 
		
		mySocket=null;
		Socket clientSocket=null;
		try {
			mySocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("Error while listen on" +port);
			e.printStackTrace();
		}
		
		System.out.println("Now listening for the node of the group...");
		
		while(cont!=0){ //wait for 8 clients
	    try {
			  clientSocket = mySocket.accept(); //wait untill a client connect 
			  cont--;
			  System.out.println("A node joined! missing "+cont);
			  
		} catch (IOException e) {
			System.out.println("An error during the accept has occured");
			e.printStackTrace();
		}
	    
	    //------------------------------------------------------
	    //HANDSHAKE WITH THE GROUP MEMBER:
		//get its publicKey and send them the group key encrypted 
		//------------------------------------------------------	
	    
	    ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream()); 
		ObjectOutputStream oos = new ObjectOutputStream(clientSocket.getOutputStream());

		BootMessage bootMessage = (BootMessage)ois.readObject();
		
		group.put(""+bootMessage.getId(),bootMessage.getNigm());

				
		Cipher c = Cipher.getInstance("RSA");
		c.init(Cipher.ENCRYPT_MODE, bootMessage.getPublicKey());
		
		StartConfigMessage scm = new StartConfigMessage();
		
		 //TO TEST IF PUBLIC KEY WORKS
		 //byte[] raw = c.doFinal("prova".getBytes());
		 //scm.setTest(raw);
		
		// ----------------------------------------
		// send the encrypted KEY GROUP(DeK) to the node 
		// ----------------------------------------
		System.out.println("Sending the encrypted DEK to the node "+bootMessage.getId());
	    
	    byte [] raw = c.doFinal(dek.getEncoded());
	    scm.setDeK(raw);
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK0 to the node 
		// ----------------------------------------
		System.out.println("Sending the encrypted KEK0 to the node "+bootMessage.getId());
	    String binaryId = bootMessage.getId();
	    //System.out.println("binary id is " +binaryId);
	    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
	    
	    raw = c.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK0(raw);
	    // ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK1 to the node 
		// ----------------------------------------

		System.out.println("Sending the encrypted KEK1 to the node "+bootMessage.getId());
	    binaryId = bootMessage.getId();
	    mapKey = ""+binaryId.charAt(1)+"1";
	    
	    raw = c.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK1(raw);
	    
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK2 to the node 
		// ----------------------------------------
	    
		System.out.println("Sending the encrypted KEK2 to the node "+bootMessage.getId());
	    binaryId = bootMessage.getId();
	    mapKey = binaryId.charAt(0)+"2";
	    
	    raw = c.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK2(raw);
	    
		// ----------------------------------------
	    
	    oos.writeObject(scm);
	    
   } //handle the handshake for all the pre-configured clients ( 8 times for this demo )
		
	System.out.println("[INFO] Correctly configured all the members of the group");
	
	for(NetInfoGroupMember nigm : group.values()){
		System.out.println("member ip: "+nigm.getIpAddress()+" port: "+nigm.getPort());
	}
		  
	} // end StartServer
	
	
	
	public synchronized void HandleLeavingMember(){
		
		/*
		 * If a lock is ON that means I must wait to leave. ( somebody is sending message and I am in the group view )
		 * */
		while(lock==1){ 
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		lock=1;
		
		//TODO HANDLE HERE THE LEAVING MEMBER ( SEE DOCUMENT IN ORDER TO UNDERSTAND WHAT DO )
		
		
	}
	
	/*
	 * This method returns the current viewGroup and lock all every possible modify
	 * on it.
	 * */
	public synchronized HashMap <String,NetInfoGroupMember> GetGroup(){
		
		while(lock==1){
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		lock=1;
		return group;
		
	}
}
