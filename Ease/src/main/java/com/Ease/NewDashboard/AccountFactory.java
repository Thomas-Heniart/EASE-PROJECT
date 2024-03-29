package com.Ease.NewDashboard;

import com.Ease.Hibernate.HibernateQuery;
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

    public Account createAccountFromMap(Map<String, String> account_information, String symmetric_key, Integer reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
        Account account = new Account(reminder_interval, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
        hibernateQuery.saveOrUpdateObject(account);
        for (Map.Entry<String, String> entry : account_information.entrySet()) {
            AccountInformation accountInformation = new AccountInformation(entry.getKey(), RSA.Encrypt(entry.getValue(), public_and_private_key.getKey()), entry.getValue(), account);
            hibernateQuery.saveOrUpdateObject(accountInformation);
            accountInformationSet.add(accountInformation);
        }
        account.setAccountInformationSet(accountInformationSet);
        return account;
    }

    public Account createAccountFromAccount(Account account, HibernateQuery hibernateQuery) throws HttpServletException {
        if (account == null)
            return null;
        Set<AccountInformation> accountToCopyInformationSet = account.getAccountInformationSet();
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Account new_account = new Account(account.getReminder_interval(), account.getPublic_key(), account.getPrivate_key(), accountInformationSet, account.getDeciphered_private_key());
        hibernateQuery.saveOrUpdateObject(new_account);
        for (AccountInformation accountInformation : accountToCopyInformationSet) {
            AccountInformation accountInformation1 = new AccountInformation(accountInformation.getInformation_name(), accountInformation.getInformation_value(), accountInformation.getDeciphered_information_value(), new_account);
            hibernateQuery.saveOrUpdateObject(accountInformation1);
            accountInformationSet.add(accountInformation1);
        }
        new_account.setAccountInformationSet(accountInformationSet);
        return new_account;
    }

    public Account createAccountFromJson(JSONObject account_information, String symmetric_key, Integer reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
        Account account = new Account(reminder_interval, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
        hibernateQuery.saveOrUpdateObject(account);
        for (Object object : account_information.keySet()) {
            String key = String.valueOf(object);
            String value = account_information.getString(key);
            AccountInformation accountInformation = new AccountInformation(key, RSA.Encrypt(value, public_and_private_key.getKey()), value, account);
            hibernateQuery.saveOrUpdateObject(accountInformation);
            accountInformationSet.add(accountInformation);
        }
        account.setAccountInformationSet(accountInformationSet);
        return account;
    }

    public Account createAccountFromAccountCopy(Account account, HibernateQuery hibernateQuery) {
        Set<AccountInformation> accountInformationSet = new HashSet<>();
        Account account_copy = new Account(account.getReminder_interval(), account.getPublic_key(), account.getPrivate_key(), accountInformationSet, null);
        hibernateQuery.saveOrUpdateObject(account_copy);
        for (AccountInformation accountInformation : account.getAccountInformationSet()) {
            AccountInformation accountInformation1 = new AccountInformation(accountInformation.getInformation_name(), accountInformation.getInformation_value(), null, account_copy);
            hibernateQuery.saveOrUpdateObject(accountInformation1);
            accountInformationSet.add(accountInformation1);
        }
        account_copy.setAccountInformationSet(accountInformationSet);
        return account_copy;
    }
}
