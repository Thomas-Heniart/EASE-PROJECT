package com.Ease.NewDashboard.App.WebsiteApp.ClassicApp;

import javax.persistence.*;
import java.util.List;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    List<AccountInformation> accountInformationList;

    public Account(List<AccountInformation> accountInformationList) {
        this.accountInformationList = accountInformationList;
    }

    public Account() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public List<AccountInformation> getAccountInformationList() {
        return accountInformationList;
    }

    public void setAccountInformationList(List<AccountInformation> accountInformationList) {
        this.accountInformationList = accountInformationList;
    }
}
