package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "anyApps")
@PrimaryKeyJoinColumn(name = "id")
public class AnyApp extends WebsiteApp {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    public AnyApp() {

    }

    public AnyApp(AppInformation appInformation, Website website, Account account) {
        super(appInformation, website);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public JSONObject getJson() {
        return super.getJson();
    }

    @Override
    public String getType() {
        return "anyApp";
    }

    @Override
    public JSONObject getRestJson() {
        JSONObject res = super.getRestJson();
        res.put("empty", this.isEmpty());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJson());
        return res;
    }
}