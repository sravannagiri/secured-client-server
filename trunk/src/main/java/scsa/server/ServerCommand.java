package scsa.server;

public class ServerCommand {
	
	protected UsersStore users = null;
	
	public ServerCommand(UsersStore users) {
		this.users = users;
	}
	
	public String process(String[] params) {
		return null;
	}
	
}
