package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;

import javax.persistence.*;

@Entity
@Table(name = "websiteApps")
@PrimaryKeyJoinColumn(name = "id")
abstract public class WebsiteApp extends App {

    @Column(name = "type")
    private String websiteApp_type;

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    public WebsiteApp() {

    }

    public WebsiteApp(AppInformation appInformation, String type, String websiteApp_type, Website website) {
        super(appInformation, type);
        this.websiteApp_type = websiteApp_type;
        this.website = website;
    }

    public String getWebsiteApp_type() {
        return websiteApp_type;
    }

    public void setWebsiteApp_type(String websiteApp_type) {
        this.websiteApp_type = websiteApp_type;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}