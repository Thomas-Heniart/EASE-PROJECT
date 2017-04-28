package com.Ease.NewDashboard.App.WebsiteApp.LogwithApp;

import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.NewDashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManagerHibernate;
import com.Ease.Website.Website;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 25/04/2017.
 */
@Entity
@Table(name = "logWithApps")
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

    @Override
    public void edit(JSONObject editJson, ServletManagerHibernate sm) throws GeneralException {
        super.edit(editJson, sm);
        String logwithApp_id = (String) editJson.get("logwithApp_id");
        String profile_id = (String) editJson.get("profile_id");
        if (logwithApp_id == null)
            return;
        WebsiteApp logWithApp = (WebsiteApp) sm.getUser().getProfileManager().getProfileWithId(Integer.parseInt(profile_id)).getAppWithId(Integer.parseInt(logwithApp_id));
        this.logwithApp = logWithApp;
    }
}
