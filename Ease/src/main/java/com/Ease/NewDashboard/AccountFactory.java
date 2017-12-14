package com.Ease.NewDashboard;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AccountFactory {
    private static AccountFactory ourInstance = new AccountFactory();

    public static AccountFactory getInstance() {
        return ourInstance;
    }

    private AccountFactory() {
    }

    public Account createAccountFromMap(Map<String, String> account_information, String symmetric_key, Integer reminder_interval) throws HttpServletException {
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
        for (Map.Entry<String, String> entry : account_information.entrySet())
            accountInformationSet.add(new AccountInformation(entry.getKey(), RSA.Encrypt(entry.getValue(), public_and_private_key.getKey()), entry.getValue()));
        Account account = new Account(reminder_interval, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
        accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(account));
        return account;
    }

    public Account createAccountFromAccount(Account account, String symmetric_key) throws HttpServletException {
        Set<AccountInformation> accountToCopyInformationSet = account.getAccountInformationSet();
        Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        for (AccountInformation accountInformation : accountToCopyInformationSet)
            accountInformationSet.add(new AccountInformation(accountInformation.getInformation_name(), RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_and_private_key.getKey()), accountInformation.getDeciphered_information_value()));
        Account new_account = new Account(account.getReminder_interval(), public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
        accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(new_account));
        return new_account;
    }

    public Account createAccountFromJson(JSONObject account_information, String symmetric_key, Integer reminder_interval) throws HttpServletException {
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
        for (Object object : account_information.keySet()) {
            String key = String.valueOf(object);
            String value = account_information.getString(key);
            accountInformationSet.add(new AccountInformation(key, RSA.Encrypt(value, public_and_private_key.getKey()), value));
        }
        Account account = new Account(reminder_interval, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
        accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(account));
        return account;
    }
}
