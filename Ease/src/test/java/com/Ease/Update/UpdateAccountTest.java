package com.Ease.Update;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UpdateAccountTest {

    private UpdateAccount updateAccount;

    private final static String RIGHT_LOGIN = "login";
    private final static String WRONG_LOGIN = "loginFake";
    private final static String RIGHT_PASSWORD = "password";
    private final static String WRONG_PASSWORD = "passwordFake";

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
    void testPasswordMatchFailIfPasswordsAreNotEquals() {
        UpdateAccountInformation password = mock(UpdateAccountInformation.class);
        when(password.getName()).thenReturn("password");
        when(password.getDeciphered_value()).thenReturn(RIGHT_PASSWORD);
        this.updateAccount.addUpdateAccountInformation(password);
        JSONObject account_information = mock(JSONObject.class);
        when(account_information.optString("password")).thenReturn(WRONG_PASSWORD);
        assertFalse(this.updateAccount.passwordMatch(account_information));
    }

    @Test
    void testPasswordMatchWorkIfPasswordsAreEquals() {
        UpdateAccountInformation password = mock(UpdateAccountInformation.class);
        when(password.getName()).thenReturn("password");
        when(password.getDeciphered_value()).thenReturn(RIGHT_PASSWORD);
        this.updateAccount.addUpdateAccountInformation(password);
        JSONObject account_information = mock(JSONObject.class);
        when(account_information.optString("password")).thenReturn(RIGHT_PASSWORD);
        assertTrue(this.updateAccount.passwordMatch(account_information));
    }

    @Test
    void testPasswordMatchFailIfThereIsNoPassword() {
        JSONObject account_information = mock(JSONObject.class);
        assertFalse(this.updateAccount.passwordMatch(account_information));
        UpdateAccountInformation updateAccountInformation = mock(UpdateAccountInformation.class);
        when(updateAccountInformation.getName()).thenReturn("login");
        when(updateAccountInformation.getDeciphered_value()).thenReturn(RIGHT_LOGIN);
        this.updateAccount.addUpdateAccountInformation(updateAccountInformation);
        when(account_information.optString("login")).thenReturn(RIGHT_LOGIN);
        assertFalse(this.updateAccount.passwordMatch(account_information));
    }

    @Test
    void testMatchFailIfThereIsNothing() {
        JSONObject account_information = mock(JSONObject.class);
        assertFalse(this.updateAccount.match(account_information));
    }

    @Test
    void testMatchWorkIfAllFieldsAreEquals() {
        JSONObject account_information = mock(JSONObject.class);
        UpdateAccountInformation login = mock(UpdateAccountInformation.class);
        when(login.getName()).thenReturn("login");
        when(login.getDeciphered_value()).thenReturn(RIGHT_LOGIN);
        this.updateAccount.addUpdateAccountInformation(login);
        when(account_information.length()).thenReturn(1);
        when(account_information.optString("login")).thenReturn(RIGHT_LOGIN);
        assertTrue(this.updateAccount.match(account_information));
    }

    @Test
    void testMatchWorkIfAllFieldsAreEqualsExceptPassword() {
        JSONObject account_information = mock(JSONObject.class);
        UpdateAccountInformation login = mock(UpdateAccountInformation.class);
        when(login.getName()).thenReturn("login");
        when(login.getDeciphered_value()).thenReturn(RIGHT_LOGIN);
        this.updateAccount.addUpdateAccountInformation(login);
        UpdateAccountInformation password = mock(UpdateAccountInformation.class);
        when(password.getName()).thenReturn("password");
        when(password.getDeciphered_value()).thenReturn(RIGHT_PASSWORD);
        this.updateAccount.addUpdateAccountInformation(password);
        when(account_information.length()).thenReturn(2);
        when(account_information.optString("login")).thenReturn(RIGHT_LOGIN);
        when(account_information.optString("password")).thenReturn(WRONG_PASSWORD);
        assertTrue(this.updateAccount.match(account_information));
    }

    @Test
    void testMatchFailIfAtLeastOneFieldNotEqualsExceptPassword() {
        JSONObject account_information = mock(JSONObject.class);
        UpdateAccountInformation login = mock(UpdateAccountInformation.class);
        when(login.getName()).thenReturn("login");
        when(login.getDeciphered_value()).thenReturn(RIGHT_LOGIN);
        this.updateAccount.addUpdateAccountInformation(login);
        when(account_information.length()).thenReturn(1);
        when(account_information.optString("login")).thenReturn(WRONG_LOGIN);
        assertFalse(this.updateAccount.match(account_information));
    }
}
