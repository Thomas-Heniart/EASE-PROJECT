package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity
@Table(name = "websiteApps")
@PrimaryKeyJoinColumn(name = "id")
public class WebsiteApp extends App {

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @OneToMany(mappedBy = "loginWith_app")
    private Set<LogWithApp> logWithAppSet = ConcurrentHashMap.newKeySet();

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

    public Set<LogWithApp> getLogWithAppSet() {
        return logWithAppSet;
    }

    public void setLogWithAppSet(Set<LogWithApp> logWithAppSet) {
        this.logWithAppSet = logWithAppSet;
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

    @Override
    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        JSONArray res = super.getConnectionJson(public_key);
        JSONObject website = new JSONObject();
        website.put("website", this.getWebsite().getConnectionJson());
        res.add(website);
        return res;
    }
}