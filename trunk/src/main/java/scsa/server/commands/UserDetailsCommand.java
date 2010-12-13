package scsa.server.commands;

import scsa.server.ServerCommand;
import scsa.server.UsersStore;

public class UserDetailsCommand extends ServerCommand {

	public UserDetailsCommand(UsersStore users) {
		super(users);
	}

	public String process(String[] params) {
		
		if ( params.length < 2 )
			return params[0] + ": user name not specified";
		
		if ( users.getUserByName(params[1]) != null )
			return "USER: " + params[1] + "\n" +
				"ROLE: " + users.getUserByName(params[1]).getUserRole() + "\n" +
				"STATUS: " + users.getUserByName(params[1]).getUserStatus() + "\n";
		else
			return params[0] + ": no such user";
	}

}
