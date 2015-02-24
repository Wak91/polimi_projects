package it.polimi.flat.table;

import it.polimi.flat.table.support.ActionMessage;
import it.polimi.flat.table.support.BootMessage;
import it.polimi.flat.table.support.CommMessage;
import it.polimi.flat.table.support.NetInfoGroupMember;
import it.polimi.flat.table.support.StartConfigMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class GroupMember extends Thread {
	
	private String nodeId; //Identifier of the node 
	private Integer myPort;
	private ServerSocket mySocket; //the socket associated to this groupMember ( the listener for incoming messages )
	
	private Key publicKey;  //Initial public key
	private Key privateKey; //Initial private key
	
	private Cipher RsaCipher;
	private Cipher DesCipher;
	
	private SecretKey dek;
	private SecretKey kek0; //KEK for the bit 0 of the ID
	private SecretKey kek1; //KEK for the bit 1 of the ID
	private SecretKey kek2; //KEK for the bit 2 of the ID 
	
	/*
	 * Constructor for the GroupMember, it generates its 
	 * own public and private key for the bootstrap of 
	 * the group configuration.
	 * ListenPort is the port where this group member will
	 * listen for incoming messages 
	 * */
	public GroupMember(Integer id,Integer ListenPort){
		
		mySocket=null;
		nodeId = Integer.toBinaryString(id);
		if(nodeId.length()==1){
			nodeId = "00"+nodeId;
		}
		else
			if(nodeId.length()==2){
				nodeId="0"+nodeId;
			}
		
		System.out.println("my id is " +nodeId);
		myPort = ListenPort;
		
		//Initialize the public and private key of this node 
		//for the bootstrap of the group 
		KeyPairGenerator kpg=null;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error no such algorithm RSA");
			e.printStackTrace();
		}
		kpg.initialize(2048);
		KeyPair kp = kpg.genKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		
		
		//Initialization of the ciphers
		//-------------------------------------------------------------
		
		try {
			this.DesCipher = Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		try {
			RsaCipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	    
		//-------------------------------------------------------------

		
		
		
		
	}
	
	
	public void run(){
	
	Socket socket = this.connectToGroupController();
	this.spawnListener(); //spawn a listen socket in order to receive messages from others, the generated socket goes into mySocket attribute.
	
	System.out.println("[INFO]Started initial handshake with the group controller");
	this.InitialHandshake(socket);
	System.out.println("[INFO]Ended initial handshake with the group controller");
	/*
	System.out.println("["+this.nodeId+"] Hash of the dek is"+dek.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek0 is"+kek0.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek1 is"+kek1.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek2 is"+kek2.hashCode());
	*/
	while(true){ //listen forever
	try {

		byte[] decryptedText=null;
		String plainText="";
		
		
		//---------------
		//Waiting for an incoming connection for a message 
		
		Socket guestSocket = mySocket.accept();
		ObjectInputStream ois = new ObjectInputStream(guestSocket.getInputStream()); 

		//---------------

		CommMessage incoming = (CommMessage)ois.readObject();
		
		//now decrypt the incoming message with the group key 
		byte[] chiperText = incoming.getText();
		
		try {
			DesCipher.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		 try {
				decryptedText = DesCipher.doFinal(chiperText); //decrypting the message  
				plainText = new String(decryptedText);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("Something went wrong during decryption of text");
				e.printStackTrace();
			}
		 
		 System.out.println("GroupMember " + incoming.getIdSender() +" says " + plainText);
		 
		 //TODO Continue from here!
		
			
	} catch (IOException | ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
  } // while listener ended 
}//end run
	
	
	/*
	 * Listener for this group member in order to receive messages 
	 * from other group members
	 * */
	private void spawnListener() {
				
		try {
			mySocket = new ServerSocket(myPort);
		} catch (IOException e) {
			System.out.println("Error while listen on" +myPort);
			e.printStackTrace();
		}
	}


	private Socket connectToGroupController(){
			
		Socket socket = null;
		System.out.println("Connecting to the GroupController: localhost:5620");
		try {
			socket = new Socket("localhost",56520);			
		} catch (IOException e) {
			System.out.println("An error occured during the connection to the server");
			e.printStackTrace();
		}			
		return socket;
	}
	
	private void InitialHandshake(Socket socket){
		
		ObjectOutputStream oos=null;
		ObjectInputStream ois = null;
		StartConfigMessage scm=null;
	    byte[] decryptedKey=null;
			
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("An error occured during the oos creation");
			e.printStackTrace();
		}
		
		System.out.println("Sending id and my publicKey...");

		
		try {
			oos.writeObject(new BootMessage(this.nodeId,publicKey,new NetInfoGroupMember(mySocket.getInetAddress(),mySocket.getLocalPort())));
		} catch (IOException e) {
			System.out.println("An error occurd during the communication with the group controller");
			e.printStackTrace();
		}
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		try {
			scm = (StartConfigMessage)ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Something went wrong while reading the startConfigMessage from GroupController");
			e.printStackTrace();
		}
		
	    try {
	    	RsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		byte[] rawDek = scm.getDeK();//extract the string of the dek (publicKey encrypted )
	  
	    try {
			decryptedKey = RsaCipher.doFinal(rawDek);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of DEK");
			e.printStackTrace();
		}
	    
	    this.dek = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "DES");
		System.out.println("Successfully memorized the DEK of the group");
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK0());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK0");
			e.printStackTrace();
		}
		
		this.kek0 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		System.out.println("Successfully memorized the KEK0 of the group");
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK1());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK1");
			e.printStackTrace();
		}
		
		this.kek1 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		System.out.println("Successfully memorized the KEK1 of the group");
		
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK2());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK2");
			e.printStackTrace();
		}
		
		this.kek2 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		System.out.println("Successfully memorized the KEK2 of the group");
		
		
		try {
			byte[] decryptedTest = RsaCipher.doFinal(scm.getTest());
			String testtt = new String(decryptedTest);
			System.out.println("test decrypted is:"+testtt);

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of test");
			e.printStackTrace();
		}
		
		
		//-------Let's wait for the OK LET'S START FROM SERVER---------------------------
		System.out.println("Waiting for 'start' from GroupController");
		
		Socket guestSocket;
		ObjectInputStream oiss=null;
		
		try {
			guestSocket = mySocket.accept();
			oiss = new ObjectInputStream(guestSocket.getInputStream()); 
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		ActionMessage am=null;
		
		try {
			am = (ActionMessage)oiss.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String nodeId = "";
		String action="";
		
		 try {
				DesCipher.init(Cipher.DECRYPT_MODE, this.dek);

				byte[] decryptedId = DesCipher.doFinal(am.getnodeId()); //decrypting the message  
				nodeId = new String(decryptedId);
				
				byte[] decryptedAction = DesCipher.doFinal(am.getAction()); //decrypting the message  
				action = new String(decryptedAction);
				
			} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
				System.out.println("Something went wrong during decryption of text");
				e.printStackTrace();
			}
		 
		 if(action.equals("start")){
			
			 try {
				Thread.sleep(5000);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			 
				try {
					DesCipher.init(Cipher.ENCRYPT_MODE,dek); //initialize the cipher with the dek 
					} catch (InvalidKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				am = new ActionMessage();
				try {
					am.setnodeId(DesCipher.doFinal(this.nodeId.getBytes()));
					am.setAction(DesCipher.doFinal("getGroup".getBytes()));
				} catch (IllegalBlockSizeException | BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Socket sock = this.connectToGroupController();
				ObjectOutputStream ooss;
				try {
					ooss = new ObjectOutputStream(sock.getOutputStream());
					ObjectInputStream oiss2 = new ObjectInputStream(sock.getInputStream());
					ooss.writeObject(am);
					
					HashMap <String,NetInfoGroupMember> group = (HashMap <String,NetInfoGroupMember>)oiss2.readObject();
					
					System.out.println("Ricevuta view group attuale");
					
					if(this.nodeId.equals("001")){
					for(NetInfoGroupMember nigm : group.values()){
												
						//broadcast test from node 001
						System.out.println("received port " + nigm.getPort() + " my port " + this.myPort);
						
						if(nigm.getPort().intValue() != this.myPort.intValue()){

							Socket newsocket = new Socket(nigm.getIpAddress(),nigm.getPort());
						ooss = new ObjectOutputStream(newsocket.getOutputStream());
						CommMessage comm = new CommMessage();
						comm.setIdSender(this.nodeId);
						try {
							comm.setText(DesCipher.doFinal("test broadcast".getBytes()));
						} catch (IllegalBlockSizeException
								| BadPaddingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						ooss.writeObject(comm);
						newsocket.close();
						
						
						}
					}
				 }	
				 
				} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 	
			 return; //return to run method and start the group and start the comunication
			 
		 }
		 else
		 {
			 System.exit(-1);
		 }

				
	}
	
	/*
	 * Function used in order to send a message to all the member
	 * of the groups.It retreive the list of the member from the controller
	 * and open a socket for each of them sending the CommMessage object
	 * */
	private void BroadcastMessage(String textToSend){
		
		CommMessage msg = new CommMessage();
		msg.setIdSender(this.nodeId);

		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, this.dek);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	    try {
			byte[] encryptedText = DesCipher.doFinal(textToSend.getBytes());
			msg.setText(encryptedText);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		
		
	}
	
	/*
	 * Method used in order to left the group voluntarly 
	 * */
	private void ExitGroup(){
		
		
	}
	
	
}
