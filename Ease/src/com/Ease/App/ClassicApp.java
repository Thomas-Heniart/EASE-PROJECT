package com.Ease.App;

import javax.persistence.*;

@Entity
@Table(name = "classicApps")
@PrimaryKeyJoinColumn(name = "id")
public class ClassicApp extends WebsiteApp {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public ClassicApp() {

    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}