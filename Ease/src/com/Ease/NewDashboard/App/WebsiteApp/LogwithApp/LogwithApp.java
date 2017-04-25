package com.Ease.NewDashboard.App.WebsiteApp.LogwithApp;

import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Website.Website;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "LogwithApps")
@PrimaryKeyJoinColumn(name = "id")
public class LogwithApp extends WebsiteApp {
    @OneToOne
    @JoinColumn(name = "logWith_website_app_id")
    protected WebsiteApp logwithApp;

    public LogwithApp(AppInformation appInformation, Website website, WebsiteApp logwithApp) {
        super(appInformation, website, "logwithApp");
        this.logwithApp = logwithApp;
    }

    public LogwithApp() {
    }

    public WebsiteApp getLogwithApp() {
        return logwithApp;
    }

    public void setLogwithApp(WebsiteApp logwithApp) {
        this.logwithApp = logwithApp;
    }

    @Override
    public String toString() {
        return "LogwithApp";
    }
}
