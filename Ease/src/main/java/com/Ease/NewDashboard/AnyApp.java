package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import org.json.JSONObject;

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
    public boolean isAnyApp() {
        return true;
    }

    @Override
    public void decipher(String symmetric_key, String team_key) throws HttpServletException {
        if (this.getAccount() == null)
            return;
        this.getAccount().decipher(team_key == null ? symmetric_key : team_key);
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.isEmpty());
        res.put("account_information", new JSONObject());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("password_reminder_interval", this.getAccount().getReminder_interval());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
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