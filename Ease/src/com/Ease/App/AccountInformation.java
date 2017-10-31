package com.Ease.App;

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

    public AccountInformation() {

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
}