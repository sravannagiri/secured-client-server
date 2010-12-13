package scsa.server;

import java.net.Socket;
import java.security.KeyFactory;
import java.security.spec.RSAPublicKeySpec;

import scsa.authorisation.Authorisation;
import scsa.encryption.RSAEncryption;
import scsa.socketWrapper.SocketWrapper;

class ProcessConnection extends Thread {
	
	   Socket clientSocket;
	   SocketWrapper socketWrapper;
	   
	   CommandProcessor commandProcessor = null;
	   UsersStore usersStore = null;
	   AccessManager accessManager = null;	   
	   
	   public ProcessConnection(Socket s,
			   CommandProcessor commandProcessor,
			   AccessManager accessManager,
			   UsersStore usersStore) {
	      this.clientSocket = s;
	      this.socketWrapper = new SocketWrapper(clientSocket);
	      this.start();
	      
	      this.commandProcessor = commandProcessor;
	      this.usersStore = usersStore;
	      this.accessManager = accessManager;
	   }
	 
	   public void run() {
		   
		   boolean loginSuccessfull = false;
		   int loginAttempts = 0;
		   
		   try {
			   
			   Authorisation a = new Authorisation();
			   a.generateKeys();
			   
			   KeyFactory fact = KeyFactory.getInstance("RSA");
			   RSAPublicKeySpec pub = fact.getKeySpec(a.getPublicKey(), 
					   RSAPublicKeySpec.class);
			   
			   socketWrapper.write( pub.getModulus().toString() );
			   if ( ! socketWrapper.read().equals("got") ) return;
			   socketWrapper.write( pub.getPublicExponent().toString() );
			   
			   RSAEncryption rsa = new RSAEncryption();
			   String msg = rsa.encrypt("[ENCRYPTED HELLO]", a.getPrivateKey());
			   socketWrapper.write(msg);
			   
			   String user = null, pass = null;
			   
			   socketWrapper.write(rsa.encrypt(
					   "login as: ", a.getPrivateKey()));
			   user = rsa.decrypt(socketWrapper.read(), a.getPrivateKey());
			   System.out.println("[FROM CLIENT] " + user);
			   
			   while ( ! loginSuccessfull && loginAttempts < 3 ) {
   				   
				   if ( loginAttempts > 0 )
					   socketWrapper.write(rsa.encrypt(
							   "login failed.\npassword: ", a.getPrivateKey()));
				   else
					   socketWrapper.write(rsa.encrypt(
							   "password: ", a.getPrivateKey()));
				   
				   pass = rsa.decrypt(socketWrapper.read(), a.getPrivateKey());
				   System.out.println("[FROM CLIENT] " + pass);
				   
				   if ( accessManager.isPasswordCorrect(user, pass) ) {
					   loginSuccessfull = true;
					   break;
				   } else {
					   loginAttempts ++;
				   }
			   }
			   
			   if ( ! loginSuccessfull ) {
				   if ( ! user.equals("admin") ) 
					   		accessManager.lockUserAccount(user);
				   
				   socketWrapper.write(rsa.encrypt(
						   "maximum login attempts exeeded. Account is locked now. Disconnect\n", a.getPrivateKey()));
				   clientSocket.close();
				   return;
			   } else {
				   if ( ! accessManager.isAccountUnlocked( user ) ) {
					   socketWrapper.write(rsa.encrypt(
							   "account locked. Contact administrator\n", a.getPrivateKey()));
					   clientSocket.close();
					   return;
				   }
			   }
			   
			   socketWrapper.write(rsa.encrypt(
					   "logged in as \"" + user + "\" successfully\n", a.getPrivateKey()));
			   
			   while (true) {
				   String userInput = rsa.decrypt(socketWrapper.read(), a.getPrivateKey());
				   
				   if (userInput.equals("quit")) {
					   System.out.println("[GOT \"quit\" FROM USER]");
					   socketWrapper.write(rsa.encrypt(
							   "[quit]", a.getPrivateKey()));
					   break;
				   }
				   
				   socketWrapper.write(
						   rsa.encrypt(
								   commandProcessor.processCommand(user, userInput), 
								   a.getPrivateKey()));
			   }
		   
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
	   }  
}
