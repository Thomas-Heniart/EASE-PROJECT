package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "websiteApps")
@PrimaryKeyJoinColumn(name = "id")
public class WebsiteApp extends App {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "website_id")
    private Website website;

    public WebsiteApp() {

    }

    public WebsiteApp(AppInformation appInformation, Website website) {
        super(appInformation);
        this.website = website;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getLogo() {
        return this.getWebsite().getLogo();
    }

    public String getType() {
        return "websiteApp";
    }

    @Override
    public boolean isWebsiteApp() {
        return true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("website", this.getWebsite().getCatalogJson());
        return res;
    }
}