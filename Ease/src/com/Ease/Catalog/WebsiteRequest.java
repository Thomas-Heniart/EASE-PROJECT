package com.Ease.Catalog;

import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "websiteRequests")
public class WebsiteRequest {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "url")
    private String url;

    @Column(name = "email")
    private String email;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date = new Date();

    @ManyToOne
    @JoinColumn(name = "website_id", nullable = false)
    private Website website;

    public WebsiteRequest() {

    }

    public WebsiteRequest(String url, String email, Website website) {
        this.url = url;
        this.email = email;
        this.website = website;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getDb_id());
        res.put("url", this.getUrl());
        res.put("email", this.getEmail());
        res.put("date", this.getDate().getTime());
        res.put("integrated", this.getWebsite().getWebsiteAttributes().isIntegrated());
        res.put("website_id", this.getWebsite().getDb_id());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteRequest that = (WebsiteRequest) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}
