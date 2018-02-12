package com.Ease.Update;

import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.HttpServletException;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "UPDATE_ACCOUNT_INFORMATION")
public class UpdateAccountInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_account_id")
    private UpdateAccount updateAccount;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Transient
    private String deciphered_value;

    public UpdateAccountInformation() {

    }

    public UpdateAccountInformation(String name, String value, UpdateAccount updateAccount) {
        this.name = name;
        this.value = value;
        this.updateAccount = updateAccount;
    }

    public UpdateAccountInformation(String name, String value, UpdateAccount updateAccount, String deciphered_value) {
        this.name = name;
        this.value = value;
        this.updateAccount = updateAccount;
        this.deciphered_value = deciphered_value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public UpdateAccount getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(UpdateAccount updateAccount) {
        this.updateAccount = updateAccount;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getDeciphered_value() {
        return deciphered_value;
    }

    public void setDeciphered_value(String deciphered_value) {
        this.deciphered_value = deciphered_value;
    }

    public void decipher(String private_key) throws HttpServletException {
        if (this.getDeciphered_value() != null)
            return;
        this.setDeciphered_value(RSA.Decrypt(this.getValue(), private_key));
    }

    public void edit(String value, String publicKey) throws HttpServletException {
        this.setValue(RSA.Encrypt(value, publicKey));
        this.setDeciphered_value(value);
    }
}