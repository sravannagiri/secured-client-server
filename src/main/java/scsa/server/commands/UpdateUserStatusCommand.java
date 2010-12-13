package scsa.server.commands;

import scsa.server.ServerCommand;
import scsa.server.UsersStore;
import scsa.server.User;

public class UpdateUserStatusCommand extends ServerCommand {

	public UpdateUserStatusCommand(UsersStore users) {
		super(users);
	}

	public String process(String[] params) {
		if (params.length < 3)
			return params[0] + ": specify username and status";
		
		if ( users.getUserByName(params[1]) != null ) {
			User user = users.getUserByName(params[1]);
			
			if ( params[2].equals("locked") || params[2].equals("unlocked") ) {
				user.setUserStatus(params[2].toUpperCase());
				users.save();
			} else {
				return "invalid user status specified";
			}
			
			return "user status updated successfull";
		} else {
			return "user does not exist";
		}
	}

}
