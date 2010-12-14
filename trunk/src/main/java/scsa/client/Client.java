package scsa.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.SocketFactory;

import scsa.encryption.RSAEncryption;
import scsa.socketWrapper.SocketWrapper;

public class Client {
	
		private PublicKey serverPublicKey = null;
		private RSAEncryption rsa = null;
		private SocketWrapper s = null;
		
		public boolean connectAndHandshake(String host, int port) 
			throws 	UnknownHostException, 
				IOException, 
				NoSuchAlgorithmException, 
				NoSuchPaddingException, 
				InvalidKeySpecException, 
				InvalidKeyException, 
				IllegalBlockSizeException, 
				BadPaddingException 
		{
			
			 SocketFactory factory = SocketFactory.getDefault(); 
		     Socket socket = factory.createSocket(host, port);
		      
		     this.s = new SocketWrapper(socket);
			
		     System.out.println("Please, wait for secured session initialization...");
		     
		     // authorisation
		     String modulus = s.read();
		     s.write("got");
		     String exponent = s.read();
		     
		     BigInteger m = new BigInteger(modulus);
		     BigInteger e = new BigInteger(exponent);
		     
		     RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		     KeyFactory fact = KeyFactory.getInstance("RSA");
		     this.serverPublicKey = fact.generatePublic(keySpec);
		     
		     this.rsa = new RSAEncryption();
		     
		     String hello1 = s.read();
		     String hello2 = rsa.decrypt(hello1, this.serverPublicKey);
		     //System.out.println( rsa.decrypt(hello2, this.serverPublicKey) );
		     
		     if ( ! "[ENCRYPTED HELLO]".equals(hello2) )
		    	 return false;
		     else
		    	 return true;
		}
		
		public boolean login(String username, String password) 
			throws InvalidKeyException, 
				IllegalBlockSizeException, 
				BadPaddingException, 
				UnsupportedEncodingException 
		{
			
		      // input login
		      String fromServer = s.read();
			  //System.out.print(rsa.decrypt(fromServer, this.serverPublicKey));
			  
			  s.write(rsa.encrypt(username, this.serverPublicKey));
			  
			  // input pass
			  fromServer = s.read();
			  //System.out.print(rsa.decrypt(fromServer, this.serverPublicKey));
			  
			  s.write(rsa.encrypt(password, this.serverPublicKey));
			  
			  String tmp0 = s.read();
			  fromServer = rsa.decrypt(tmp0, this.serverPublicKey);
			  
			  if ( fromServer.indexOf("successfully") == -1 ) {
				  System.out.println("Login failed: " + fromServer);
				  return false;
			  }
			  s.write(rsa.encrypt("ok", this.serverPublicKey));
			
			  String tmp1 = s.read();
	    	  fromServer = rsa.decrypt(tmp1, this.serverPublicKey);
			  if ( ! fromServer.equals("ok") ) {
				  System.out.println("Server response is not OK - quit");
				  return false;
			  }
			  
			  return true;
		}

		public String command(String str) 
			throws InvalidKeyException, 
				IllegalBlockSizeException, 
				BadPaddingException, 
				UnsupportedEncodingException 
		{
			  s.write(rsa.encrypt(str, this.serverPublicKey));
			  
			  String tmp = s.read();
	    	  String fromServer = rsa.decrypt(tmp, this.serverPublicKey);
	    	  
	    	  return fromServer;
		}
		
	   static final int PORT = 7070; 
	   
	   public static void main(String argv[]) { 
		   
		  BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		  
		  try {
		  
			  Client c = new Client();
			  if ( c.connectAndHandshake("localhost", 7070) ) {
				  System.out.print("login: ");
				  String login = console.readLine();
				  System.out.print("password: ");
				  String pass = console.readLine();
				  
				  if ( c.login(login, pass) ) {
					  
					  String input = null, output = null;
					  
					  while ( ! "[quit]".equals(output) ) {
						  System.out.print("> ");
						  input = console.readLine();
						  output = c.command(input);
						  System.out.println(output);
					  }
				  } else {
					  System.out.println("Login failed");
				  }
				  
			  } else {
				  System.out.println("Connection failed");
			  }
		  
		  } catch (Exception e) {
			  System.out.println("[ERROR] " + e.getMessage());
			  e.printStackTrace();
		  }
		  
	   }
}