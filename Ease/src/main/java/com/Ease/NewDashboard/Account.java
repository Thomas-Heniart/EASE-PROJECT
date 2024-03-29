package com.Ease.NewDashboard;

import com.Ease.Catalog.Software;
import com.Ease.Catalog.SoftwareConnectionInformation;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteInformation;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;
import com.nulabinc.zxcvbn.Zxcvbn;
import haveibeenpwned.api.RangeAPI;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONObject;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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

    @Column(name = "passwordMustBeUpdated")
    private boolean password_must_be_updated = false;

    @Column(name = "adminNotified")
    private boolean admin_notified = false;

    @Column(name = "lastPasswordReminderDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPasswordReminderDate;

    @Column(name = "strongerPasswordAsked")
    private boolean strongerPasswordAsked = false;

    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public boolean isPassword_must_be_updated() {
        return password_must_be_updated;
    }

    public void setPassword_must_be_updated(boolean password_must_be_updated) {
        this.password_must_be_updated = password_must_be_updated;
    }

    public boolean isAdmin_notified() {
        return admin_notified;
    }

    public void setAdmin_notified(boolean admin_notified) {
        this.admin_notified = admin_notified;
    }

    public Date getLastPasswordReminderDate() {
        return lastPasswordReminderDate;
    }

    public void setLastPasswordReminderDate(Date lastPasswordReminderDate) {
        this.lastPasswordReminderDate = lastPasswordReminderDate;
    }

    public boolean isStrongerPasswordAsked() {
        return strongerPasswordAsked;
    }

    public void setStrongerPasswordAsked(boolean strongerPasswordAsked) {
        this.strongerPasswordAsked = strongerPasswordAsked;
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
        this.setPublic_key(public_key);
        this.setPrivate_key(AES.encrypt(private_key, symmetric_key));
        this.setDeciphered_private_key(private_key);
        for (AccountInformation accountInformation : this.getAccountInformationSet())
            accountInformation.setInformation_value(RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_key));
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
        JSONObject res = new JSONObject();
        for (AccountInformation accountInformation : this.getAccountInformationSet())
            res.put(accountInformation.getInformation_name(), RSA.Encrypt(accountInformation.getDeciphered_information_value(), public_key));
        return res;
    }

    /**
     * @return a JSONObject of account information with information name as key and deciphered information value as value except for the password
     */
    public JSONObject getJsonWithoutPassword() {
        JSONObject res = new JSONObject();
        this.getAccountInformationSet().forEach(accountInformation -> res.put(accountInformation.getInformation_name(), accountInformation.getInformation_name().equals("password") ? "" : accountInformation.getDeciphered_information_value()));
        return res;
    }

    public void edit(JSONObject account_information, HibernateQuery hibernateQuery) throws HttpServletException {
        for (Object object : account_information.keySet()) {
            String key = String.valueOf(object);
            String value = account_information.optString(key);
            AccountInformation accountInformation = this.getInformationNamed(key);
            String old_value = null;
            if (accountInformation == null) {
                accountInformation = new AccountInformation(key, RSA.Encrypt(value, this.getPublic_key()), value, this);
                hibernateQuery.saveOrUpdateObject(accountInformation);
                this.getAccountInformationSet().add(accountInformation);
            } else {
                if (value.equals(""))
                    continue;
                old_value = accountInformation.getDeciphered_information_value();
                accountInformation.setInformation_value(RSA.Encrypt(value, this.getPublic_key()));
                accountInformation.setDeciphered_information_value(value);
                hibernateQuery.saveOrUpdateObject(accountInformation);
            }
            if (key.equals("password") && !value.equals(old_value)) {
                this.setLast_update(new Date());
                this.setStrongerPasswordAsked(false);
            }
        }
    }

    public void edit(JSONObject account_information, Integer password_reminder_interval, HibernateQuery hibernateQuery) throws HttpServletException {
        this.setReminder_interval(password_reminder_interval);
        this.edit(account_information, hibernateQuery);
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

    public AccountInformation getInformationNamed(String information_name) {
        return this.getAccountInformationSet().stream().filter(accountInformation -> accountInformation.getInformation_name().equals(information_name)).findFirst().orElse(null);
    }

    public boolean mustUpdatePassword() {
        Calendar nextUpdate = Calendar.getInstance();
        nextUpdate.setTime(this.getLast_update());
        nextUpdate.add(Calendar.MONTH, this.getReminder_interval());
        return this.getReminder_interval() != 0 && new Date().getTime() >= nextUpdate.getTimeInMillis();
    }

    public boolean satisfyWebsite(Website website) {
        if (this.getAccountInformationSet().size() != website.getWebsiteInformationList().size())
            return false;
        for (WebsiteInformation websiteInformation : website.getWebsiteInformationList()) {
            AccountInformation accountInformation = this.getInformationNamed(websiteInformation.getInformation_name());
            if (accountInformation == null || (accountInformation.getDeciphered_information_value() == null && accountInformation.getInformation_value() != null) || accountInformation.getDeciphered_information_value().equals(""))
                return false;
        }
        return true;
    }

    public boolean satisfySoftware(Software software) {
        if (this.getAccountInformationSet().size() != software.getSoftwareConnectionInformationSet().size())
            return false;
        for (SoftwareConnectionInformation softwareConnectionInformation : software.getSoftwareConnectionInformationSet()) {
            AccountInformation accountInformation = this.getInformationNamed(softwareConnectionInformation.getInformation_name());
            if (accountInformation == null || (accountInformation.getDeciphered_information_value() == null && accountInformation.getInformation_value() != null) || accountInformation.getDeciphered_information_value().equals(""))
                return false;
        }
        return true;
    }

    /**
     * @param account_information
     * @return true if all information are identical
     */
    public boolean sameAs(JSONObject account_information) {
        if (this.getAccountInformationSet().isEmpty())
            return false;
        for (AccountInformation accountInformation : this.getAccountInformationSet()) {
            String value = account_information.optString(accountInformation.getInformation_name());
            if (value.equals("") || !value.equals(accountInformation.getDeciphered_information_value()))
                return false;
        }
        return true;
    }

    /**
     * @param account_information
     * @return true if information are identical, password squizzed
     */
    public boolean matchExceptPassword(JSONObject account_information) {
        if (this.getAccountInformationSet().isEmpty())
            return false;
        for (AccountInformation accountInformation : this.getAccountInformationSet()) {
            if (accountInformation.getInformation_name().equals("password"))
                continue;
            String value = account_information.optString(accountInformation.getInformation_name());
            if (value.equals("") || !value.equals(accountInformation.getDeciphered_information_value()))
                return false;
        }
        return true;
    }

    public JSONObject getAccountInformationJson() {
        JSONObject res = new JSONObject();
        this.getAccountInformationSet().forEach(accountInformation -> res.put(accountInformation.getInformation_name(), accountInformation.getDeciphered_information_value()));
        return res;
    }

    public String getPassword() {
        for (AccountInformation accountInformation : this.getAccountInformationSet()) {
            if (!accountInformation.getInformation_name().equals("password"))
                continue;
            return accountInformation.getDeciphered_information_value();
        }
        return null;
    }

    public Integer calculatePasswordScore() throws NoSuchAlgorithmException {
        String password = this.getPassword();
        if (password == null)
            return null;
        if (new RangeAPI().isPwned(password))
            return -1;
        else
            return new Zxcvbn().measure(password).getScore();
    }

    public StringBuilder passwordExportCsvString() {
        StringBuilder passwordExportCsv = new StringBuilder();
        for (AccountInformation accountInformation : this.getAccountInformationSet()) {
            passwordExportCsv
                    .append(accountInformation.getDeciphered_information_value())
                    .append(",");
        }
        if (passwordExportCsv.length() > 0)
            passwordExportCsv.deleteCharAt(passwordExportCsv.length() - 1);
        else
            passwordExportCsv.append(",");
        return passwordExportCsv;
    }
}