package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
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
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("logWithApp_id", this.getLoginWith_app().getDb_id());
        res.put("logWith_website", this.getLogWith_website().getCatalogJson());
        return res;
    }
}