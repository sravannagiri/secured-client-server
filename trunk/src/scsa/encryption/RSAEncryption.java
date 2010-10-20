package scsa.encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import scsa.authorisation.Authorisation;

public class RSAEncryption {
	
	private Cipher cipher = null;
	
	public RSAEncryption() throws NoSuchAlgorithmException, NoSuchPaddingException {
		cipher = Cipher.getInstance("RSA");
	}
	
	public String encrypt(String message, Key key) 
		throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		byte[] data = message.getBytes("ISO-8859-1");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] result = cipher.doFinal(data);
		return new String(result, "ISO-8859-1");
	}
	
	public String decrypt(String message, Key key) 
		throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

		byte[] data = message.getBytes("ISO-8859-1");
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] result = cipher.doFinal(data);
		return new String(result, "ISO-8859-1");
	}

	
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, NoSuchPaddingException {
		
	   Authorisation a = new Authorisation();
	   a.generateKeys();
	   
	   KeyFactory fact = KeyFactory.getInstance("RSA");
	   RSAPublicKeySpec pub = fact.getKeySpec(a.getPublicKey(), 
			   RSAPublicKeySpec.class);
	   
	   pub.getModulus();
	   pub.getPublicExponent();
	   
	   //BigInteger i = new BigInteger();
	   
	   RSAEncryption rsa = new RSAEncryption();
	   
	   String s = rsa.encrypt("test message111", a.getPrivateKey());
	   System.out.println(s.length());
	   
	   byte[] b = s.getBytes("ISO-8859-1");
	   String s2 = new String(b, "ISO-8859-1");
	   
	   System.out.println( rsa.decrypt(s2, a.getPublicKey()) );
	}
}
