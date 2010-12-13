package scsa.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.math.BigInteger;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.net.SocketFactory;

import scsa.encryption.RSAEncryption;
import scsa.socketWrapper.SocketWrapper;

public class Client {

	   static final int PORT = 7070; 
	   
	   public static void main(String argv[]) throws Exception { 
		   
		  BufferedReader console = 
			   new BufferedReader(new InputStreamReader(System.in));

	      SocketFactory factory = SocketFactory.getDefault(); 
	      Socket socket = factory.createSocket("localhost", PORT);
	      
	      SocketWrapper s = new SocketWrapper(socket);
	      
	      System.out.println("Please, wait for secured session initialization...");
	      
	      // authorisation
	      String modulus = s.read();
	      s.write("got");
	      String exponent = s.read();
	      
	      BigInteger m = new BigInteger(modulus);
	      BigInteger e = new BigInteger(exponent);
	      
	      RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
	      KeyFactory fact = KeyFactory.getInstance("RSA");
	      PublicKey publicKey = fact.generatePublic(keySpec);
	      
	      RSAEncryption rsa = new RSAEncryption();
	      
	      String hello = s.read();
	      System.out.println( rsa.decrypt(hello, publicKey) );
	      
	      // input login
	      String fromServer = s.read();
		  System.out.print(rsa.decrypt(fromServer, publicKey));
		  
		  String login = console.readLine();
		  s.write(rsa.encrypt(login, publicKey));
		  
		  // input pass
		  fromServer = s.read();
		  System.out.print(rsa.decrypt(fromServer, publicKey));
		  
		  String pass = console.readLine();
		  s.write(rsa.encrypt(pass, publicKey));
		  
		  String tmp0 = s.read();
		  fromServer = rsa.decrypt(tmp0, publicKey);
		  
		  if ( fromServer.indexOf("successfully") == -1 ) {
			  System.out.println("Login failed: " + fromServer);
			  return;
		  }
		  s.write(rsa.encrypt("ok", publicKey));
		  
	      while (true) {
	    	  String tmp = s.read();
	    	  fromServer = rsa.decrypt(tmp, publicKey);
	    	  
	    	  if ( fromServer.equals("[quit]") ) 
				  return;
	    	  
			  System.out.print(fromServer + "\n> ");

			  String str = console.readLine();
			  s.write(rsa.encrypt(str, publicKey));
			  
	      }
	   }
}