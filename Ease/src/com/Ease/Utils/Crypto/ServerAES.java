package com.Ease.Utils.Crypto;

import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.crypto.SecretKey;

public class ServerAES {

    private SecretKey secretKey;
    private String salt;
    private String passphrase;
    private AESUtils aesUtils = new AESUtils();

    public ServerAES() {
        this.salt = AESUtils.random(8);
        this.passphrase = AESUtils.random(18);
        this.secretKey = aesUtils.generateKey(salt, passphrase);
    }

    public JSONObject cipher(String strToCipher) {
        return aesUtils.encrypt(this.secretKey, strToCipher);

    }

    public String cipher(String strToCipher, String iv) {
        return aesUtils.encrypt(this.secretKey, iv, strToCipher);
    }

    public void cipher(JSONObject jsonObject) {
        jsonObject.forEach((key, value) -> {
            if (value.getClass().getName().equals(String.class.getName()))
                jsonObject.put(key, this.cipher((String) value));
            else if (value.getClass().getName().equals(JSONObject.class.getName()))
                cipher((JSONObject) value);
            else if (value.getClass().getName().equals(JSONArray.class.getName()))
                cipher((JSONArray) value);
        });
    }

    public void cipher(JSONArray jsonArray) {
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value.getClass().getName().equals(String.class.getName()))
                jsonArray.set(i, this.cipher((String) value));
            else if (value.getClass().getName().equals(JSONObject.class.getName()))
                cipher((JSONObject) value);
            else if (value.getClass().getName().equals(JSONArray.class.getName()))
                cipher((JSONArray) value);
        }
    }

    public String decipher(String strToDecipher, String iv) {
        return this.aesUtils.decrypt(this.secretKey, iv, strToDecipher);
    }

    public void decipher(JSONObject jsonObject, String iv) {
        jsonObject.forEach((key, value) -> {
            if (value.getClass().getName().equals(String.class.getName()))
                jsonObject.put(key, this.decipher((String) value, iv));
            else if (value.getClass().getName().equals(JSONObject.class.getName()))
                decipher((JSONObject) value, iv);
            else if (value.getClass().getName().equals(JSONArray.class.getName()))
                decipher((JSONArray) value, iv);
        });
    }

    public void decipher(JSONArray jsonArray, String iv) {
        for (int i = 0; i < jsonArray.size(); i++) {
            Object value = jsonArray.get(i);
            if (value.getClass().getName().equals(String.class.getName()))
                jsonArray.set(i, this.decipher((String) value, iv));
            else if (value.getClass().getName().equals(JSONObject.class.getName()))
                decipher((JSONObject) value, iv);
            else if (value.getClass().getName().equals(JSONArray.class.getName()))
                decipher((JSONArray) value, iv);
        }
    }

    public String getKey() {
        return new String(Hex.encodeHex(secretKey.getEncoded()));
    }

    public String getPassphrase() {
        return passphrase;
    }

    public String getSalt() {
        return salt;
    }
}
