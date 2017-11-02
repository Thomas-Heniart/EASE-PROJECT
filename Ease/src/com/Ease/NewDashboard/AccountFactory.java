package com.Ease.NewDashboard;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;

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
        try {
            Set<AccountInformation> accountInformationSet = new HashSet<>();
            Map.Entry<String, String> public_and_private_key = RSA.generateKeys();
            for (Map.Entry<String, String> entry : account_information.entrySet())
                accountInformationSet.add(new AccountInformation(entry.getKey(), RSA.Encrypt(entry.getValue(), public_and_private_key.getKey()), entry.getValue()));
            Account account = new Account(reminder_interval, public_and_private_key.getKey(), AES.encrypt(public_and_private_key.getValue(), symmetric_key), accountInformationSet, public_and_private_key.getValue());
            accountInformationSet.stream().forEach(accountInformation -> accountInformation.setAccount(account));
            return account;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
