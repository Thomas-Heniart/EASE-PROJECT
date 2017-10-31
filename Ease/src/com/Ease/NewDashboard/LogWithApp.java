package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;

import javax.persistence.*;

@Entity
@Table(name = "logWithApps")
@PrimaryKeyJoinColumn(name = "id")
public class LogWithApp extends WebsiteApp {

    @ManyToOne
    @JoinColumn(name = "logWith_website_app_id")
    private WebsiteApp loginWith_app;

    public LogWithApp() {

    }

    public LogWithApp(AppInformation appInformation, String type, String websiteApp_type, Website website, WebsiteApp loginWith_app) {
        super(appInformation, type, websiteApp_type, website);
        this.loginWith_app = loginWith_app;
    }

    public WebsiteApp getLoginWith_app() {
        return loginWith_app;
    }

    public void setLoginWith_app(WebsiteApp loginWith_app) {
        this.loginWith_app = loginWith_app;
    }
}