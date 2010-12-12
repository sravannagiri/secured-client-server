package scsa.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UsersStore {

	private HashMap<String, User> users = null;
	
	private String filename = null;
	
	public UsersStore(String filename) {
		this.filename = filename;
	}
	
	@SuppressWarnings("unchecked")
	public void load() {
		try {
			File infile = new File(this.filename);
			if ( ! infile.exists() ) {
				this.users = new HashMap<String, User> ();
				this.users.put("admin", new User("admin", "admin_pass", "ADMIN", "ADMIN"));
				
				save();
				return;
			}
			
			FileInputStream fis = new FileInputStream(infile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			HashMap<String, User> users = 
				(HashMap<String, User>) ois.readObject();
			
			if ( users != null ) {
				this.users = users;
			} else {
				this.users = new HashMap<String, User> ();
				this.users.put("admin", new User("admin", "admin_pass", "ADMIN", "ADMIN"));
			}
		} catch (Exception e) {
			System.out.println("[ERROR] " + e.getMessage());
		}
	}
	
	public void save() {
		try {
			File outfile = new File(this.filename);
			if ( ! outfile.exists() ) {
				outfile.createNewFile();
			}
			
			FileOutputStream fos = new FileOutputStream(outfile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject( this.users );
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println("[ERROR] " + e.getMessage());
		}
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
	
	public String addUser(String username, String password, String status) {
		if ( ! this.users.containsKey(username) ) {
			this.users.put(username, 
					new User(username, password, "USER", status));
			return "user added";
		} else {
			return "user with specified username already exists";
		}
	}
	
}
