package View;

import it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti.GameOption;
import it.polimi.ingegneriaDelSoftware2013.horseFever_lorenzo2.fontana_fabio1.gritti.Main;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Welcome_horse_fever implements ActionListener{

	private JFrame frame;
	private Container c;
	private JPanel panel;
	private JButton start;
	private JButton developer;
	private JButton exit;
	private JLabel welcome;
	private Choice mode;
	private Choice rete;
	
	public Welcome_horse_fever ()
	{
		frame =  new JFrame("Welcome to Horse Fever!");
		panel = new JPanel();
		start = new JButton("Start");
		developer = new JButton("Info");
		exit = new JButton("Exit");
		welcome = new JLabel();
		mode = new Choice();
		rete = new Choice();
		
		rete.add("Locale");
		rete.add("Server");
		rete.add("Client");		
		
		mode.add("Text Mode");
		mode.add("Gui mode");
		
		ImageIcon img = new ImageIcon("images/horse_fever_logo.png");
		welcome.setIcon(img);
		
		c = frame.getContentPane();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel.setLayout(null);
		panel.add(rete);
		panel.setBackground(Color.darkGray);
		panel.add(start);
		panel.add(mode);
		panel.add(welcome);
		panel.add(developer);
		panel.add(exit);
		
		developer.setBounds(410,50,80,30);
		rete.setBounds(410, 150, 80, 30);
		welcome.setBounds(0,-115,490,500);
		start.setBounds(410, 10, 80 , 30);
		mode.setBounds(410, 100 , 80, 30);
		exit.setBounds(410, 230, 80, 30);
		start.setBackground(Color.ORANGE);
		exit.setBackground(Color.ORANGE);
		developer.setBackground(Color.ORANGE);
		mode.setBackground(Color.ORANGE);
		
		developer.addActionListener(this);
		exit.addActionListener(this);
		start.addActionListener(this);

		c.add(panel);
		frame.setSize(500, 300);
		frame.setResizable(false);
	    frame.setVisible(true);
	}

	
	
	public void actionPerformed(ActionEvent e) {
		if("Info".equals(e.getActionCommand()))
		  {
		   JOptionPane.showMessageDialog(null, "Fabio Gritti & Lorenzo Fontana", "Developer", JOptionPane.INFORMATION_MESSAGE);
		  }
		if("Exit".equals(e.getActionCommand()))
		  {
			for(int i=500;i>=0;i--)
			  {frame.setSize(i,300);}
			for(int i=300;i>=0;i--)
			 {frame.setSize(0,i);}
			System.exit(0);
		  }
		if("Start".equals(e.getActionCommand()))
		  {	
		   int index = mode.getSelectedIndex();
		   if(rete.getSelectedIndex()==1){
			   GameOption.rete_server=true;
				String num = (JOptionPane.showInputDialog("Inserisci numero giocatori"));
				try {
					Integer num_p = Integer.parseInt(num);
					if (2<=num_p&&num_p<=6) {
						GameOption.numplayer=num_p;		
						GameOption.gui_text=index;
						   frame.dispose();
						   Main.procedi();
					}
					else {
						JOptionPane.showMessageDialog(null, "Inserisci numero compreso tra 2 e 6");
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Inserisci numero compreso tra 2 e 6");
				}
				
		   }
		   else if (rete.getSelectedIndex()==2) {
			GameOption.rete_client=true;
			GameOption.gui_text=index;
			   frame.dispose();
			   Main.procedi();
		}
		   else {
			GameOption.singleplayer=true;
			GameOption.gui_text=index;
			   frame.dispose();
			   Main.procedi();
		}
		   
		   }
	}


	
}
