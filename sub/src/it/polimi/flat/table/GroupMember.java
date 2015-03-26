package it.polimi.flat.table;

import it.polimi.flat.table.support.ActionMessage;
import it.polimi.flat.table.support.BootMessage;
import it.polimi.flat.table.support.CommMessage;
import it.polimi.flat.table.support.CrashReportMessage;
import it.polimi.flat.table.support.NetInfoGroupMember;
import it.polimi.flat.table.support.NewDekMessage;
import it.polimi.flat.table.support.NewKekMessage;
import it.polimi.flat.table.support.StartConfigMessage;
import it.polimi.flat.table.support.StopMessage;
import it.polimi.flat.table.support.WakeUpMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class GroupMember {
	
	private String nodeId; //Identifier of the node 
	private Integer myPort;
	private ServerSocket mySocket; //the socket associated to this groupMember ( the listener for incoming messages )
	private int idle;
	
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
		
		idle=1;
		mySocket=null;
		nodeId = Integer.toBinaryString(id);
		
		//System.out.println("MY ID: " + nodeId+"\n");
		
		//Patch the nodeID on 3 bits
		if(nodeId.length()==1){
			nodeId = "00"+nodeId;
		}
		else
			if(nodeId.length()==2){
				nodeId="0"+nodeId;
			}
		
		//System.out.println("my id is " +nodeId);
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
			e.printStackTrace();
		}
		
		
		try {
			RsaCipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	
	    
		//-------------------------------------------------------------

	}
	
		
	public void run(){
	
	Socket socket = this.connectToGroupController();
	
	if(mySocket==null){
	//System.out.println("my socket is null, let's define it");
	this.spawnListener(this.myPort);
	} //spawn a listen socket in order to receive messages from others, the generated socket goes into mySocket attribute.

	System.out.println("[INFO]Started initial handshake with the group controller");
	this.InitialHandshake(socket);
	System.out.println("[INFO]Ended initial handshake with the group controller");

	/*
	System.out.println("["+this.nodeId+"] Hash of the dek is"+dek.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek0 is"+kek0.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek1 is"+kek1.hashCode());
	System.out.println("["+this.nodeId+"] Hash of the kek2 is"+kek2.hashCode());
	*/
	
	InputThread it = new InputThread(this,1); //1= node start alive obviously
	Thread t = new Thread(it);
	t.start();
			
	while(idle==0){
	try {
		
		byte[] decryptedText=null;
		String plainText="";
		
		
		//---------------
		//Waiting for an incoming connection for a message 
		
		Socket guestSocket = mySocket.accept();
		ObjectInputStream ois = new ObjectInputStream(guestSocket.getInputStream()); 
		Object message = ois.readObject();
				
		 if(message.getClass().getSimpleName().equals("CommMessage")){
			 CommMessage incoming = (CommMessage)message;
				
			//now decrypt the incoming message with the group key 
			byte[] chiperText = incoming.getText();
			
			try {
				DesCipher.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				}
			
			 try {
					decryptedText = DesCipher.doFinal(chiperText); //decrypting the message  
					plainText = new String(decryptedText);
				} catch (IllegalBlockSizeException | BadPaddingException e) {
					System.out.println("[ERROR]Something went wrong during decryption of text");
					//e.printStackTrace();
				}
			 
			 System.out.println("\n-------------------NEW MESSAGE!-------------------");
			 System.out.println("GroupMember " + incoming.getIdSender() +" says: " + plainText);
			 System.out.println("--------------------------------------------------\n");
			 
			 System.out.println("[INFO]Waiting for something to broadcast...");

			 
		    }
		 //caso nuova dek
		 else if(message.getClass().getSimpleName().equals("NewDekMessage")){
			System.out.println("[INFO]New DeK has been received");
			this.retrieveNewDek(message);
		 }
		 //caso nuove kek
		 else if(message.getClass().getSimpleName().equals("NewKekMessage")){
				System.out.println("[INFO]New KeKs has been received");
				this.retrieveNewKek(message);
		 }
		 
		 else if(message.getClass().getSimpleName().equals("StopMessage")){
			 
			 //System.out.println("Stop message arrived");
			 this.idle=1;	 
		 }
		//---------------
		//I have to check previously what kind of message is in order to understand what to do 
		 else if(message.getClass().getSimpleName().equals("StartConfigMessage")){
			 
			 //System.out.println("NODEID => "+nodeId + " RECEIVE STARTCONFIGMESSAGE");
			
			 //update keys and dek
			StartConfigMessage scm = (StartConfigMessage)message;
			DesCipher.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
			byte[] rawDek = scm.getDeK();//extract the string of the dek (old dek encrypted )
			byte[] decryptedKey = null;
			try {
				decryptedKey = DesCipher.doFinal(rawDek);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("[ERROR]Something went wrong during decryption of DEK");
				e.printStackTrace();
			}
			
			this.dek = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "DES");
			try {
				decryptedKey = DesCipher.doFinal(scm.getKeK0());
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("[ERROR]Something went wrong during decryption of KEK0");
				e.printStackTrace();
			}
			
			this.kek0 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
			
			try {
				decryptedKey = DesCipher.doFinal(scm.getKeK1());
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("[ERROR]Something went wrong during decryption of KEK1");
				e.printStackTrace();
			}
			
			this.kek1 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
			//System.out.println("Successfully memorized the KEK1 of the group");
			
			
			try {
				decryptedKey = DesCipher.doFinal(scm.getKeK2());
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				System.out.println("[ERROR]Something went wrong during decryption of KEK2");
				e.printStackTrace();
			}
			
			this.kek2 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
			
			//-------Let's wait for the OK LET'S START FROM SERVER---------------------------
			System.out.println("[INFO]Waiting for 'start' from GroupController");
			waitingForStartMessage();
			System.out.println("[INFO]Received 'start' from GroupController");
			System.out.println("[INFO]Waiting for something to broadcast...");

		}
		guestSocket.close();
			
			
	} catch (IOException | ClassNotFoundException e) {
		e.printStackTrace();
	} catch (InvalidKeyException e) {
		e.printStackTrace();
	}
	
  } // while listener ended 

	//in idle=1 accept only a commMessage or a message that wake up the member
	while(idle==1){
		
		try{
		Socket guestSocket = mySocket.accept();
		ObjectInputStream ois = new ObjectInputStream(guestSocket.getInputStream()); 
		Object message = ois.readObject();
		byte[] decryptedText=null;
		String plainText="";
		
		 if(message.getClass().getSimpleName().equals("CommMessage")){
			 CommMessage incoming = (CommMessage)message;
			//now decrypt the incoming message with the group key 
				byte[] chiperText = incoming.getText();
				
				try {
					DesCipher.init(Cipher.DECRYPT_MODE,dek); //initialize the cipher with the dek 
					} catch (InvalidKeyException e) {
					}
				
				 try {
						decryptedText = DesCipher.doFinal(chiperText); //decrypting the message  
						plainText = new String(decryptedText);
						System.out.println("GroupMember " + incoming.getIdSender() +" says " + plainText);
					} catch (IllegalBlockSizeException | BadPaddingException e) {
						System.out.println("Something went wrong during decryption of text");
					}				 
		 }
		 else if (message.getClass().getSimpleName().equals("WakeUpMessage")){ 
			 this.idle=0;
			 continue;
		 }
		
		}
		catch(Exception e){
			
		}	
	} //end while ( idle=1 )
	
	System.out.println("end run cycle");
}//end run
	
	
	/**
	 * cerchiamo di ricavare la nuova dek in seguito ad un leave di un nodo
	 * @param message
	 */
	private void retrieveNewDek(Object message) {
		//ricaviamo il messaggio castandolo giusto
		NewDekMessage incoming = (NewDekMessage) message;

		//ricaviamo la lista di cypher
		ArrayList<byte[]> newDekList = incoming.getnewDekList();
		
		for (int i = 0; i < 3; i++) {
			//ricaviamo la kek giusta (proviamo con tutte tanto sono poche)
			try {
				switch (i) {
				case 0:
					DesCipher.init(Cipher.DECRYPT_MODE, kek0);
					break;
				case 1:
					DesCipher.init(Cipher.DECRYPT_MODE, kek1);
					break;
				case 2:
					DesCipher.init(Cipher.DECRYPT_MODE, kek2);
					break;
				}
			}
			catch (InvalidKeyException e) {
				e.printStackTrace();
			}
			//per ogni cipher proviamo a decriptarlo
			for (byte[] bs : newDekList) {
				byte[] decryptedText = null;
				try {
					//decrypt (sa gia quando fallisce quindi non ho bisogno di token  cose simili per riconoscere la chiave giusta)
					decryptedText = DesCipher.doFinal(bs);
					//settiamo la nuova dek
					this.dek = new SecretKeySpec(decryptedText, 0, decryptedText.length, "DES");
					//System.out.println("trovata nuova dek!!");
				} 
				//se ho un eccezione vuol dire che la chiave non e quella giusta
				catch (IllegalBlockSizeException | BadPaddingException e) {
					continue;
				}
				
			}						
			
		}

	}
	
	private void retrieveNewKek(Object message) {
		//ricaviamo il messaggio castandolo giusto
		NewKekMessage incoming = (NewKekMessage) message;

		//ricaviamo la lista di cypher
		ArrayList<byte[]> newDekList = incoming.getnewKekList();
		
		for (int i = 0; i < 3; i++) {
			//ricaviamo la kek giusta (proviamo con tutte tanto sono poche)
			try {
				switch (i) {
				case 0:
					DesCipher.init(Cipher.DECRYPT_MODE, kek0);
					break;
				case 1:
					DesCipher.init(Cipher.DECRYPT_MODE, kek1);
					break;
				case 2:
					DesCipher.init(Cipher.DECRYPT_MODE, kek2);
					break;
				}
			}
			catch (InvalidKeyException e) {
				e.printStackTrace();
			}
			//per ogni cypher proviamo a decriptarlo
			for (byte[] bs : newDekList) {
				byte[] decryptedDek = null;
				byte[] decryptedKek = null;
				try {
					//decrypt (sa gia quando fallisce quindi non ho bisogno di token  cose simili per riconoscere la chiave giusta)
					decryptedDek = DesCipher.doFinal(bs);
					//setto gia qui tanto non ho casi in cui la stessa kek decripta 2 messaggi
					try {
						DesCipher.init(Cipher.DECRYPT_MODE, dek);
					} catch (Exception e) {
						e.printStackTrace();
					}
					//System.out.println("TROVATA NUOVA KEK!!");
					//settiamo la nuova kek
					decryptedKek = DesCipher.doFinal(decryptedDek);

					switch (i) {
						case 0:
							this.kek0 = new SecretKeySpec(decryptedKek, 0, decryptedKek.length, "DES");
							break;
						case 1:
							this.kek1 = new SecretKeySpec(decryptedKek, 0, decryptedKek.length, "DES");
							break;
						case 2:
							this.kek2 = new SecretKeySpec(decryptedKek, 0, decryptedKek.length, "DES");
							break;

					}
					
					//System.out.println("trovata nuova dek!!");
				} 
				//se ho un eccezione vuol dire che la chiave non e quella giusta
				catch (IllegalBlockSizeException | BadPaddingException e) {
					continue;
				}
				
			}						
			
		}

	}
	
	
	/*
	 * Listener for this group member in order to receive messages 
	 * from other group members
	 * */
	private void spawnListener(Integer port) {
				
		try {
			mySocket = new ServerSocket(port);
		} catch (IOException e) {
			//se abbiamo un errore perche la porta e gia in uso facciamo scegliere un altra porta
			System.out.println("Error while listen on" + myPort + "\n");
			System.out.println("Select another port: ");	
			BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
			try {
				String newPort = bufferRead.readLine();
				this.myPort = Integer.parseInt(newPort);
				port = this.myPort;
				spawnListener(port);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			
			//e.printStackTrace();
		}
	}
	

	private Socket connectToGroupController(){
			
		Socket socket = null;
		//sSystem.out.println("Connecting to the GroupController: localhost:5620");
		try {
			socket = new Socket("localhost",56520);			
		} catch (IOException e) {
			System.out.println("An error occured during the connection to the server");
			e.printStackTrace();
		}			
		return socket;
	}
	
	/*
	 * Perform the first handshake with the group controller
	 * @socket is the socket opened with the controller 
	 * */
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
		
		//System.out.println("Sending id and my publicKey...");

		
		try {
			oos.writeObject(new BootMessage(this.nodeId,publicKey,new NetInfoGroupMember(mySocket.getInetAddress(),mySocket.getLocalPort())));
		} catch (IOException e) {
			System.out.println("An error occur during the communication with the group controller");
			e.printStackTrace();
		}
		
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println("WAITING FOR STARTCONFIGMESSAGE");
		
		try {
			scm = (StartConfigMessage)ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Something went wrong while reading the startConfigMessage from GroupController");
			e.printStackTrace();
		}
		
		//System.out.println("RECEIVE  STARTCONFIGMESSAGE");

		
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
		//System.out.println("Successfully memorized the DEK of the group");
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK0());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK0");
			e.printStackTrace();
		}
		
		this.kek0 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		//System.out.println("Successfully memorized the KEK0 of the group");
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK1());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK1");
			e.printStackTrace();
		}
		
		this.kek1 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		//System.out.println("Successfully memorized the KEK1 of the group");
		
		
		try {
			decryptedKey = RsaCipher.doFinal(scm.getKeK2());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of KEK2");
			e.printStackTrace();
		}
		
		this.kek2 = new SecretKeySpec(decryptedKey,0,decryptedKey.length,"DES");
		//System.out.println("Successfully memorized the KEK2 of the group");
		
		/*
		try {
			byte[] decryptedTest = RsaCipher.doFinal(scm.getTest());
			String testtt = new String(decryptedTest);
			System.out.println("test decrypted is:"+testtt);

		} catch (IllegalBlockSizeException | BadPaddingException e) {
			System.out.println("Something went wrong during decryption of test");
			e.printStackTrace();
		}
		*/
		
		//-------Let's wait for the OK LET'S START FROM SERVER---------------------------
		System.out.println("[INFO]Waiting for 'start' from group controller");
		waitingForStartMessage();			
	}
	
	/**
	 * Method that wait the start message coming from the group manager after have joined 
	 * the group
	 */
	private void waitingForStartMessage(){
		
		//System.out.println("in waiting start message");
		
		Socket guestSocket;
		ObjectInputStream oiss=null;
		
		try {
			guestSocket = mySocket.accept();
			oiss = new ObjectInputStream(guestSocket.getInputStream()); 
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		
		ActionMessage am=null;
		
		try {
			am = (ActionMessage)oiss.readObject();
		} catch (ClassNotFoundException | IOException e) {
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
				System.out.println("Something went wrong during decryption of start message");
				e.printStackTrace();
			}
		 
		 if(action.equals("start")){
			 this.idle=0;
			 return;
		}	
			 
		 else
		 {
			 System.exit(-1); //Something strange from server, go home we are drunk...
		 }

	}
	
	/*
	 * Function used in order to send a message to all the member
	 * of the groups.It retreive the list of the member from the controller
	 * and open a socket for each of them sending the CommMessage object
	 * */
	private void BroadcastMessage(String textToSend){
		
	
		ActionMessage am = new ActionMessage(); //to request the viewgroup from groupcontroller
		CommMessage msg = new CommMessage();
	
		try {
			
			DesCipher.init(Cipher.ENCRYPT_MODE,dek); //initialize the cipher with the dek 
			am.setnodeId(DesCipher.doFinal(this.nodeId.getBytes()));
			am.setAction(DesCipher.doFinal("getGroup".getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e1) {
			e1.printStackTrace();
		}
		
		msg.setIdSender(this.nodeId);

		try {
			DesCipher.init(Cipher.ENCRYPT_MODE, this.dek);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}		
	    try {
			byte[] encryptedText = DesCipher.doFinal(textToSend.getBytes());
			msg.setText(encryptedText);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
	   
	    
	    Socket sock = this.connectToGroupController();
		ObjectOutputStream ooss=null;
		ArrayList <NetInfoGroupMember> crashedMembers = new ArrayList<NetInfoGroupMember>();
		
		try {
			ooss = new ObjectOutputStream(sock.getOutputStream());
			ObjectInputStream oiss2 = new ObjectInputStream(sock.getInputStream());
			ooss.writeObject(am);
			
			HashMap <String,NetInfoGroupMember> group = (HashMap <String,NetInfoGroupMember>)oiss2.readObject();
			
			System.out.println("Ricevuta view group attuale");
	    
			for(NetInfoGroupMember nigm : group.values()){
				
				//System.out.println("received port " + nigm.getPort() + " my port " + this.myPort);
				
				if(nigm.getPort().intValue() != this.myPort.intValue()){

				try{
				Socket newsocket = new Socket(nigm.getIpAddress(),nigm.getPort());
				ooss = new ObjectOutputStream(newsocket.getOutputStream());
				
				ooss.writeObject(msg);
				newsocket.close();
				}catch(ConnectException ce){	
					//In case somebody crashes...
					System.out.println("The group member with IP: " + nigm.getIpAddress() +"and PORT: " +nigm.getPort() +" seems crashed...");
					System.out.println("Let's continue with the other members...");
					crashedMembers.add(nigm); //keep track of the crashed member in order to inform the group controller 
				}
				finally{
					continue;
				}
				
				}
			} //end foreach
						
			//POC OF THE SECRET OF THE CONVERSATION ( send the message also to a MITM )
			
			//-----------------------------------------------------
			try{
			Socket newsocket = new Socket("localhost",9000);
			ooss = new ObjectOutputStream(newsocket.getOutputStream());
			ooss.writeObject(msg);
			newsocket.close();
			}
			catch(ConnectException ce){
				System.out.println("No MITM for the PoC...");
			}
			//-----------------------------------------------------
			  		
		}
		catch(Exception e){
			System.out.println("Something went wrong during the broadcast of the message...");
			e.printStackTrace();
		}
		
		this.NotifyGroupController(crashedMembers);
		
		
	}
	
	
	
	/*
	 * This method is exploited in order to
	 * signal to the group controller the crashed members, so it can
	 * delete them from the group view 
	 * */
	private void notifyCrashedMembers(ArrayList <NetInfoGroupMember> crashed) {
		
		Socket sock = this.connectToGroupController();
		ObjectOutputStream ooss=null;
		try {
			ooss = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		CrashReportMessage crm = new CrashReportMessage();
		
		try {
			DesCipher.init(Cipher.ENCRYPT_MODE,dek); //initialize the cipher with the dek 
			crm.setnodeId(DesCipher.doFinal(this.nodeId.getBytes()));
			crm.setAction(DesCipher.doFinal("crashreport".getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e1) {
			e1.printStackTrace();
		}
		
		//System.out.println("The crashed members are"+crashed.size());
		crm.setCrashedMembers(crashed);
		
		try {
			ooss.writeObject(crm); //Signal the groupController of a broadcastDone.
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		return;
		
	}


	/*
	 * Notify the controller about the broadcast end 
	 * in order to decrease the BroadcasLock 
	 * */
	private void NotifyGroupController(ArrayList <NetInfoGroupMember> crashedMembers){
		
		/*
		 * If i discover that somebody is crashed, let's notify the controller about this, without remove
		 * our broadcast lock!
		 * */
		if(crashedMembers.size()!=0){
			//System.out.println("Hey now I'm calling notify crashed");
			this.notifyCrashedMembers(crashedMembers);
			return;
		}
		
		//mandiamo il messaggio
		
		this.buildAndSendMessage("broadcastdone");
				
		return;
		
	}
	
	/*
	 * Method used in order to left the group voluntarly 
	 * */
	private void ExitGroup(){
		
		Socket socket = null;
		try {
			socket = new Socket("localhost",this.myPort);	
			ObjectOutputStream ooss = new ObjectOutputStream(socket.getOutputStream());
			
			StopMessage sm = new StopMessage();
		
			ooss.writeObject(sm);
			
			socket.close();

		} catch (Exception e) {
			System.out.println("An error occured during the sending of the stop message");
			e.printStackTrace();
		}			
		
		//this.spawnListener(9999); //Port of the death! ( the members will listen here to prove forward security )
		InputThread it = new InputThread(this,0);
		Thread t = new Thread(it);
		t.start();
		
		//PROVA
		this.buildAndSendMessage("leave");
		
	}
	
	public void wakeUp() {
		
		Socket socket = null;
		try {
			socket = new Socket("localhost",this.myPort);	
			ObjectOutputStream ooss = new ObjectOutputStream(socket.getOutputStream());
			
			WakeUpMessage wm = new WakeUpMessage();

			ooss.writeObject(wm);
			
			socket.close();

		} catch (Exception e) {
			System.out.println("An error occured during the sending of the wake up message");
			e.printStackTrace();
		}		
	}
	
	
	
	/**
	 * funzione da chiamare quando abbiamo un messaggio azione e non broadcast
	 * @param action
	 */
	private void buildAndSendMessage(String action){
		Socket sock = this.connectToGroupController();
		ObjectOutputStream ooss=null;
		try {
			ooss = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		ActionMessage am = new ActionMessage();
		
		try {
			DesCipher.init(Cipher.ENCRYPT_MODE,dek); //initialize the cipher with the dek 
			am.setnodeId(DesCipher.doFinal(this.nodeId.getBytes()));
			am.setAction(DesCipher.doFinal(action.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException e1) {
			e1.printStackTrace();
		}
		
		try {
			ooss.writeObject(am); //Signal the groupController of a broadcastDone.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	/*
	 * This is used in order to provide an interface where you can 
	 * write and see the broadcast communication between the 
	 * groupmember
	 * */
	private class InputThread implements Runnable{

		private GroupMember gm;
		private int mode; //0= node dead, permi only an 'add' to rejoin the group
						  //1= node alive! give complete terminal
				
		public InputThread(GroupMember gm , int mode){
			this.gm = gm;
			this.mode=mode;
		}
		
		@Override
		public void run() {
			
			if(this.mode==1){
			while(true){
				String line="";
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("[INFO]Waiting for something to broadcast...");
				try {
					line = br.readLine();
				}catch (IOException e) {
					e.printStackTrace();
				}
				
				if(line.equals("leave")){
					this.gm.ExitGroup();
					break; //terminate this thread
				}
				
				else{
					//System.out.println("chiamo broadcast message");
					gm.BroadcastMessage(line);	
				}
			
			} //end while(true)			
		  }
			else{ //dead mode, give only the possibility to rejoin from the terminal
				while(true){
					
					String line="";
					BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					System.out.println("\n[!!!]RESTRICTED TERMINAL[!!!]");
					try {
						line = br.readLine();
					}catch (IOException e) {
						e.printStackTrace();
					}
					
					if(line.equals("join")){
						
						this.gm.wakeUp(); //wake up the son		
						this.gm.run();
						break;
					}
					
					else{
						System.out.println("[INFO]If you want to speak with the group you have to perform a 'join'");
					}
				
				} //end while(true)				
			}
		}
	}

	
}
