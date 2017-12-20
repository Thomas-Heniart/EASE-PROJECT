package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ssoApps")
@PrimaryKeyJoinColumn(name = "id")
public class SsoApp extends WebsiteApp {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ssoGroup_id")
    private SsoGroup ssoGroup;

    public SsoApp(AppInformation appInformation, Website website, SsoGroup ssoGroup) {
        super(appInformation, website);
        this.ssoGroup = ssoGroup;
    }

    public SsoApp() {
    }

    public SsoGroup getSsoGroup() {
        return ssoGroup;
    }

    public void setSsoGroup(SsoGroup ssoGroup) {
        this.ssoGroup = ssoGroup;
    }

    @Override
    public Account getAccount() {
        if (this.getSsoGroup() == null)
            return null;
        return this.getSsoGroup().getAccount();
    }

    @Override
    public boolean isSsoApp() {
        return true;
    }

    @Override
    public String getType() {
        return "ssoApp";
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("sso_group_id", this.getSsoGroup() == null ? -1 : this.getSsoGroup().getDb_id());
        return res;
    }

    @Override
    public void decipher(String symmetric_key, String team_key) throws HttpServletException {
        if (this.getAccount() == null)
            return;
        this.getAccount().decipher(team_key == null ? symmetric_key : team_key);
    }

    @Override
    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        if (this.getAccount() == null)
            throw new HttpServletException(HttpStatus.BadRequest, "You cannot connect to en empty app");
        JSONArray res = super.getConnectionJson(public_key);
        JSONObject website = (JSONObject) res.get(0);
        website.put("user", this.getAccount().getCipheredJson(public_key));
        website.put("type", "ClassicApp");
        website.put("app_name", this.getAppInformation().getName());
        website.put("website_name", this.getWebsite().getName());
        return res;
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

    @Override
    public boolean isEmpty() {
        return this.getAccount() == null || !this.getAccount().satisfyWebsite(this.getWebsite());
    }
}