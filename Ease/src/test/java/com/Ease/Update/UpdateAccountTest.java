package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            String privateKey = publicAndPrivateKey.getValue();
            this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
            this.updateAccount.decipher(privateKey);
            verify(updateAccountInformation).decipher(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testEditUpdateAccount() {
        try {
            UpdateAccountInformation updateAccountInformation = mock(UpdateAccountInformation.class);
            this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
            when(updateAccountInformation.getName()).thenReturn("login");
            JSONObject account_information = mock(JSONObject.class);
            when(account_information.getString("login")).thenReturn("test");
            String publicKey = RSA.generateKeys().getKey();
            this.updateAccount.edit(account_information, publicKey);
            verify(updateAccountInformation).edit(account_information.getString(updateAccountInformation.getName()), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
