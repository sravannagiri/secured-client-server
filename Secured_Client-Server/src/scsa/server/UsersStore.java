package scsa.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class UsersStore {

	private HashMap<String, User> users = null;
	
	private String filename = null;
	
	public UsersStore(String filename) {
//		this.users = new HashMap<String, User> ();
//		
//		users.put("admin", new User("admin", "admin_pass", "ADMIN", "ADMIN"));
//		users.put("user1", new User("user1", "1234", "USER", "UNLOCKED"));
		
		this.filename = filename;
		
		try {
			this.load();
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println("[ERROR] Loading users store failed");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		File infile = new File(this.filename);
		if ( ! infile.exists() ) {
			this.users = new HashMap<String, User> ();
			this.users.put("admin", 
					new User("admin", "admin_pass", "ADMIN", "ADMIN"));
			
			throw new IOException("Input file not found");
		} else {
			FileInputStream fis = new FileInputStream(infile);
			ObjectInputStream ois = new ObjectInputStream( fis );
			
			HashMap<String, User> users = 
				(HashMap<String, User>) ois.readObject();
			
			if ( users != null ) {
				this.users = users;
			} else {
				this.users = new HashMap<String, User> ();
				this.users.put("admin", 
						new User("admin", "admin_pass", "ADMIN", "ADMIN"));
			}
		}
	}
	
	public void save() {
		try {
			File outfile = new File(this.filename);
			if ( ! outfile.exists() )
				outfile.createNewFile();
			
			FileOutputStream fos = new FileOutputStream(outfile);
			ObjectOutputStream oos = new ObjectOutputStream( fos );
			
			oos.writeObject(this.users);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			System.out.println("Error during savid users store: " + 
					e.getMessage());
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
		User user = new User(username, password, "USER", status);
		if ( ! users.containsKey(username) ) {
			users.put(username, user);
			return "User added succesfully";
		} else {
			return "User with specified username already exists.";
		}
	}
	
}
