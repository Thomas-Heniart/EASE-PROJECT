package com.Ease.NewDashboard;

import javax.persistence.*;

@Entity
@Table(name = "linkAppInformations")
public class LinkAppInformation {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @Column(name = "url")
    private String url;

    @Column(name = "img_url")
    private String img_url;

    public LinkAppInformation() {

    }

    public LinkAppInformation(String url, String img_url) {
        this.url = url;
        this.img_url = img_url;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LinkAppInformation that = (LinkAppInformation) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}