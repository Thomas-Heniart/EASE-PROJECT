package com.Ease.Utils.Crypto;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONObject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;

public class AESUtils {
    private static final int KEY_SIZE = 128;
    private static final int ITERATIONS = 10000;

    private Cipher cipher;

    public AESUtils() {
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    public SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), ITERATIONS, KEY_SIZE);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw fail(e);
        }
    }

    public JSONObject encrypt(SecretKey secretKey, String plaintext) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            AlgorithmParameters parameters = cipher.getParameters();
            byte[] iv = parameters.getParameterSpec(IvParameterSpec.class).getIV();
            byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8"));
            JSONObject res = new JSONObject();
            res.put("iv", hex(iv));
            res.put("msg", base64(cipherText));
            return res;
        } catch (UnsupportedEncodingException | InvalidParameterSpecException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw fail(e);
        }
    }

    public String encrypt(SecretKey secretKey, String iv, String plaintext) {
        byte[] cipherText = doFinal(Cipher.ENCRYPT_MODE, secretKey, iv, base64(plaintext));
        return base64(cipherText);
    }

    public String decrypt(SecretKey secretKey, String iv, String ciphertext) {
        try {
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, secretKey, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            throw fail(e);
        }
    }


    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }

    public static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    public static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    public static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        e.printStackTrace();
        return new IllegalStateException(e);
    }
}
