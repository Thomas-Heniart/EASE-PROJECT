package com.Ease.data;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {

	public static void GenerateKey() throws NoSuchAlgorithmException{
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(1024);
		KeyPair kp = kpg.genKeyPair();
		PublicKey publicKey = kp.getPublic();
		PrivateKey privateKey = kp.getPrivate();
		System.out.println(new Base64().encodeToString(publicKey.getEncoded()));
		System.out.println(new Base64().encodeToString(privateKey.getEncoded()));

	}

	public static String Encrypt(String plain, String publicK) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {      

		byte[] byteKeyPublic = new Base64().decode(publicK);

		KeyFactory kf = KeyFactory.getInstance("RSA");

		PublicKey publicKey = null;
		try {

			publicKey = kf.generatePublic(new X509EncodedKeySpec(byteKeyPublic));

		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return Base64.encodeBase64String(cipher.doFinal(plain.getBytes("UTF-8")));

	}

	public static String Decrypt(String cypher, String privateK) throws NoSuchAlgorithmException,
	NoSuchPaddingException, InvalidKeyException,
	IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		
		byte[] byteKeyPrivate = new Base64().decode(privateK);

		KeyFactory kf = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = null;
		try {

			privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(byteKeyPrivate));

		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return new String(cipher.doFinal(new Base64().decode(cypher)),"UTF-8");

	}
	
	public static void printBytes(byte[] toPrint){
		for(int i = 0; i<toPrint.length;i++){
			System.out.print(toPrint[i] + "/");
		}
		System.out.println();
	}

}