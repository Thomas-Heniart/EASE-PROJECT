package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class UpdateAccountTest {

    private UpdateAccount updateAccount;

    @BeforeEach
    void init() {
        this.updateAccount = new UpdateAccount();
    }

    @Test
    void testAddUpdateAccountInformation() {
        assertEquals(this.updateAccount.getUpdateAccountInformationSet().size(), 0);
        UpdateAccountInformation updateAccountInformation = new UpdateAccountInformation();
        this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
        assertEquals(this.updateAccount.getUpdateAccountInformationSet().size(), 1);
    }

    @Test
    void testRemoveUpdateAccountInformation() {
        assertEquals(this.updateAccount.getUpdateAccountInformationSet().size(), 0);
        UpdateAccountInformation updateAccountInformation = new UpdateAccountInformation();
        this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
        this.updateAccount.removeUpdateAccountInformation(updateAccountInformation);
        assertEquals(this.updateAccount.getUpdateAccountInformationSet().size(), 0);
    }

    @Test
    void testDecipherUpdateAccountInformation() {
        try {
            UpdateAccountInformation updateAccountInformation = mock(UpdateAccountInformation.class);
            //UpdateAccountInformation updateAccountInformation = new UpdateAccountInformation();
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String publicKey = publicAndPrivateKey.getKey();
            String privateKey = publicAndPrivateKey.getValue();
            this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
            this.updateAccount.decipher(privateKey);
            verify(updateAccountInformation).decipher(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
