package scsa.server.commands;

import scsa.server.ServerCommand;
import scsa.server.UsersStore;

public class AddUserCommand extends ServerCommand {

	public AddUserCommand(UsersStore users) {
		super(users);
	}

	public String process(String[] params) {
		
		if ( params.length < 3 )
			return params[0] + ": specify username and password";
		
		String username = params[1];
		String password = params[2];
		
		String result = users.addUser(username, password, "UNLOCKED");
		
		users.save();
		
		return result;
	}
}
