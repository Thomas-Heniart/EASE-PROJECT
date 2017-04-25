package com.Ease.NewDashboard.App.WebsiteApp;

import com.Ease.NewDashboard.App.App;
import com.Ease.NewDashboard.App.AppInformation;
import com.Ease.Website.Website;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "WebsiteApps")
@PrimaryKeyJoinColumn(name = "id")
public class WebsiteApp extends App {
    @OneToOne
    @JoinColumn(name = "website_id")
    protected Website website;

    @Column(name = "type")
    protected String website_app_type;

    public WebsiteApp(AppInformation appInformation, Website website, String website_app_type) {
        super("websiteApp", new Date(), appInformation);
        this.website = website;
        this.website_app_type = website_app_type;
    }

    public WebsiteApp() {
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getWebsite_app_type() {
        return website_app_type;
    }

    public void setWebsite_app_type(String website_app_type) {
        this.website_app_type = website_app_type;
    }

    @Override
    public String toString() {
        return "WebsiteApp";
    }
}
