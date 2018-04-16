package com.Ease.Team.TeamCard;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamLinkCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamLinkCard extends TeamCard {

    @Column(name = "url")
    private String url;

    @Column(name = "img_url")
    private String img_url;

    public TeamLinkCard() {

    }

    public TeamLinkCard(String name, Team team, Channel channel, String description, String url, String img_url) {
        super(name, team, channel, description);
        this.url = url;
        this.img_url = img_url;
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
    public Integer getPasswordScore() {
        throw new RuntimeException("You shouldn't be there");
    }

    @Override
    public void setPasswordScore(Integer passwordScore) {
        throw new RuntimeException("You shouldn't be there");
    }

    @Override
    public void setLastPasswordScoreAlertDate(Date date) {
        throw new RuntimeException("You shouldn't be there");
    }

    @Override
    public String getLogo() {
        return this.getImg_url();
    }

    @Override
    public String getType() {
        return "teamLinkCard";
    }

    @Override
    public String getSubtype() {
        return "";
    }

    @Override
    public boolean isTeamLinkCard() {
        return true;
    }

    @Override
    public String getMetricName() {
        return this.getUrl();
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("url", this.getUrl());
        return res;
    }
}