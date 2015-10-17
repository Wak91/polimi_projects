package it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti;

import Controller.Controller;
import Controller.Controller_Interface;
import Gui_Events.setNumPlayerEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;


import Model.Game;
import Model.Model_interface;
import Network.Client;
import Network.ModReteUI;
import Network.Server;
import View.GUI_interface;
import View.Text_interface;
import View.User_interface;
import View.Welcome_horse_fever;


public class Main {

	private static Controller_Interface game_controller;
	private static  User_interface uInterface;
	private static Model_interface model_interface;

	private Main(){}
	
	public static void main(String args[])
	{
		new Welcome_horse_fever();
	}
	
	
	
	public static void procedi()
	{		
		model_interface = new Game();
		
		
		if(GameOption.singleplayer==true)
		    {
			 if(GameOption.gui_text == 1)
			     { 				 
				 uInterface = new GUI_interface();	     
			     }
			 else
			     {
				uInterface = new Text_interface();
			     }
			game_controller = new Controller(uInterface,model_interface);	
			 game_controller.get_player_number();
		   }
		
		else {
			uInterface=new ModReteUI();
			game_controller = new Controller(uInterface,model_interface);
			if (GameOption.rete_server) {
				try {
					new Server(game_controller,model_interface);
					InetAddress serveraddress= InetAddress.getByName("localhost");
					int serverport = 4560;
					Client client= new Client(serveraddress,serverport);
					client.go();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(GameOption.rete_client){
				try {
					//InetAddress serveraddress= InetAddress.getByName("localhost");
					//int serverport = 4560;
					String serveraddress_in = (JOptionPane.showInputDialog("Inserisci indirizzo server"));
					String port_in = (JOptionPane.showInputDialog("Inserisci porta server"));
					InetAddress serveraddress = InetAddress.getByName(serveraddress_in);
					Integer serverport = Integer.parseInt(port_in);
					Client client= new Client(serveraddress,serverport);
					client.go(); // i client non dovrebbero cominciare qua, ma dovrebbero aspettare che il server abbia aspettato tutti 
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
		     }
			
		   }

	   }
	
	public static void start_online(setNumPlayerEvent evt){	
		System.out.println("Let's start");
		
		 game_controller.how_many_player(evt);
	 }
	
	
	
}