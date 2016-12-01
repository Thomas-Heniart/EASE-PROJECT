package com.Ease.Utils.Crypto;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
	/*public static void main(String[] args){
			
		FileReader fr;
		try {
			fr = new FileReader("C:/Users/FelixPro/Documents/privateKeys.txt");

		BufferedReader br = new BufferedReader(fr); 
		String s; 

			while((s = br.readLine()) != null) { 
			System.out.println(s); 
			}

			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
			//GenerateKey();
			/*String privateK = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAK52yu3Usv6uplfTq3vVSzLAX0Mt9+WfddaCjqoqEugaMZBMu/uczFDeFrzYlbzEGHD0IWB8AHVtEpYDt5wrBmNhzlDM+9o4pzmhrSj2CuQoc4ZQQOSyTcL1TYTUSU9imAZOIaiQeG9At5wsriBagVE017p8/Zq0qbFjMV522S21AgMBAAECgYA2gYCW2zvs2622hLfvoUy0F2vrtiHbyHztPq7JtQlhIEXZ2k9kpbEjlq8t4tCtP+qO54bB+Ru/lAsZeSHVFZAR2n3wQ6qoyPiSEU1gD2kWA9eTI6QvydypYMwfOG+r3/sQnCE3NRXw8mO4tIPBmHroo49EsOd2DHD9yZfOe9YRhQJBANXSATz0JDmG9prU0ZKnSD/O239cMUQzoIxsEGq2cigiotoPZq2n69+eYR9XvLSwiAq/1H+ffVIrX1JgRjNNZkcCQQDQ4UhA1jSoObFK8ZSbGdK8TM78D/TCB4oUSpFTMc/EVJkdM8lNg4GpiGtMZady5ZbiQIFTUwAKkkwulEEECT4jAkB2ZJHK7mQgdlqV4MEqMvYOXrurCaE94lhDaJKug9cx4fvKQjzYauJwL4IXmL0kT5sEWLOQ9v6tQNbHBgfY4EKBAkBHcCUT966+siyOoxzeBvDp8aMA1jwxd/6jffVB7NFQJlbPg/yfBBD+eGeqE0I3q4n1C3avlr124B4p+A5cYQTdAkEAtSbgmAMN9g7bfUVhREkMLx4fTqZj3rkJCLSWSOsOKoxnkge5IpfM3mvabFvIDsbwOkkW4Jiv4reWh+0AsURVuA==";

				try {
					System.out.println(Decrypt("YMYD0f9sNG9ylmurVHQ0PM1qF0HbknjV8T9wP/rr0hRpoXi8g1ZQFNFY18nxpnZmZXtYdrQn1Hv0rcXiIyOZz10wpMr2KQH/QH14q18AVjNxHTsjfrvLhQ3AucduCBGd3JuhCb1Uwcvdq+FnGF+Lgjl9qBYXCEh+n6+DsB/FXGw=", privateK));
				} catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
						| IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		

	}*/

}