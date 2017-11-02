package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

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

    @Override
    public void decipher(String symmetric_key) throws HttpServletException {
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
        JSONObject res =  super.getJson();
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }

    @Override
    public JSONObject getRestJson() {
        JSONObject res = super.getRestJson();
        res.put("account_information", this.getAccount().getJson());
        return res;
    }
}