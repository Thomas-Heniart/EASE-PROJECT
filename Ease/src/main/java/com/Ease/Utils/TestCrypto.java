package com.Ease.Utils;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;

public class TestCrypto {
    public static void main() {
        StringBuilder val = new StringBuilder("");
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024);
            KeyPair kp = kpg.genKeyPair();
            PublicKey publicKey = kp.getPublic();
            PrivateKey privateKey = kp.getPrivate();
            System.out.println(((RSAPrivateKey) privateKey).getModulus().bitLength());

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] data = cipher.doFinal(val.toString().getBytes("UTF-8"));
            System.out.println(data.length * 8);
            System.out.println(data.length * 8 < ((RSAPrivateKey) privateKey).getModulus().bitLength());
        } catch (Exception e) {

        }
    }
}
