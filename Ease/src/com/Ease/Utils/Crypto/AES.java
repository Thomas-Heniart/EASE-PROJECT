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

import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.apache.tomcat.util.codec.binary.Base64;


public class AES {

    private static final int KEY_SIZE = 128;
    private static final String[] PEPPERS = {
            "rRAwXwaUjL/nGlk7L9RAYRFVqLI=",
            "hf0awq2mJFe1NUzT+KciUL4HRN8=",
            "UQu1FQwf8vldwQjzjzdP9IOFwHY=",
            "bvRJY4LXt7HC5nOyKRaLvXD9PlA=",
            "DlbP/mkKmfgKQxZt/4f0TmMioAk=",
            "X9qsP83Z3LxCqQbzyf/bcwkjCO4=",
            "L19F+V3UeKDD1ZK7oMTJjhklJZE=",
            "c9dWZjJmUBggG0dKVNBLOGhGfdU=",
            "8L+jERqOQakTl8WtnaiR6YMVpvg=",
            "KXxjWw1Ml/Y8y9vEqZvHTSmoJKY=",
            "k+GSXW/HFayjL2YO4BvLiAeCjZ8=",
            "9DwQs2gHBEFjZxgX0YenAchXTG0=",
            "UCFDA1tyZ+Y1yV+CfJnad0MZWYs=",
            "6JHmUWTJ87MsaSKzabclPK9fCZ8=",
            "8bMk9l5zP9inrpNQeNDUnT4Pv+Q=",
            "qg5Vh9xiL2GZtkK7hUPNI61ov3o="
    };

    public static String keyGenerator() {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        keyGen.init(KEY_SIZE);
        SecretKey secretKey = keyGen.generateKey();
        return new Base64().encodeToString(secretKey.getEncoded());
    }

    public static String encrypt(String strToEncrypt, String key) {
        try {
            byte[] bytesKey = new Base64().decode(key);
            // rebuild key using SecretKeySpec

            SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);


            return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (Exception e) {

            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String key) {
        try {
            byte[] bytesKey = new Base64().decode(key);
            // rebuild key using SecretKeySpec

            SecretKeySpec secretKey = new SecretKeySpec(bytesKey, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)), StandardCharsets.UTF_8);

        } catch (Exception e) {

            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public static String encryptUserKey(String plainKey, String easePass, String salt) {

        try {
            byte[] saltBytes = pepperedSalt(salt.getBytes(), easePass);

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, KEY_SIZE);
            SecretKey secretKey = skf.generateSecret(spec);

            return encrypt(plainKey, new Base64().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
            System.out.println("Error while encrypting key : " + e.toString());
        }
        return null;

    }

    public static String cipherKey(String plainKey, String keyUser, String salt) throws GeneralException {
        try {
            byte[] saltBytes = pepperedSalt(salt.getBytes(), keyUser);

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(keyUser.toCharArray(), saltBytes, 65536, KEY_SIZE);
            SecretKey secretKey = skf.generateSecret(spec);

            return encrypt(plainKey, new Base64().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
            throw new GeneralException(ServletManager.Code.InternError, e);
        }
    }

    public static String decryptUserKey(String encryptedKey, String easePass, String salt) {
        try {
            byte[] saltBytes = pepperedSalt(salt.getBytes(), easePass);

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, KEY_SIZE);
            SecretKey secretKey = skf.generateSecret(spec);

            return decrypt(encryptedKey, new Base64().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
            System.out.println("Error while decrypting key : " + e.toString());
        }
        return null;
    }

    public static String oldEncryptUserKey(String plainKey, String easePass, String salt) {

        try {
            byte[] saltBytes = salt.getBytes();

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, KEY_SIZE);
            SecretKey secretKey = skf.generateSecret(spec);

            return encrypt(plainKey, new Base64().encodeToString(secretKey.getEncoded()));
        } catch (Exception e) {
            System.out.println("Error while encrypting key : " + e.toString());
        }
        return null;

    }

    public static String oldDecryptUserKey(String encryptedKey, String easePass, String salt) {
        try {
            byte[] saltBytes = salt.getBytes();

            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(easePass.toCharArray(), saltBytes, 65536, KEY_SIZE);
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

    private static String getPepper(String pass) {
        byte[] passBytes = pass.getBytes();
        byte tmp = 0;
        for (int i = 0; i < passBytes.length; i++) {
            tmp = (byte) (tmp + passBytes[i]);
        }
        int pepperIndex = (tmp + 127) / 16;
        return PEPPERS[pepperIndex];
    }

    private static byte[] pepperedSalt(byte[] saltBytes, String pass) throws Exception {
        byte[] pepperBytes = getPepper(pass).getBytes();
        if (pepperBytes.length != saltBytes.length) {
            throw new Exception("Different sizes between pepper and salt");
        } else {
            for (int i = 0; i < saltBytes.length; i++) {
                saltBytes[i] = (byte) (saltBytes[i] + pepperBytes[i]);
            }
        }
        return saltBytes;
    }
}