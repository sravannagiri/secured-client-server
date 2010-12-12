package scsa.server;

public abstract class ServerCommand {
	
	protected UsersStore users = null;
	
	public ServerCommand(UsersStore users) {
		this.users = users;
	}
	
	public abstract String process(String[] params);
	
}
