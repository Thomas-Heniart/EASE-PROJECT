package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.DateComparator;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "classicApps")
@PrimaryKeyJoinColumn(name = "id")
public class ClassicApp extends WebsiteApp {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    public ClassicApp() {

    }

    public ClassicApp(AppInformation appInformation, Website website) {
        super(appInformation, website);
    }

    public ClassicApp(AppInformation appInformation, Website website, Account account) {
        super(appInformation, website);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isUpToDate() {
        return this.getAccount() == null || !DateComparator.isOutdated(this.getAccount().getLast_update(), this.getAccount().getReminder_interval(), 0);
    }

    @Override
    public void decipher(String symmetric_key) throws HttpServletException {
        if (this.getAccount() == null)
            return;
        this.getAccount().decipher(symmetric_key);
    }

    @Override
    public String getType() {
        return "classicApp";
    }

    @Override
    public boolean isClassicApp() {
        return true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.getAccount() == null || !this.getAccount().satisfyWebsite(this.getWebsite()));
        res.put("account_information", new JSONObject());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }

    @Override
    public JSONObject getRestJson() {
        JSONObject res = super.getRestJson();
        res.put("empty", this.getAccount() == null);
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJson());
        return res;
    }

    @Override
    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        JSONArray res = super.getConnectionJson(public_key);
        JSONObject website = (JSONObject) res.get(0);
        website.put("user", this.getAccount().getCipheredJson(public_key));
        website.put("type", "ClassicApp");
        website.put("app_name", this.getAppInformation().getName());
        website.put("website_name", this.getWebsite().getName());
        return res;
    }
}