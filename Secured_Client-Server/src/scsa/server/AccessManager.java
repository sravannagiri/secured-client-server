package scsa.server;

import java.util.ArrayList;
import java.util.HashMap;

public class AccessManager {

	private UsersStore users = null;
	private HashMap<String, ArrayList<String>> allowedCommands = null;
	
	public AccessManager(UsersStore users) {
		this.users = users;
		
		this.allowedCommands = new HashMap<String, ArrayList<String>>();
		
		this.addAllowedCommand("ADMIN", "userslist");
	}
	
//	private void loadPermissions(String filename) {
//		//
//	}
	
//	private void savePermissions(String filename) {
//		//
//	}
	
	
	private void addUserRole(String userRole) {
		allowedCommands.put(userRole, new ArrayList<String>());
	}
	
	public void addAllowedCommand(String userRole, String command) {
		if ( allowedCommands.get(userRole) == null ) {
			addUserRole(userRole);
			addAllowedCommand(userRole, command);
		} else {
			allowedCommands.get(userRole).add(command);
		}
	}
	
	public boolean isCommandAllowed(String userName, String command) {
		if ( users.getUserByName(userName) != null ) {
			User user = users.getUserByName(userName);
			String role = user.getUserRole();
			
			if ( allowedCommands.get(role) != null ) {
				
				return allowedCommands.get(role).contains(command);
				
			} else {
				return false;
			}
			
		} else {
			return false;
		}
	}
	
	public boolean isPasswordCorrect(String user, String pass) {
		
		if ( users.getUserByName(user) != null ) {
			
			User u = users.getUserByName(user);
			return u.checkUserPass(pass);
			
		} else {
			return false;
		}
	}
	
	public boolean isAccountUnlocked(String user) {
		if ( users.getUserByName(user) != null ) {
			
			User u = users.getUserByName(user);
			return ! u.getUserStatus().equals("LOCKED");
			
		} else {
			return false;
		}
	}
	
	public void lockUserAccount(String user) {
		if ( users.getUserByName(user) != null ) {
			
			User u = users.getUserByName(user);
			u.setUserStatus("LOCKED");
			
		}
	}
}
