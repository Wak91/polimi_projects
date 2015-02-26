package it.polimi.flat.table;

import it.polimi.flat.table.support.CommMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MitmMember {
	
	
	public static void main(String args[]){
		
		ServerSocket s =null;
		ObjectInputStream ois=null;
		CommMessage am=null;
		Socket socket=null;

		try {
			s = new ServerSocket(9000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Man In The Middle spawned");
		
		while(true){
	
		try {
			socket = s.accept();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			 ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			am = (CommMessage) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String messageIntercepted = new String(am.getText());
		System.out.println("I've intercepted a message: " + messageIntercepted);	
	}	
}

}
