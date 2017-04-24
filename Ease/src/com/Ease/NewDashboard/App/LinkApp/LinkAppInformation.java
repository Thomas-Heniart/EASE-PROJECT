package com.Ease.NewDashboard.App.LinkApp;

import javax.persistence.*;

/**
 * Created by thomas on 21/04/2017.
 */
@Entity
@Table(name = "linkAppInformations")
public class LinkAppInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "url")
    protected String url;

    @Column(name = "img_url")
    protected String img_url;

    public LinkAppInformation(String url, String img_url) {
        this.url = url;
        this.img_url = img_url;
    }

    public LinkAppInformation() {
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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public String toString() {
        return "URL: " + this.url;
    }
}
