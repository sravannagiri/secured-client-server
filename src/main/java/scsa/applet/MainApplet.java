package scsa.applet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import scsa.client.Client;

public class MainApplet extends JApplet {

	private static final long serialVersionUID = 3445688350202871275L;

	private JButton loginButton = new JButton("Login");
	private JButton submitButton = new JButton("Submit");
	private JButton logoutButton = new JButton("Logout");
	
	private ActionListener loginButtonClick = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			String login = loginInput.getText();
			String password = new String(passwordInput.getPassword());
			
			String errorMessage = null;
			
			try {
				if ( client.connectAndHandshake("localhost", 7070) ) {
					if ( client.login(login, password) ) {
						isLoggedIn = true;
						loginDialog.setVisible(false);
					} else {
						errorMessage = "Login failed";
					}
				} else {
					errorMessage = "Connection failed";
				}
			} catch (Exception exception) {
				errorMessage = exception.getMessage();
			} finally {
				if ( ! isLoggedIn ) {
					JOptionPane.showMessageDialog((Component) e.getSource(), 
							errorMessage);
				}
			}
		}
	};
	
	private ActionListener submitButtonClick = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			String input = commandInput.getText();
			
			if ( ! isLoggedIn || input.trim().length() == 0 ) {
				commandInput.setText( "[Not logged in]" );
				commandResult.setText( "[Not logged in]" );
				return;
			}
			
			String output = null;
			
			try {
				output = client.command(input);
			} catch (Exception e1) {
				output = "[ERROR]" + e1.getMessage();
			} finally {
				commandResult.append( "[[ " + input + " ]]\n" + output + "\n" );
			}
			
		}
	};
	
	private ActionListener logoutButtonClick = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			
			String output = null;
			
			if ( ! isLoggedIn ) {
				commandInput.setText( "[Not logged in]" );
				commandResult.setText( "[Not logged in]" );
				return;
			}
			
			try {
				client.command("quit");
			} catch (Exception e1) {
				output = "[ERROR] " + e1.getMessage();
			} finally {
				if ( output == null )
					output = "[SESSION CLOSED]";
				
				commandInput.setText( output );
				commandResult.setText( output );
			}
			
		}
	};
	
	private JDialog loginDialog = new JDialog();
	
	private JTextField loginInput = new JTextField(10);
	private JPasswordField passwordInput = new JPasswordField(10);
	
	private JTextField commandInput = new JTextField(25);
	private JTextArea commandResult = new JTextArea(20, 40);
	
	private boolean isLoggedIn = false; 
	private Client client = null;
	
	public void init() {
		
		client = new Client();
		
		loginButton.addActionListener( loginButtonClick );
		
		loginDialog.setModal(true);
		
		loginDialog.setLayout(new GridLayout(3,1));
		loginDialog.add( loginInput );
		loginDialog.add( passwordInput );
		loginDialog.add( loginButton );
		loginDialog.pack();
		loginDialog.setVisible(true);
		
		Container c = getContentPane();
		c.setLayout( new FlowLayout() );
		
		submitButton.addActionListener( submitButtonClick );
		logoutButton.addActionListener( logoutButtonClick );
		
		commandInput.setText("");
		commandResult.setEditable(false);
		
		c.add( commandInput );
		c.add( submitButton );
		c.add( logoutButton );
		c.add( new JScrollPane(commandResult) );
	}
	
	public static void main(String[] args) {
	    JApplet applet = new MainApplet();
	    JFrame frame = new JFrame("Applet1c");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(applet);
	    frame.setSize(100,50);
	    applet.init();
	    applet.start();
	    frame.setVisible(true);
	  }

}
