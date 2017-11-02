package com.Ease.Team.TeamCard;

import javax.persistence.*;

@Entity
@Table(name = "teamLinkCards")
@PrimaryKeyJoinColumn(name = "id")
public class TeamLinkCard extends TeamCard {

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "img_url")
    private String img_url;

    public TeamLinkCard() {

    }

    public TeamLinkCard(String name, String url, String img_url) {
        this.name = name;
        this.url = url;
        this.img_url = img_url;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
    public String getLogo() {
        return this.getImg_url();
    }

    @Override
    public String getType() {
        return "teamLinkCard";
    }
}