package com.Ease.Dashboard.App.WebsiteApp.ClassicApp;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class AccountInformation {

    public enum AccountInformationData {
        NOTHING,
        ID,
        ACCOUNT_ID,
        INFORMATION_NAME,
        INFORMATION_VALUE
    }

    public static List<AccountInformation> createAccountInformations(String account_id, Map<String, String> account_informations, String publicKey, ServletManager sm) throws GeneralException {
        List<AccountInformation> informations = new LinkedList<AccountInformation>();
        for (Map.Entry<String, String> entry : account_informations.entrySet()) {
            informations.add(createAccountInformation(account_id, entry.getKey(), entry.getValue(), publicKey, sm));
        }
        return informations;
    }

    public static List<AccountInformation> createSharedAccountInformationList(String account_id, Map<String, String> informationMap, String deciphered_teamKey, ServletManager sm) throws GeneralException {
        List<AccountInformation> informations = new LinkedList<AccountInformation>();
        for (Map.Entry<String, String> entry : informationMap.entrySet()) {
            informations.add(createSharedAccountInformation(account_id, entry.getKey(), entry.getValue(), deciphered_teamKey, sm));
        }
        return informations;
    }

    public static List<AccountInformation> createAccountInformationFromAccountInformations(String account_id, List<AccountInformation> accountInformations, String publicKey, ServletManager sm) throws GeneralException {
        List<AccountInformation> informations = new LinkedList<AccountInformation>();
        for (AccountInformation info : accountInformations)
            informations.add(createAccountInformation(account_id, info.getInformationName(), info.getInformationValue(), publicKey, sm));
        return informations;
    }

    public static AccountInformation createAccountInformation(String account_id, String information_name, String information_value, String publicKey, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("INSERT INTO accountsInformations values (null, ?, ?, ?);");
        request.setInt(account_id);
        request.setString(information_name);
        request.setString(RSA.Encrypt(information_value, publicKey));
        int db_id = request.set();
        return new AccountInformation(String.valueOf(db_id), account_id, information_name, information_value);
    }

    public static AccountInformation createSharedAccountInformation(String account_id, String information_name, String information_value, String deciphered_teamKey, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("INSERT INTO accountsInformations values (null, ?, ?, ?);");
        request.setInt(account_id);
        request.setString(information_name);
        request.setString(AES.encrypt(information_value, deciphered_teamKey));
        int db_id = request.set();
        return new AccountInformation(String.valueOf(db_id), account_id, information_name, information_value);
    }

    public static List<AccountInformation> loadInformations(String account_id, DataBaseConnection db) throws GeneralException {
        List<AccountInformation> account_informations = new LinkedList<AccountInformation>();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM accountsInformations WHERE account_id= ?;");
        request.setInt(account_id);
        DatabaseResult rs = request.get();
        while (rs.next()) {
            String db_id = rs.getString(AccountInformationData.ID.ordinal());
            String information_name = rs.getString(AccountInformationData.INFORMATION_NAME.ordinal());
            String information_value = rs.getString(AccountInformationData.INFORMATION_VALUE.ordinal());
            account_informations.add(new AccountInformation(db_id, account_id, information_name, information_value));
        }
        return account_informations;
    }

    protected String db_id;
    protected String account_id;
    protected String information_name;
    protected String information_value;

    public AccountInformation(String db_id, String account_id, String information_name, String information_value) {
        this.db_id = db_id;
        this.account_id = account_id;
        this.information_name = information_name;
        this.information_value = information_value;
    }

    public String getInformationName() {
        return this.information_name;
    }

    public String getInformationValue() {
        return this.information_value;
    }

    public void setInformation_value(String information_value, String publicKey, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE accountsInformations SET information_value = ? WHERE id = ?;");
        request.setString(RSA.Encrypt(information_value, publicKey));
        request.setInt(this.db_id);
        request.set();
        this.information_value = information_value;
    }

    public void removeFromDb(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("DELETE FROM accountsInformations WHERE id = ?;");
        request.setInt(db_id);
        request.set();
    }

    public void decipher(String privateKey) throws GeneralException {
        this.information_value = RSA.Decrypt(this.information_value, privateKey);
    }

    public void update_ciphering(String publicKey, ServletManager sm) throws GeneralException {
        if (this.information_name.equals("password"))
            this.information_value = sm.getUser().decrypt(this.information_value);
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE accountsInformations SET information_value = ? WHERE id = ?;");
        request.setString(RSA.Encrypt(this.information_value, publicKey));
        request.setString(this.db_id);
        request.set();
    }

    public void decipherAndCipher(String deciphered_teamPrivateKey, String publicKey, ServletManager sm) throws GeneralException {
        this.information_value = RSA.Decrypt(this.information_value, deciphered_teamPrivateKey);
        DatabaseRequest request = sm.getDB().prepareRequest("UPDATE accountsInformations SET information_value = ? WHERE id = ?;");
        request.setString(RSA.Encrypt(this.information_value, publicKey));
        request.setString(this.db_id);
        request.set();
    }
}
