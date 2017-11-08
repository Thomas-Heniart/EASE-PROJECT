package com.Ease.Catalog;

import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "websiteFailures")
public class WebsiteFailure {
    @Id
    @Column(name = "url")
    private String url;

    @Column(name = "count")
    private Long count = 1L;

    public WebsiteFailure() {

    }

    public WebsiteFailure(String url) {
        this.url = url;
    }

    public WebsiteFailure(String url, Long count) {
        this.url = url;
        this.count = count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebsiteFailure that = (WebsiteFailure) o;

        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }

    public void incrementCount() {
        this.count++;
    }

    public void incrementCount(Long count_to_add) {
        this.count += count_to_add;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("url", this.getUrl());
        res.put("count", this.getCount());
        return res;
    }
}