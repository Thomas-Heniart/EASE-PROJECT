package com.Ease.NewDashboard.App.WebsiteApp.ClassicApp;

import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Website.Website;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "ClassicApps")
@PrimaryKeyJoinColumn(name = "id")
public class ClassicApp extends WebsiteApp {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    protected Account account;

    public ClassicApp(AppInformation appInformation, Website website, Account account) {
        super(appInformation, website, "classicApp");
        this.account = account;
    }

    public ClassicApp() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Canonical name: " + this.getClass().getCanonicalName() + ", Type name: " + this.getClass().getTypeName();
    }
}
