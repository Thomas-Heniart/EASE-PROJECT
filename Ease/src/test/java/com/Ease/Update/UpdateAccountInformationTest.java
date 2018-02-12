package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateAccountInformationTest {

    static final String NAME = "login";
    static final String VALUE_DECIPHERED = "password";

    @Test
    public void testDecipherInformation() {
        try {
            UpdateAccountInformation updateAccountInformation = new UpdateAccountInformation();
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            String privateKey = publicAndPrivateKey.getValue();
            updateAccountInformation.setName(NAME);
            updateAccountInformation.setValue(RSA.Encrypt(VALUE_DECIPHERED, publicKey));
            updateAccountInformation.decipher(privateKey);
            assertEquals(updateAccountInformation.getDeciphered_value(), VALUE_DECIPHERED);
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }
}
