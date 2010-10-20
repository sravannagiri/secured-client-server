package scsa.server;

import java.util.HashMap;

public class UsersStore {

	private HashMap<String, User> users = null;
	
	public UsersStore() {
		this.users = new HashMap<String, User> ();
		
		users.put("admin", new User("admin", "admin_pass", "ADMIN", "ADMIN"));
		users.put("user1", new User("user1", "1234", "USER", "UNLOCKED"));
	}
	
	public void load(String filename) {
		//
	}
	
	public void save(String filename) {
		//
	}
	
	public User getUserByName(String name) {
		if ( users != null && users.get(name) != null ) {
			return users.get(name);
		} else {
			return null;
		}
	}
	
	public String[] getUsers() {
		String result[] = {};
		result = users.keySet().toArray(result);
		return result;
	}
	
}
