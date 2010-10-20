package scsa.server;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4383501546717495387L;
	
	private String userName = null;
	private String userPass = null;
	private String userRole = null;
	private String userStatus = null;
	
	public User(String name, String pass, String role, String status) {
		this.userName = name;
		this.userPass = pass;
		this.userRole = role;
		this.userStatus = status;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public boolean checkUserPass(String pass) {
		return ( this.userPass.equals(pass) );
	}
	
	public String getUserRole() {
		return this.userRole;
	}
	
	public String getUserStatus() {
		return this.userStatus;
	}
	
	public void setUserStatus(String status) {
		this.userStatus = status;
	}
	
	public void setUserPass(String pass) {
		this.userPass = pass;
	}
	
}
