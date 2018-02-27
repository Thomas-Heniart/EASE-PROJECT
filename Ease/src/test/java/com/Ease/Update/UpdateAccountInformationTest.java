package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateAccountInformationTest {

    private static final String NAME = "login";
    private static final String VALUE_DECIPHERED = "password";
    private static final String EDITED_VALUE_DECIPHERED = "password2";

    private UpdateAccountInformation updateAccountInformation;

    @BeforeEach
    void init() {
        this.updateAccountInformation = new UpdateAccountInformation();
    }

    @Test
    void testDecipherInformation() {
        try {
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            String privateKey = publicAndPrivateKey.getValue();
            this.updateAccountInformation.setName(NAME);
            this.updateAccountInformation.setValue(RSA.Encrypt(VALUE_DECIPHERED, publicKey));
            this.updateAccountInformation.decipher(privateKey);
            assertEquals(this.updateAccountInformation.getDeciphered_value(), VALUE_DECIPHERED);
        } catch (HttpServletException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEdit() {
        try {
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            this.updateAccountInformation.setName(NAME);
            this.updateAccountInformation.setValue(RSA.Encrypt(VALUE_DECIPHERED, publicKey));
            this.updateAccountInformation.edit(EDITED_VALUE_DECIPHERED, publicKey);
            assertEquals(this.updateAccountInformation.getDeciphered_value(), EDITED_VALUE_DECIPHERED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
