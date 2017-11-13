package com.Ease.NewDashboard;

import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "lastUpdateDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date last_update;

    @Column(name = "reminderIntervalValue")
    private Integer reminder_interval = 0;

    @Column(name = "publicKey")
    private String public_key;

    @Column(name = "privateKey")
    private String private_key;

    @Column(name = "mustBeReciphered")
    private boolean must_be_reciphered = false;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<AccountInformation> accountInformationSet = ConcurrentHashMap.newKeySet();

    @Transient
    private String deciphered_private_key;

    public Account() {

    }

    public Account(Integer reminder_interval, String public_key, String private_key, Set<AccountInformation> accountInformationSet, String deciphered_private_key) {
        this.reminder_interval = reminder_interval;
        this.public_key = public_key;
        this.private_key = private_key;
        this.accountInformationSet = accountInformationSet;
        this.deciphered_private_key = deciphered_private_key;
        this.last_update = new Date();
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public Integer getReminder_interval() {
        return reminder_interval;
    }

    public void setReminder_interval(Integer reminder_interval) {
        this.reminder_interval = reminder_interval;
    }

    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public boolean isMust_be_reciphered() {
        return must_be_reciphered;
    }

    public void setMust_be_reciphered(boolean must_be_reciphered) {
        this.must_be_reciphered = must_be_reciphered;
    }

    public String getDeciphered_private_key() {
        return deciphered_private_key;
    }

    public void setDeciphered_private_key(String deciphered_private_key) {
        this.deciphered_private_key = deciphered_private_key;
    }

    public Set<AccountInformation> getAccountInformationSet() {
        return accountInformationSet;
    }

    public void setAccountInformationSet(Set<AccountInformation> accountInformationSet) {
        this.accountInformationSet = accountInformationSet;
    }

    /**
     * This method is used to decipher the account
     * For example: after user connection
     *
     * @param symmetric_key the user or team symmetric key to decipher private_key from the database
     * @throws HttpServletException if a decryption error occurred
     */
    public void decipher(String symmetric_key) throws HttpServletException {
        this.setDeciphered_private_key(AES.decrypt(this.getPrivate_key(), symmetric_key));
        for (AccountInformation accountInformation : this.getAccountInformationSet())
            accountInformation.decipher(this.getDeciphered_private_key());
    }

    /**
     * This method update public and private key and encrypt all account information in the database with the new public key
     * For example: when a user update his password
     *
     * @param public_key    the new public key
     * @param private_key   the new private key
     * @param symmetric_key the user symmetric key to cipher private_key from the database
     * @throws HttpServletException if an encryption error occurred
     */
    public void setKeys(String public_key, String private_key, String symmetric_key) throws HttpServletException {
        try {
            this.setPublic_key(public_key);
            this.setPrivate_key(AES.encrypt(private_key, symmetric_key));
            this.setDeciphered_private_key(private_key);
            for (AccountInformation accountInformation : this.getAccountInformationSet())
                accountInformation.setInformation_value(RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_key));
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    /**
     * @return a JSONObject of account information with information name as key and deciphered information value as value
     */
    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        this.getAccountInformationSet().forEach(accountInformation -> res.put(accountInformation.getInformation_name(), accountInformation.getDeciphered_information_value()));
        return res;
    }

    public JSONObject getCipheredJson(String public_key) throws HttpServletException {
        try {
            JSONObject res = new JSONObject();
            for (AccountInformation accountInformation : this.getAccountInformationSet())
                res.put(accountInformation.getInformation_name(), RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_key));
            return res;
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    /**
     * @return a JSONObject of account information with information name as key and deciphered information value as value except for the password
     */
    public JSONObject getJsonWithoutPassword() {
        JSONObject res = new JSONObject();
        this.getAccountInformationSet().forEach(accountInformation -> res.put(accountInformation.getInformation_name(), accountInformation.getInformation_name().equals("password") ? "" : accountInformation.getDeciphered_information_value()));
        return res;
    }

    public void edit(JSONObject account_information) throws HttpServletException {
        try {
            for (AccountInformation accountInformation : this.getAccountInformationSet()) {
                String value = (String) account_information.get(accountInformation.getInformation_name());
                if (value == null || value.equals(""))
                    continue;
                if (accountInformation.getInformation_name().equals("password") && !accountInformation.getInformation_value().equals(value))
                    this.setLast_update(new Date());
                accountInformation.setInformation_value(RSA.Encrypt(value, this.getPublic_key()));
                accountInformation.setDeciphered_information_value(value);
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return db_id.equals(account.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public String getInformationNamed(String information_name) throws HttpServletException {
        AccountInformation information = this.getAccountInformationSet().stream().filter(accountInformation -> accountInformation.getInformation_name().equals(information_name)).findFirst().orElse(null);
        if (information == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No information with this name");
        return information.getDeciphered_information_value();
    }
}