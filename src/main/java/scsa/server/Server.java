package scsa.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import scsa.server.commands.AddUserCommand;
import scsa.server.commands.GetDocumentCommand;
import scsa.server.commands.UpdateUserStatusCommand;
import scsa.server.commands.UserDetailsCommand;
import scsa.server.commands.UserListCommand;

public class Server {
	
	CommandProcessor commandProcessor = null;
	UsersStore usersStore = null;
	AccessManager accessManager = null;
	
	ProcessConnection connections[] = null;
	
	public static final int PORT = 7070;
	
	public ServerSocket getServer() throws IOException {
		return new ServerSocket(PORT);
	}
	
	private void init() {
		
		System.out.println("Server initialization, please wait...");
		
		usersStore = new UsersStore("users.dat");
		
		accessManager = new AccessManager(usersStore);
		accessManager.addAllowedCommand("ADMIN", "userslist");
		accessManager.addAllowedCommand("ADMIN", "userdetails");
		accessManager.addAllowedCommand("ADMIN", "getdocument");
		accessManager.addAllowedCommand("ADMIN", "adduser");
		accessManager.addAllowedCommand("ADMIN", "updateuserstatus");
		
		accessManager.addAllowedCommand("USER", "userslist");
		accessManager.addAllowedCommand("USER", "getdocument");
		
		commandProcessor = new CommandProcessor(accessManager); 
		commandProcessor.addCommand("userslist", new UserListCommand(usersStore));
		commandProcessor.addCommand("userdetails", new UserDetailsCommand(usersStore));
		commandProcessor.addCommand("getdocument", new GetDocumentCommand(usersStore));
		commandProcessor.addCommand("adduser", new AddUserCommand(usersStore));
		commandProcessor.addCommand("updateuserstatus", new UpdateUserStatusCommand(usersStore));
		
		connections = new ProcessConnection[] {
				new ProcessConnection(commandProcessor, accessManager, usersStore),
				new ProcessConnection(commandProcessor, accessManager, usersStore),
				new ProcessConnection(commandProcessor, accessManager, usersStore)
		};
		
		System.out.println("Server initialization complete.\n " +
				"Server is ready to accept client connections.");
	}
	
	public void run() {
	
		int i=0;
		
		ServerSocket listen;
		
		try {
			listen = getServer();
			while(true) {
				Socket client = listen.accept();
				
				if ( i < connections.length ) {
					connections[i].setClientSocket(client);
					connections[i].start();
					
					i++;
					System.out.println("Client number: " + i);
					
					if (i==connections.length) {
						System.out.println("Generating new ProcessConnectors");
						connections = new ProcessConnection[] {
								new ProcessConnection(commandProcessor, accessManager, usersStore),
								new ProcessConnection(commandProcessor, accessManager, usersStore),
								new ProcessConnection(commandProcessor, accessManager, usersStore)
						};
						System.out.println("Generating new ProcessConnectors complete");
						i = 0;
					}
				} 
				
			}
		} catch (Exception e) {
			System.out.println("[Exception] " + e.getMessage());
		}
	}
	
	public static void main(String args[]) {
		Server server = new Server();
		server.init();
		System.out.println("[SERVER STARTED]");
		server.run();
	}
	
}