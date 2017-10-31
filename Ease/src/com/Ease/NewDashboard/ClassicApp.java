package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;

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

    public ClassicApp(AppInformation appInformation, String type, String websiteApp_type, Website website) {
        super(appInformation, type, websiteApp_type, website);
    }

    public ClassicApp(AppInformation appInformation, String type, String websiteApp_type, Website website, Account account) {
        super(appInformation, type, websiteApp_type, website);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}