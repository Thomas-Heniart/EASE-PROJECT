package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "logWithApps")
@PrimaryKeyJoinColumn(name = "id")
public class LogWithApp extends WebsiteApp {

    @ManyToOne
    @JoinColumn(name = "logWith_website_app_id")
    private WebsiteApp loginWith_app;

    @OneToOne
    @JoinColumn(name = "logWithWebsite_id")
    private Website logWith_website;

    public LogWithApp() {

    }

    public LogWithApp(AppInformation appInformation, Website website, WebsiteApp loginWith_app) {
        super(appInformation, website);
        this.loginWith_app = loginWith_app;
        this.logWith_website = loginWith_app.getWebsite();
    }

    public WebsiteApp getLoginWith_app() {
        return loginWith_app;
    }

    public void setLoginWith_app(WebsiteApp loginWith_app) {
        this.loginWith_app = loginWith_app;
    }

    public Website getLogWith_website() {
        return logWith_website;
    }

    public void setLogWith_website(Website logWith_website) {
        this.logWith_website = logWith_website;
    }

    public boolean isLogWithApp() {
        return true;
    }

    @Override
    public String getType() {
        return "logWithApp";
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("logWithApp_id", this.getLoginWith_app() == null ? -1 : this.getLoginWith_app().getDb_id());
        res.put("logWith_website", this.getLogWith_website().getCatalogJson());
        return res;
    }

    @Override
    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        if (this.getLoginWith_app() == null)
            throw new HttpServletException(HttpStatus.BadRequest, "You cannot connect to en empty app");
        JSONArray res = this.getLoginWith_app().getConnectionJson(public_key);
        JSONObject website = (JSONObject) super.getConnectionJson(public_key).get(0);
        website.put("logWith", this.getLogWith_website().getName());
        website.put("app_name", this.getAppInformation().getName());
        website.put("website_name", this.getWebsite().getName());
        website.put("type", "logWithApp");
        return res;
    }

    @Override
    public void decipher(String symmetric_key, String team_key) throws HttpServletException {
        if (this.getLoginWith_app() != null) {

            this.getLoginWith_app().decipher(symmetric_key, team_key);
        }
    }
}