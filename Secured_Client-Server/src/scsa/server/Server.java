package scsa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scsa.server.commands.GetDocumentCommand;
import scsa.server.commands.UserDetailsCommand;
import scsa.server.commands.UserListCommand;

public class Server {
	
	CommandProcessor commandProcessor = null;
	UsersStore usersStore = null;
	AccessManager accessManager = null;	
	
	public static final int PORT = 7070;
	
	public ServerSocket getServer() throws IOException {
		return new ServerSocket(PORT);
	}
	
	private void init() {		
		usersStore = new UsersStore();
		accessManager = new AccessManager(usersStore);
		accessManager.addAllowedCommand("ADMIN", "userslist");
		accessManager.addAllowedCommand("ADMIN", "userdetails");
		accessManager.addAllowedCommand("ADMIN", "getdocument");
		
		accessManager.addAllowedCommand("USER", "userslist");
		accessManager.addAllowedCommand("USER", "getdocument");
		
		commandProcessor = new CommandProcessor(accessManager); 
		commandProcessor.addCommand("userslist", new UserListCommand(usersStore));
		commandProcessor.addCommand("userdetails", new UserDetailsCommand(usersStore));
		commandProcessor.addCommand("getdocument", new GetDocumentCommand(usersStore));
	}
	
	public void run() {
		
		init();
		
		ServerSocket listen;
		
		try {
			listen = getServer();
			while(true) {
				Socket client = listen.accept();
				
				new ProcessConnection(
						client,
						commandProcessor,
						accessManager,
						usersStore );
			}
		} catch (Exception e) {
			System.out.println("[Exception] " + e.getMessage());
		}
	}
	
	public static void main(String args[]) {
		Server server = new Server();
		System.out.println("[SERVER STARTED]");
		server.run();
	}
	
}