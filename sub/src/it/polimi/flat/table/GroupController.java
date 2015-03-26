package it.polimi.flat.table;

import it.polimi.flat.table.support.ActionMessage;
import it.polimi.flat.table.support.BootMessage;
import it.polimi.flat.table.support.CommMessage;
import it.polimi.flat.table.support.CrashReportMessage;
import it.polimi.flat.table.support.Message;
import it.polimi.flat.table.support.NetInfoGroupMember;
import it.polimi.flat.table.support.NewDekMessage;
import it.polimi.flat.table.support.NewKekMessage;
import it.polimi.flat.table.support.StartConfigMessage;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*
 * This is the class for the group controller
 * of the centralized flat table.
 * It handle all the join and leaving of 
 * the group member.
 * 
 * TODO Handle the changing of the key also when a member crashes 
 * */
public class GroupController {
	
	private static final int GROUP_MEMBER_NUM = 8;
		
	private Integer port=56520;
	private ServerSocket mySocket;
	
	private Cipher RsaCipher; //the RsaCipher used to encrypt the DEK on Boot of new group members
	private Cipher DesCipher; //the DES cipher used to encrypt all the message for the group member once they join
	
	private SecretKey dek; //the DEK of the group
	private SecretKey oldDek; //oldDek 

	private HashMap <String,SecretKey> table; //hashmap that store the flatTable 
	
	private HashMap <String,NetInfoGroupMember> group; //table to keep track of the 'sockets' of the member in the group, this will be passed in response to a GetGroup request from members
	
	private Integer DynLock; //this lock handle the concurrency AddMember and LeavingMember on the group structure 
	private Integer BroadcastLock;
	private CommMessage backSecurityCheck;
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
		DynLock=0;
		BroadcastLock=0;
		
		KeyGenerator keyGen=null;
		
		try {
			keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56); //key of 56 bits 
		} catch (NoSuchAlgorithmException e) {
			System.out.println("[ERROR]Error no such algorithm for DES");
			e.printStackTrace();
		}
		SecretKey key = null;
		String tableIndex="";
		
		dek = keyGen.generateKey(); //Generate the group key 
		
		try {
			DesCipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
	
		
		try {
			RsaCipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}

		
		//Initialize the centralized flat table with the keys
		for(int i=0;i<2;i++)
			for(int j=0;j<3;j++){
				key = keyGen.generateKey();
				tableIndex = ""+i+""+j; //this is for example 00 01 02 10 11 12 ( first is the KEK for the bit
									    //0 of the first bit of the key...
				table.put(tableIndex, key);
			}
		
		createBackSecurityMessageCheck();
	}
	
	
	public void run(){
		
		try {
			startServer();
		} catch (IOException | ClassNotFoundException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("[ERROR]Something went wrong in the server startup");
			e.printStackTrace();
		}
		
		while(true){
			
			Socket clientSocket = null;
			ObjectInputStream ois = null;
			ObjectOutputStream oos = null;
		    Message m=null;
			
			  try {
				  clientSocket = mySocket.accept(); //wait untill a client connect 				  
			} catch (IOException e) {
				System.out.println("[ERROR]An error during the accept has occured");
				e.printStackTrace();
			}
			  
			//System.out.println("received a connection"); 
			   
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
		     
			try {
				m = (Message)ois.readObject();
			} catch (ClassNotFoundException | IOException e) {	
				e.printStackTrace();
			}
			
			//System.out.println("received a message from " + clientSocket.getPort());

		    
		    if(m.getClass().getSimpleName().equals("BootMessage")){
		    	//handle join of a new member
		    	System.out.println("[INFO]A boot message has been received");
		    	BootMessage bm = (BootMessage)m;
		    	HandleAddMember(bm,oos);
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
			    			//System.out.println(" before handling leave member : BL= "+this.BroadcastLock); 

		    			this.HandleLeavingMember(am);
		    			
		    			//System.out.println(" after handling leave member : BL= "+this.BroadcastLock); 

		    		 };break;
		    		 
		    		 case "getGroup": {
		    			 
		    			 //System.out.println("in getGroup");
		    			 
		    			 HashMap <String,NetInfoGroupMember> group = this.GetGroup();
		    			 try {
							oos.writeObject(group); //send in plain the group structure 
						} catch (IOException e) {
							e.printStackTrace();
						}
		    			 
		    		 };break;
		    		 
		    		 case "broadcastdone":{ //signal a broadcastdone and decrement BroadcastLock
		    			 
		    			 this.BroadcastLock--; // remove a broadcastlock 
		    			 //System.out.println("BroadcastLock is " + BroadcastLock);
		    		 };break;
		    		 
		    		 case "crashreport":{    			 
		    			 CrashReportMessage crm = (CrashReportMessage)am;
		    			 ArrayList <NetInfoGroupMember> crashedMember = crm.getCrashedMembers();
		    			 System.out.println("[INFO]Received a crash report from a member");
		    			 this.HandleCrashedMembers(crashedMember);	
		    			 System.out.println("Ended crash report\n Now group is: \n");
		    			 
		    			 for(NetInfoGroupMember nigm : group.values()){
		    				 
		    				 System.out.println("IP: "+nigm.getIpAddress()+ "PORT:  " +nigm.getPort() + "\n");
		    				 
		    			 }
		    			 
		    		 };break;
		    		 
		    	 }//end switch   			
		    } //end ActionMessage if
		    try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       }//end while(true)		
          }
	
	
	/**
	 * genera la chiave di gruppo
	 */
	private void dekGeneration(){
		
		KeyGenerator keyGen=null;
		
		try {
			keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56); //key of 56 bits 
		} catch (NoSuchAlgorithmException e) {
			System.out.println("[ERROR]Error no such algorithm for DES");
			e.printStackTrace();
		}
		//salviamo la dek come oldDek 
		this.oldDek = this.dek; 
		
		this.dek = keyGen.generateKey(); 
		
		//System.out.println("In DeK generation : BL= "+this.BroadcastLock); 
	}
	
	
	/*
	 * This method is exploited in order to remove
	 * the member from the view group that are reported 
	 * crashed after a broadcast from another member.
	 * If two members report the crash of the same member this
	 * method is called two times, but the second times this for will
	 * not do anything.
	 * */
	private void HandleCrashedMembers(ArrayList<NetInfoGroupMember> crashedMember) {
		
		//System.out.println("In handle crash, the size of crashed member is " + crashedMember.size());
		ArrayList <String> toDelete = new ArrayList <String>();
		ArrayList <NetInfoGroupMember> toDelete2 = new ArrayList <NetInfoGroupMember>();

		this.BroadcastLock--; // after a report crash a broadcast is surely finished 
		DynLock=1; //block potentially other GetGroup for a moment! 
		
		/*
		for(NetInfoGroupMember nigm : crashedMember){
			System.out.println("the info of the crash" + nigm.getIpAddress() + "  "  +nigm.getPort());
			
			for(NetInfoGroupMember nigm2 : this.group.values()){
				if(nigm2.getPort().equals(nigm.getPort()) && nigm2.getIpAddress().equals(nigm.getIpAddress())){
					toDelete.add(nigm2);
				}
			}						
		}
		
		for(NetInfoGroupMember nigm3 : toDelete){
			this.group.values().remove(nigm3);
		}
		
		for(NetInfoGroupMember nigm2 : this.group.values()){
			System.out.println(" member " +nigm2.getIpAddress() + " " + nigm2.getPort());
			
		}
		*/
		//ricaviamo gli id dei membri crashati
		for(NetInfoGroupMember nigm : crashedMember){
			System.out.println("the info of the crash" + nigm.getIpAddress() + "  "  +nigm.getPort());
			
			for (Map.Entry<String,NetInfoGroupMember> entry : this.group.entrySet()){
				if(entry.getValue().getPort().equals(nigm.getPort()) && entry.getValue().getIpAddress().equals(nigm.getIpAddress())){
					//System.out.println("id of the crashed memeber: " +  entry.getKey());
					//this.group.values().remove(entry.getValue());
					toDelete.add(entry.getKey());
				}
			}						
		}
		
		for(NetInfoGroupMember nigm : crashedMember){
			//System.out.println("the info of the crash" + nigm.getIpAddress() + "  "  +nigm.getPort());
			
			for(NetInfoGroupMember nigm2 : this.group.values()){
				if(nigm2.getPort().equals(nigm.getPort()) && nigm2.getIpAddress().equals(nigm.getIpAddress())){
					toDelete2.add(nigm2);
				}
			}						
		}
		
		for(NetInfoGroupMember nigm3 : toDelete2){
			this.group.values().remove(nigm3);
		}
		
		DynLock=0;
		
		//rimuoviamo i membri 
		for (String id : toDelete) {
			this.HandleLeavingMember(id);
		}		
	}


	/*
	 * This method start the GroupController, it wait  MemberGroup and perform the first
	 * handshake with them
	 * */
	private void startServer() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		int cont=3; //wait untill all 8 clients connect to server 
		
		mySocket=null;
		Socket clientSocket=null;
		byte[] raw;
		
		try {
			mySocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out.println("[ERROR]Error while listen on" +port);
			e.printStackTrace();
		}
		
		System.out.println("[INFO]Now listening for the node of the group...");
		
		while(cont!=0){ //wait for 8 clients
	    try {
			  clientSocket = mySocket.accept(); //wait untill a client connect 
			  cont--;
			  System.out.println("[INFO]A node joined! missing "+cont);
			  
		} catch (IOException e) {
			System.out.println("[ERROR]An error during the accept has occured");
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

				
		RsaCipher.init(Cipher.ENCRYPT_MODE, bootMessage.getPublicKey());
		
		StartConfigMessage scm = new StartConfigMessage();
		
		 //TO TEST IF PUBLIC KEY WORKS
		 //raw = RsaCipher.doFinal(("prova"+bootMessage.getId()).getBytes());
		 //scm.setTest(raw);
		
		// ----------------------------------------
		// send the encrypted KEY GROUP(DeK) to the node 
		// ----------------------------------------
		System.out.println("[INFO]Sending the encrypted DEK to the node "+bootMessage.getId());
	    
	    raw = RsaCipher.doFinal(dek.getEncoded());
	    scm.setDeK(raw);
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK0 to the node 
		// ----------------------------------------
		System.out.println("[INFO]Sending the encrypted KEK0 to the node "+bootMessage.getId());
	    String binaryId = bootMessage.getId();
	    //System.out.println("binary id is " +binaryId);
	    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
	    //System.out.println("map key is " + mapKey);
	    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK0(raw);
	    // ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK1 to the node 
		// ----------------------------------------

		System.out.println("[INFO]Sending the encrypted KEK1 to the node "+bootMessage.getId());
	    binaryId = bootMessage.getId();
	    mapKey = ""+binaryId.charAt(1)+"1";
	    
	    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK1(raw);
	    
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK2 to the node 
		// ----------------------------------------
	    
		System.out.println("[INFO]Sending the encrypted KEK2 to the node "+bootMessage.getId());
	    binaryId = bootMessage.getId();
	    mapKey = binaryId.charAt(0)+"2";
	    
	    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK2(raw);
	    
		// ----------------------------------------
	    
	    oos.writeObject(scm);
	    
	    clientSocket.close();
	    
   } //handle the handshake for all the pre-configured clients ( 8 times for this demo )
		
	System.out.println("[INFO] Correctly configured all the members of the group");
	
	for(NetInfoGroupMember nigm : group.values()){
		System.out.println("member ip: "+nigm.getIpAddress()+" port: "+nigm.getPort());
	}
	
	ActionMessage am = new ActionMessage();
	
	DesCipher.init(Cipher.ENCRYPT_MODE, dek);
	
	am.setnodeId(DesCipher.doFinal("-1".getBytes()));
	am.setAction(DesCipher.doFinal("start".getBytes()));
	
	System.out.println("[INFO]Sending 'start' message to members of the group:\n----------------\n");
	
	for(NetInfoGroupMember nigm : group.values()){
		System.out.println("IP: "+ nigm.getIpAddress() + " PORT: " + nigm.getPort());
	}
	
	for(NetInfoGroupMember nigm : group.values()){
		Socket s = new Socket(nigm.getIpAddress(),nigm.getPort());
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(am);
		s.close();
	}
	
	} // end StartServer
	
	
	
	public synchronized void HandleAddMember(BootMessage msg,ObjectOutputStream oos){
		
		/*
		 * If a lock is ON that means I must wait to enter. ( somebody is sending message and I am not, or somebody is leaving in the group view )
		 * */
		while(BroadcastLock!=0 || DynLock==1 || group.keySet().size() >= GROUP_MEMBER_NUM){ //sono in corso dei broadcast nel gruppo o in corso un leaving
			System.out.println(" BL = "+BroadcastLock + " and DL = " + DynLock);

			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		DynLock=1;
		//Add new member to the group 
		group.put(""+msg.getId(),msg.getNigm());
		changeKeys(msg);
		sendNewKeys(msg,oos);
		sendStartMessage();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendCheckMessage(""+msg.getId());
		createBackSecurityMessageCheck();
		//reset old dek used to communicate new keys
		oldDek = null;
		
		DynLock=0;//REMEMBER!
			
	}
	
	/**
	 * change keys after join of a new user
	 * @param msg - bootmessage that contains information to use to encrypt new keys
	 */
	private void changeKeys(BootMessage msg){
		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, dek);
			oldDek = dek;
			dek = new SecretKeySpec(DesCipher.doFinal(dek.getEncoded()),0,dek.getEncoded().length, "DES");
			
			
		    String binaryId = msg.getId();

			
			// ----------------------------------------
			// change the encrypted KEK0 referenced byt the new participant
			//	signing it with the previous value.
			// ----------------------------------------

			System.out.println("[INFO]Change kek0 for new entry "+msg.getId());
		    //System.out.println("binary id is " +binaryId);
		    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
		    //System.out.println("map key is " + mapKey);
		    DesCipher.init(Cipher.ENCRYPT_MODE, table.get(mapKey));
			table.put(mapKey, new SecretKeySpec(DesCipher.doFinal(table.get(mapKey).getEncoded()),0,table.get(mapKey).getEncoded().length, "DES"));
			
			// ----------------------------------------
			// change the encrypted KEK1 referenced by the new participant
			//	signing it with the previous value.
			// ----------------------------------------

			System.out.println("[INFO]Change kek1 for new entry "+msg.getId());
		    mapKey = ""+binaryId.charAt(1)+"1";
		    //System.out.println("map key is " + mapKey);
		    DesCipher.init(Cipher.ENCRYPT_MODE, table.get(mapKey));
			table.put(mapKey, new SecretKeySpec(DesCipher.doFinal(table.get(mapKey).getEncoded()),0,table.get(mapKey).getEncoded().length, "DES"));
			
			// ----------------------------------------
			// change the encrypted KEK2 referenced by the new participant
			//	signing it with the previous value.
			// ----------------------------------------

			System.out.println("[INFO]Change kek2 for new entry "+msg.getId());
		    mapKey = binaryId.charAt(0)+"2";
		    //System.out.println("map key is " + mapKey);
		    DesCipher.init(Cipher.ENCRYPT_MODE, table.get(mapKey));
			table.put(mapKey, new SecretKeySpec(DesCipher.doFinal(table.get(mapKey).getEncoded()),0,table.get(mapKey).getEncoded().length, "DES"));

			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Send dek and keys to new member(encrypted with his publickey)
	 * and to old member(encrypted with old dek)
	 * @param msg - bootmessage with information of new member
	 * @param oos - ObjectOutputStream where write startconfigurationMessage for new entry
	 */
	private void sendNewKeys(BootMessage msg,ObjectOutputStream oos){
		
		try {
	    	//send to new group member with his public key
			byte[] raw;
			//------------------------------------------------------
		    //HANDSHAKE WITH THE GROUP MEMBER:
			//get its publicKey and send them the group key encrypted 
			//------------------------------------------------------	
			System.out.println("ADRESS NEW MEMBER "+msg.getId() + " "+group.get(msg.getId()).getIpAddress()+" "+group.get(msg.getId()).getPort());

							
			RsaCipher.init(Cipher.ENCRYPT_MODE, msg.getPublicKey());
			
			StartConfigMessage scm = new StartConfigMessage();
		
			// ----------------------------------------
			// send the encrypted KEY GROUP(DeK) to the node 
			// ----------------------------------------
			System.out.println("[INFO]Sending the encrypted DEK to the node "+msg.getId());
		    
		    raw = RsaCipher.doFinal(dek.getEncoded());
		    scm.setDeK(raw);
			// ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK0 to the node 
			// ----------------------------------------
			System.out.println("[INFO]Sending the encrypted KEK0 to the node "+msg.getId());
		    String binaryId = msg.getId();
		    System.out.println("binary id is " +binaryId);
		    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
		    System.out.println("map key is " + mapKey);
		    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
		    scm.setKeK0(raw);
		    // ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK1 to the node 
			// ----------------------------------------

			System.out.println("[INFO]Sending the encrypted KEK1 to the node "+msg.getId());
		    binaryId = msg.getId();
		    mapKey = ""+binaryId.charAt(1)+"1";
		    
		    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
		    scm.setKeK1(raw);
		    
			// ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK2 to the node 
			// ----------------------------------------
		    
			System.out.println("[INFO]Sending the encrypted KEK2 to the node "+msg.getId());
		    binaryId = msg.getId();
		    mapKey = binaryId.charAt(0)+"2";
		    
			raw = RsaCipher.doFinal(table.get(mapKey).getEncoded());
			scm.setKeK2(raw);
			    
			// ----------------------------------------
		    //System.out.println("SEND MESSAGE");
		    oos.writeObject(scm);
		    
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		//send keks and dek in broadcast to old group
		for(String idMember : group.keySet()){
			if(!idMember.equals(""+msg.getId())){
				//System.out.println("UPDATE KEYS "+idMember);
				sendConfiguration(idMember);
			}
		}
		
	 }
	/**
	 * Send start message to every group member after change dek 
	 */
	private void sendStartMessage(){
		ActionMessage am = new ActionMessage();
		
		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, dek);
			am.setnodeId(DesCipher.doFinal("-1".getBytes()));
			am.setAction(DesCipher.doFinal("start".getBytes()));
			
			System.out.println("[INFO]Sending 'start' message to members of the group");
			
			for(NetInfoGroupMember nigm : group.values()){
				
				System.out.println("IP: " + nigm.getIpAddress() + " PORT:  " + nigm.getPort());
				
				Socket s = new Socket(nigm.getIpAddress(),nigm.getPort());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(am);
				s.close();
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	/**
	 * Send new configuration after view change to older member group
	 * @param id
	 */
	private void sendConfiguration(String id){
		StartConfigMessage scm = new StartConfigMessage();
		byte[] raw;
		
	    try {
	    
			DesCipher.init(Cipher.ENCRYPT_MODE, oldDek);

			// ----------------------------------------
			// send the encrypted KEY GROUP(DeK) to the node 
			// ----------------------------------------
			System.out.println("[INFO]Sending the encrypted DEK to the node "+id);
			raw = DesCipher.doFinal(dek.getEncoded());
			
			scm.setDeK(raw);
			// ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK0 to the node 
			// ----------------------------------------
			System.out.println("[INFO]Sending the encrypted KEK0 to the node "+id);
		    String binaryId = id;
		    //System.out.println("binary id is " +binaryId);
		    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
		    //System.out.println("map key is " + mapKey);
		    raw = DesCipher.doFinal(table.get(mapKey).getEncoded()); 
		    scm.setKeK0(raw);
		    // ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK1 to the node 
			// ----------------------------------------

			System.out.println("[INFO]Sending the encrypted KEK1 to the node "+id);
		    binaryId = id;
		    mapKey = ""+binaryId.charAt(1)+"1";
		    
		    raw = DesCipher.doFinal(table.get(mapKey).getEncoded()); 
		    scm.setKeK1(raw);
		    
			// ----------------------------------------
		    
			// ----------------------------------------
			// send the encrypted KEK2 to the node 
			// ----------------------------------------
		    
			System.out.println("[INFO]Sending the encrypted KEK2 to the node "+id);
		    binaryId = id;
		    mapKey = binaryId.charAt(0)+"2";
		    raw = DesCipher.doFinal(table.get(mapKey).getEncoded());
		    
		    scm.setKeK2(raw);
		    
			// ----------------------------------------
		    Socket s = new Socket(group.get(id).getIpAddress(),group.get(id).getPort());
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

		    oos.writeObject(scm);
		    
		    s.close();
		} catch (IllegalBlockSizeException | BadPaddingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	    
	 
	    
	}
	
	
	public synchronized void HandleLeavingMember(ActionMessage am){
		
		/*
		 * If a lock is ON that means I must wait to leave. ( somebody is sending message and I am in the group view )
		 * */
		while(BroadcastLock!=0 || DynLock==1){ 
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//prendiamo il lock
		DynLock=1;
		String nodeId="";
				
		try {
			 
			byte[] decryptedId = DesCipher.doFinal(am.getnodeId()); //decrypting the message  
		    nodeId = new String(decryptedId);
			 
			System.out.println("[INFO]The member " +  nodeId  + " wants to leave the group ");
			
			//creiamo la nuova dek (K')
			this.dekGeneration();
			//costruiamo il messaggio da mandare in broadcast
			ArrayList<byte[]> dekEncrypted = this.encryptNewDek(nodeId);
			//mandiamo la nuova chiave a tutti
			this.sendNewDekMessage(dekEncrypted);
			//cambiamo le KEK del nodo che ha fatto leave
			ArrayList<byte[]> kekEncrypted = this.changeKek(nodeId);
			//mandiamo le nuove kek
			this.sendNewKekMessage(kekEncrypted);
			
		} catch (IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//rilasciamo il lock
		DynLock=0;
		
	}
	
public synchronized void HandleLeavingMember(String nodeId){
		
		
		//prendiamo il lock
		DynLock=1;
				
		try {
			 
			 
			System.out.println("[INFO]The member " +  nodeId  + " wants to leave the group ");
			//creiamo la nuova dek (K')
			this.dekGeneration();
			//costruiamo il messaggio da mandare in broadcast
			ArrayList<byte[]> dekEncrypted = this.encryptNewDek(nodeId);
			//mandiamo la nuova chiave a tutti
			this.sendNewDekMessage(dekEncrypted);
			//cambiamo le KEK del nodo che ha fatto leave
			ArrayList<byte[]> kekEncrypted = this.changeKek(nodeId);
			//mandiamo le nuove kek
			this.sendNewKekMessage(kekEncrypted);
			
		} catch (IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//rilasciamo il lock
		DynLock=0;
		
	}
	

	/**
	 * identifica le chiavi del odo uscente e cripta la nuova dek saltandp queste chiavi
	 * ritorna la listadi messaggi criptati contanente la dek da mandare in broadcast
	 * @param nodeId
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private ArrayList<byte[]> encryptNewDek(String nodeId) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		
		//es: nodeId = 001
		//devo togliere le chiavi 02, 01, 10
		//generiamo un array con le chiavi da evitare con questo trick
		ArrayList<String> keysLeaveMemberArrayList = new ArrayList<String>();
		//prendiamo gli id
		keysLeaveMemberArrayList = this.getKekId(nodeId);		
		Cipher cipher = Cipher.getInstance("DES");
		ArrayList<byte[]> dekEncrypted = new ArrayList<byte[]>();
		//scorriamo le tuple della tabella
		for (Map.Entry<String,SecretKey> entry : this.table.entrySet()){
			//se l entry appartiene ad una chiave del nodo uscente allora non criptiamola
			if(keysLeaveMemberArrayList.contains(entry.getKey())){
				//System.out.println("trovata chiave " + entry.getKey());
				continue;
			}
			else{
				//prendiamo la chiave del nodo valido considerato
				cipher.init(Cipher.ENCRYPT_MODE, entry.getValue());
				//criptiamo la nuova dek e mettiamola nella lista da mandare in broadcast
				dekEncrypted.add(cipher.doFinal(this.dek.getEncoded()));
			}
			
		}
		
		return dekEncrypted;
		
	}
	
	private void sendNewDekMessage(ArrayList<byte[]> newDekList){
		NewDekMessage ndm = new NewDekMessage();
		
		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, dek);
			ndm.setnewDekList(newDekList);
			
			//System.out.println("Sending newdekmessage....");
			
			for(NetInfoGroupMember nigm : group.values()){
				Socket s = new Socket(nigm.getIpAddress(),nigm.getPort());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(ndm);
				s.close();
			}
		} catch (InvalidKeyException e) {
		} catch (IOException e) {
	  }
	}
	
	private void sendNewKekMessage(ArrayList<byte[]> newKekList){
		NewKekMessage ndm = new NewKekMessage();
		
		try {
			ndm.setnewKekList(newKekList);
			
			//System.out.println("Sending newkekmessage....");
			
			for(NetInfoGroupMember nigm : group.values()){
				Socket s = new Socket(nigm.getIpAddress(),nigm.getPort());
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(ndm);
				s.close();
			}
		}catch (Exception e) {
		}		
	}
	
	/**
	 * genera le nuove chiavi kek e le cripta per essere spedite in broadcast
	 * @param nodeId
	 * @return
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	private ArrayList<byte[]> changeKek(String nodeId) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
		//inizializziamo le librerie di cripto
		KeyGenerator keyGen=null;
		try {
			keyGen = KeyGenerator.getInstance("DES");
			keyGen.init(56); //key of 56 bits 
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error no such algorithm for DES");
			e.printStackTrace();
		}	
		//array delle chiavi nuove da mandare in broadcast
		ArrayList<byte[]> kekEncrypted = new ArrayList<byte[]>();
		//array degli id delle chiavi da cambiare
		ArrayList<String> keysLeaveMemberArrayList = new ArrayList<String>();
		//prendiamo gli id
		keysLeaveMemberArrayList = this.getKekId(nodeId);
		//cambiamo le chiavi per ogni id
		for (String mapKey : keysLeaveMemberArrayList) {
			//creiamo la nuova kek
			SecretKey newKek = keyGen.generateKey();
			//criptiamola con la DEK
			DesCipher.init(Cipher.ENCRYPT_MODE, dek);
			byte[] desCriptedKek = DesCipher.doFinal(table.get(mapKey).getEncoded());
			//criptiamola con la vecchia KEK
			DesCipher.init(Cipher.ENCRYPT_MODE, table.get(mapKey));
			byte[] kekCriptedKek = DesCipher.doFinal(desCriptedKek);
			//salviamo la nuova KEK nella tabella
			table.put(mapKey, newKek);	
			//aggiungiamo la chiave a quelle da mandare in broadcast
			kekEncrypted.add(kekCriptedKek);
		}
		
		return kekEncrypted;
	}
	
	
	/**
	 * ritorna gli id nella tabella delle ciavi a seconda del nodeId Passato
	 * es: nodeId = 001
	 * ritorna 02, 01, 10
	 * @param nodeId
	 * @return
	 */
	private ArrayList<String> getKekId(String nodeId){
		//es: nodeId = 001
		//devo togliere le chiavi 02, 01, 10
		//generiamo un array con le chiavi da evitare con questo trick
		ArrayList<String> keysLeaveMemberArrayList = new ArrayList<String>();
		//reversiamo la stringa cosi da avere la convenzione dei bit giusta
		String reverseNodeId = new StringBuffer(nodeId).reverse().toString();
		//scorriamo ogni carattere di node id
		for (int i = 0;i < reverseNodeId.length(); i++){
		   //prima parte di chiave (valoreBit - posizione come spiegato nella struttura della tabella) 
		   String strI = "" + i;
		   //seconda parte di chiave e aggiungo all array list
		   keysLeaveMemberArrayList.add(reverseNodeId.charAt(i) + strI);
		}
		return keysLeaveMemberArrayList;
	}
	
	/*
	 * This method returns the current viewGroup and lock all every possible modify
	 * on it.
	 * Thanks to the two different lock, two getGroup request don't block each other,
	 * the lock are only for the functions that want modify the group structure.
	 * */
	public synchronized HashMap <String,NetInfoGroupMember> GetGroup(){
		
		while(DynLock==1){ // ci sono in corso operazioni che stanno modificando il gruppo
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		BroadcastLock++;
		//System.out.println("BroadcastLock is " + BroadcastLock);
		return group; //return the group view 
		
	}
	
	//Once a member send a broadcast to all the group, remember to call this
	//method
	public void SignalBroadcastDone(){
		BroadcastLock--;
	}
	
	private void createBackSecurityMessageCheck(){
		backSecurityCheck = new CommMessage();
		
		
		backSecurityCheck.setIdSender("-1");

		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, this.dek);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}		
	    try {
			byte[] encryptedText = DesCipher.doFinal("Check back security".getBytes());
			backSecurityCheck.setText(encryptedText);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send common message to new entry in order to check back security
	 * @param nodeId - node id of new entry
	 */
	private void sendCheckMessage(String nodeId){
		
		Socket newsocket;
		try {
			newsocket = new Socket(group.get(nodeId).getIpAddress(),group.get(nodeId).getPort());
			ObjectOutputStream ooss = new ObjectOutputStream(newsocket.getOutputStream());
			
			ooss.writeObject(backSecurityCheck);
			newsocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
