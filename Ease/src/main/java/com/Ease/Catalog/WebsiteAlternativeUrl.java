package com.Ease.Catalog;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "WEBSITE_ALTERNATIVE_URL")
public class WebsiteAlternativeUrl {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "url")
    private String url;

    public WebsiteAlternativeUrl() {

    }

    public WebsiteAlternativeUrl(Website website, String url) {
        this.website = website;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteAlternativeUrl that = (WebsiteAlternativeUrl) o;

        return website.equals(that.website) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = website.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.getId());
        res.put("url", this.getUrl());
        return res;
    }
}