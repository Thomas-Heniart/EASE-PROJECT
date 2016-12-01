package com.Ease.Utils.Crypto;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

 
public class AES {
 
    public static String keyGenerator(){
    	KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
    	keyGen.init(128);
    	SecretKey secretKey = keyGen.generateKey();
    	return new Base64().encodeToString(secretKey.getEncoded());
    }
    
    public static String encrypt(String strToEncrypt, String key) {
        try
        {
        	byte[] bytesKey = new Base64().decode(key);
        	// rebuild key using SecretKeySpec
        	
            SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
         
            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        
        }
        catch (Exception e) {
           
            System.out.println("Error while encrypting: "+e.toString());
        }
        return null;
    }
    
    public static String decrypt(String strToDecrypt, String key) {
        try
        {
        	byte[] bytesKey = new Base64().decode(key);
        	// rebuild key using SecretKeySpec
        	
            SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
           
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)), StandardCharsets.UTF_8);
            
        }
        catch (Exception e) {
         
            System.out.println("Error while decrypting: "+e.toString());
        }
        return null;
    }
 
    public static String encryptUserKey(String plainKey, String easePass, String salt) {   
         
    	try {
    		byte[] saltBytes = salt.getBytes();

    		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    		PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, 128);
    		SecretKey secretKey = skf.generateSecret(spec);
         
    		return encrypt(plainKey, new Base64().encodeToString(secretKey.getEncoded()));
    	} catch (Exception e){
    		System.out.println("Error while encrypting key : " + e.toString());
    	}
    	return null;
        
    }
 
    public static String decryptUserKey(String encryptedKey, String easePass, String salt) {
    	try {
    		byte[] saltBytes = salt.getBytes();

    		SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    		PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, 128);
    		SecretKey secretKey = skf.generateSecret(spec);
        
    		return decrypt(encryptedKey, new Base64().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
        	System.out.println("Error while decrypting key : " + e.toString());
        }
    	return null;
    }
 
	public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return new Base64().encodeToString(bytes);
    }
}