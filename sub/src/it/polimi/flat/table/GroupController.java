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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
	
	private Cipher RsaCipher; //the RsaCipher used to encrypt the DEK on Boot of new group members
	private Cipher DesCipher; //the DES cipher used to encrypt all the message for the group member once they join
	
	private SecretKey dek; //the DEK of the group
	private HashMap <String,SecretKey> table; //hashmap that store the flatTable 
	
	private HashMap <String,NetInfoGroupMember> group; //table to keep track of the 'sockets' of the member in the group, this will be passed in response to a GetGroup request from members
	
	private Integer DynLock; //this lock handle the concurrency AddMember and LeavingMember on the group structure 
	private Integer BroadcastLock; 
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
			System.out.println("Error no such algorithm for DES");
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
		    Message m=null;
			
			  try {
				  clientSocket = mySocket.accept(); //wait untill a client connect 				  
			} catch (IOException e) {
				System.out.println("An error during the accept has occured");
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
			
			//System.out.println(m.getClass());

		    
		    if(m.getClass().getSimpleName().equals("BootMessage")){
		    	//TODO HANDLE ADD OF A GROUP MEMBER 
		    }
		    
		    else //is an ActionMessage ( leave, getGroup, common )
		    	if(m.getClass().getSimpleName().equals("ActionMessage")){
		    		ActionMessage am = (ActionMessage)m;
		    		
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
		    				System.out.println("action is " + action);
		    				
		    			} catch (IllegalBlockSizeException | BadPaddingException e) {
		    				System.out.println("Something went wrong during decryption of text");
		    				e.printStackTrace();
		    			}
		    		 
		    		 switch(action){
		    		 
		    		 case "leave": //Handle leaving member
		    		 case "getGroup": {
		    			 
		    			 HashMap <String,NetInfoGroupMember> group = this.GetGroup();
		    			 try {
							oos.writeObject(group); //send in plain the group structure 
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    			 
		    		 };break;
		    		 
		    		 case "common": //a common text message 
		    		 case "broadcastdone": //signal a broadcastdone and decrement BroadcastLock
		    		 
		    		 }
		    		
		    		
		    	}
		    	
		}
			
	}
	
	
	/*
	 * This method start the GroupController, it wait n° MemberGroup and perform the first
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

				
		RsaCipher.init(Cipher.ENCRYPT_MODE, bootMessage.getPublicKey());
		
		StartConfigMessage scm = new StartConfigMessage();
		
		 //TO TEST IF PUBLIC KEY WORKS
		 raw = RsaCipher.doFinal(("prova"+bootMessage.getId()).getBytes());
		 scm.setTest(raw);
		
		// ----------------------------------------
		// send the encrypted KEY GROUP(DeK) to the node 
		// ----------------------------------------
		System.out.println("Sending the encrypted DEK to the node "+bootMessage.getId());
	    
	    raw = RsaCipher.doFinal(dek.getEncoded());
	    scm.setDeK(raw);
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK0 to the node 
		// ----------------------------------------
		System.out.println("Sending the encrypted KEK0 to the node "+bootMessage.getId());
	    String binaryId = bootMessage.getId();
	    System.out.println("binary id is " +binaryId);
	    String mapKey = ""+binaryId.charAt(2)+"0"; //charAt(2) is the 0s bit of the Id
	    System.out.println("map key is " + mapKey);
	    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK0(raw);
	    // ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK1 to the node 
		// ----------------------------------------

		System.out.println("Sending the encrypted KEK1 to the node "+bootMessage.getId());
	    binaryId = bootMessage.getId();
	    mapKey = ""+binaryId.charAt(1)+"1";
	    
	    raw = RsaCipher.doFinal(table.get(mapKey).getEncoded()); 
	    scm.setKeK1(raw);
	    
		// ----------------------------------------
	    
		// ----------------------------------------
		// send the encrypted KEK2 to the node 
		// ----------------------------------------
	    
		System.out.println("Sending the encrypted KEK2 to the node "+bootMessage.getId());
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
	
	System.out.println("Sending 'start' message to members of the group");
	
	for(NetInfoGroupMember nigm : group.values()){
		Socket s = new Socket(nigm.getIpAddress(),nigm.getPort());
		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(am);
		s.close();
	}
	
	} // end StartServer
	
	
	
	public synchronized void HandleAddMember(){
		
		/*
		 * If a lock is ON that means I must wait to enter. ( somebody is sending message and I am not, or somebody is leaving in the group view )
		 * */
		while(BroadcastLock!=0 || DynLock==1){ //sono in corso dei broadcast nel gruppo o è in corso un leaving
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		DynLock=1;
		
		//TODO HANDLE HERE THE NEW MEMBER ( SEE DOCUMENT IN ORDER TO UNDERSTAND WHAT DO )
		
		
		
		
		
		
		
		//DynLock=0;REMEMBER!
		
		
		
	}
	
	
	public synchronized void HandleLeavingMember(){
		
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
		
		DynLock=1;
		
		//TODO HANDLE HERE THE LEAVING MEMBER ( SEE DOCUMENT IN ORDER TO UNDERSTAND WHAT DO )
		
		
		
		
		
		
		//DynLock=0;REMEMBER!

		
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
		return group; //return the group view 
		
	}
	
	//Once a member send a broadcast to all the group, remember to call this
	//method
	public void SignalBroadcastDone(){
		
		BroadcastLock--;
		
	}
}
