package scsa.authorisation;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class Authorisation {

	private Key publicKey = null;
	private Key privateKey = null;
	
	public Authorisation() {
		//
	}
	
	public Key getPrivateKey() {
		return privateKey;
	}
	
	public Key getPublicKey() {
		return publicKey;
	}
	
	public void generateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048 * 2);
			KeyPair kp = kpg.genKeyPair();
			
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
}
