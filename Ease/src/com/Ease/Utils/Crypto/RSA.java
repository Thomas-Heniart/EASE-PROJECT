package com.Ease.Utils.Crypto;

import com.Ease.Context.Variables;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class RSA {

    public static void GenerateKey() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.genKeyPair();
        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();
    }

    public static Map<String, String> generateKeys(int length) throws HttpServletException {
        try {
            Map<String, String> publicAndPrivateKeys = new HashMap<>();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            for (int i = 0; i < length; i++) {
                KeyPair kp = kpg.genKeyPair();
                PublicKey publicKey = kp.getPublic();
                PrivateKey privateKey = kp.getPrivate();
                publicAndPrivateKeys.put(new Base64().encodeToString(publicKey.getEncoded()), new Base64().encodeToString(privateKey.getEncoded()));
            }
            return publicAndPrivateKeys;
        } catch (NoSuchAlgorithmException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    public static Map.Entry<String, String> generatePublicAndPrivateKey(int keySize) throws HttpServletException {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keySize);
            KeyPair kp = kpg.genKeyPair();
            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();
            return new AbstractMap.SimpleEntry<>(new Base64().encodeToString(publicKey.getEncoded()), new Base64().encodeToString(privateKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public static Map.Entry<String, String> generateKeys() throws HttpServletException {
        return generatePublicAndPrivateKey(1024);
    }

    public static String Decrypt(String cypher, int keyDate) throws HttpServletException {
        String key = getPrivateKey(keyDate);
        return Decrypt(cypher, key);
    }

    private static String getPrivateKey(int date) throws HttpServletException {
        String ligne;
        String key = null;
        try {
            BufferedReader fichier = new BufferedReader(new FileReader(Variables.KEYS_PATH));
            while ((ligne = fichier.readLine()) != null) {
                String[] keyDatas = ligne.split(":");
                if (Integer.parseInt(keyDatas[0].trim()) == date) {
                    key = keyDatas[1];
                }
            }
            fichier.close();
        } catch (NumberFormatException | IOException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
        return key;
    }

    public static String Encrypt(String plain, String publicK) throws HttpServletException {

        byte[] byteKeyPublic = new Base64().decode(publicK);
        try {
            KeyFactory kf;
            kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = null;
            publicKey = kf.generatePublic(new X509EncodedKeySpec(byteKeyPublic));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return Base64.encodeBase64String(cipher.doFinal(plain.getBytes("UTF-8")));
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    public static String Decrypt(String cypher, String privateK) throws HttpServletException {
        try {
            byte[] byteKeyPrivate = new Base64().decode(privateK);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = null;
            privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(byteKeyPrivate));
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(new Base64().decode(cypher)), "UTF-8");
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | NoSuchPaddingException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    public static void printBytes(byte[] toPrint) {
        for (int i = 0; i < toPrint.length; i++) {
            System.out.print(toPrint[i] + "/");
        }
        System.out.println();
    }
}