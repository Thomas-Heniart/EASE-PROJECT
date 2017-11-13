package com.Ease.NewDashboard;

import com.Ease.Catalog.Website;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "ssoApps")
public class SsoApp extends WebsiteApp {

    @ManyToOne
    @JoinColumn(name = "ssoGroup_id")
    private SsoGroup ssoGroup;

    public SsoApp(AppInformation appInformation, Website website, SsoGroup ssoGroup) {
        super(appInformation, website);
        this.ssoGroup = ssoGroup;
    }

    public SsoApp() {
    }

    public SsoGroup getSsoGroup() {
        return ssoGroup;
    }

    public void setSsoGroup(SsoGroup ssoGroup) {
        this.ssoGroup = ssoGroup;
    }

    public Account getAccount() {
        if (this.getSsoGroup() == null)
            return null;
        return this.getSsoGroup().getAccount();
    }

    @Override
    public boolean isSsoApp() {
        return true;
    }

    @Override
    public String getType() {
        return "ssoApp";
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("sso_group_id", this.getSsoGroup() == null ? -1 : this.getSsoGroup().getDb_id());
        return res;
    }
}