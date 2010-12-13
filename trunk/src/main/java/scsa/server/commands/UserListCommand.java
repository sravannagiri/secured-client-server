package scsa.server.commands;

import scsa.server.ServerCommand;
import scsa.server.UsersStore;

public class UserListCommand extends ServerCommand {
	
	public UserListCommand(UsersStore users) {
		super(users);
	}
	
	public String process(String[] params) {
		String result = "";
		String usersArray[] = users.getUsers();
		result += "USERS:\n";
		for (int i=0; i<usersArray.length; i++)
			result += usersArray[i] + "\n";
		
		return result;
	}

}
