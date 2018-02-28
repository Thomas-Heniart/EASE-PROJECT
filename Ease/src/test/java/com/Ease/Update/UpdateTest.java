package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateTest {
    private Update update;

    @BeforeEach
    void init() {
        this.update = new Update();
    }

    @Test
    void testDecipherUpdate() {
        try {
            UpdateAccount updateAccount = mock(UpdateAccount.class);
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String privateKey = publicAndPrivateKey.getValue();
            this.update.setUpdateAccount(updateAccount);
            this.update.decipher(privateKey);
            verify(updateAccount).decipher(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEditUpdate() {
        try {
            UpdateAccount updateAccount = mock(UpdateAccount.class);
            JSONObject account_information = mock(JSONObject.class);
            this.update.setUpdateAccount(updateAccount);
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            this.update.edit(account_information, publicKey);
            verify(updateAccount).edit(account_information, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
