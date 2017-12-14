package com.Ease.NewDashboard;

import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "linkApps")
@PrimaryKeyJoinColumn(name = "id")
public class LinkApp extends App {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "link_app_info_id")
    private LinkAppInformation linkAppInformation;


    public LinkApp() {

    }

    public LinkApp(AppInformation appInformation, LinkAppInformation linkAppInformation) {
        super(appInformation);
        this.linkAppInformation = linkAppInformation;
    }

    public LinkAppInformation getLinkAppInformation() {
        return linkAppInformation;
    }

    public void setLinkAppInformation(LinkAppInformation linkAppInformation) {
        this.linkAppInformation = linkAppInformation;
    }

    @Override
    public String getLogo() {
        return this.getLinkAppInformation().getImg_url();
    }

    @Override
    public String getType() {
        return "linkApp";
    }

    @Override
    public boolean isLinkApp() {
        return true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("url", this.getLinkAppInformation().getUrl());
        res.put("logo", this.getLogo());
        return res;
    }

    @Override
    public JSONArray getConnectionJson(String public_key) throws HttpServletException {
        return super.getConnectionJson(public_key);
    }
}