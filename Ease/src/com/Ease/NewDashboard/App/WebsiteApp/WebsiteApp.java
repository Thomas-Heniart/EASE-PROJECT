package com.Ease.NewDashboard.App.WebsiteApp;

import com.Ease.Website.Website;

import javax.persistence.*;

/**
 * Created by thomas on 24/04/2017.
 */
@Entity
@Table(name = "WebsiteApps")
@PrimaryKeyJoinColumn(name = "id")
public class WebsiteApp {
    @OneToOne
    @JoinColumn(name = "website_id")
    protected Website website;

    public WebsiteApp(Website website) {
        this.website = website;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
