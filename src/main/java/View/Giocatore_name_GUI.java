package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import Controller_events.getNomePlayerEvent;
import Gui_Events.setNomeEvent;

public class Giocatore_name_GUI {
	
	GUI_interface gui_interface;
	getNomePlayerEvent inEvent;

	
	private final Toolkit toolkit ;
	private int xSize;
	private int ySize;
	private JFrame frame;
	
	JTextField name ;
	JPanel topPanel ;
	JPanel bottom_panel;
	String name_player;
	
	public Giocatore_name_GUI(GUI_interface gui,getNomePlayerEvent evento) {
		toolkit = Toolkit.getDefaultToolkit();
		xSize = ((int) toolkit.getScreenSize().getWidth());  
		ySize = ((int) toolkit.getScreenSize().getHeight()); 
		
		inEvent=evento;
		gui_interface=gui;
		frame=gui.get_frame();
		frame.setBounds(xSize/4, ySize/9, xSize/2, ySize*6/7);
		frame.setTitle("INSERIMENTO NOMI");
		frame.setResizable(false);

		
		topPanel =new JPanel() { //nota che con l'apertura della graffa creiamo una sottoclasse di JPanel
			
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui_interface.resizeImage("images/images.jpg", 732, 543));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		topPanel.setBounds(0, 0, xSize/2, ySize/7);
		frame.add(topPanel);
		
		bottom_panel = new JPanel() { 		
			@Override
			protected void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				
				ImageIcon legni = new ImageIcon(gui_interface.resizeImage("images/images.jpg", 732, 543));
				Image legno = legni.getImage();
				graphics.drawImage(legno, 0, 0, null);
			}
		};
		bottom_panel.setBounds(0, ySize*5/7, xSize/2, ySize/7);
		bottom_panel.setBackground(new Color(30,30,30));
		frame.add(bottom_panel);
		
		
		
		
	}

	

	public void show() {
		JButton btnButton = new JButton("CONFERMA");

		JLabel title = new JLabel();
		title.setText("INSERIMENTO NOMI GIOCATORI");
		title.setForeground(Color.white);
		title.setFont(new Font("Monotype Corsiva", Font.ITALIC, 30));

		
		
		
		class Imagepanel extends JPanel{
			private BufferedImage image;

		    public Imagepanel(BufferedImage imag) {
		       image = imag;
		    }

		    
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // see javadoc for more info on the parameters            
		    }
		}
		
		
		BufferedImage back = gui_interface.resizeImage("images/insert_name.png", xSize, ySize*6/7);
		


		
		Imagepanel panel = new Imagepanel(back);
		
		panel.setLayout(null);
		panel.setBounds(0, ySize/7,xSize/2,ySize*4/7);

		name = new JTextField(10);
		name.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		name.setFont(new Font("SansSerif", Font.BOLD, 50));
		name.setBounds(xSize*1/8,ySize/3,xSize*2/7,ySize/7);
		
		panel.add(name);
        
		
		bottom_panel.add(btnButton);
		

		frame.add(panel);
		
		btnButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				name_player = name.getText();
				frame.getContentPane().removeAll();
				
				setNomeEvent event = new setNomeEvent(name_player);
				inEvent.getController().create_player(event);
				
			}
		});
		
		topPanel.revalidate();

		panel.revalidate();

		bottom_panel.revalidate();
		topPanel.add(title);
		

		frame.repaint();
		frame.setVisible(true);
	}

}
