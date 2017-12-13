package com.Ease.NewDashboard;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;

import javax.persistence.*;

@Entity
@Table(name = "accountsInformations")
public class AccountInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "information_name")
    private String information_name;

    @Column(name = "information_value")
    private String information_value;

    @Transient
    private String deciphered_information_value;

    public AccountInformation() {

    }

    public AccountInformation(String information_name, String information_value, String deciphered_information_value) {
        this.information_name = information_name;
        this.information_value = information_value;
        this.deciphered_information_value = deciphered_information_value;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getInformation_name() {
        return information_name;
    }

    public void setInformation_name(String information_name) {
        this.information_name = information_name;
    }

    public String getInformation_value() {
        return information_value;
    }

    public void setInformation_value(String information_value) {
        this.information_value = information_value;
    }

    public String getDeciphered_information_value() {
        return deciphered_information_value;
    }

    public void setDeciphered_information_value(String deciphered_information_value) {
        this.deciphered_information_value = deciphered_information_value;
    }

    public void decipher(String private_key) throws HttpServletException {
        this.setDeciphered_information_value(RSA.Decrypt(this.getInformation_value(), private_key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInformation that = (AccountInformation) o;

        return db_id != null ? db_id.equals(that.db_id) : that.db_id == null;
    }
}