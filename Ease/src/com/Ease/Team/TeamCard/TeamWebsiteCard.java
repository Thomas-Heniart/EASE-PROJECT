package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "teamWebsiteCards")
@PrimaryKeyJoinColumn(name = "id")
public abstract class TeamWebsiteCard extends TeamCard {

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "password_reminder_interval")
    private Integer password_reminder_interval;

    public TeamWebsiteCard() {

    }

    public TeamWebsiteCard(Team team, Channel channel, Website website, Integer password_reminder_interval) {
        super(team, channel);
        this.website = website;
        this.password_reminder_interval = password_reminder_interval;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public Integer getPassword_reminder_interval() {
        return password_reminder_interval;
    }

    public void setPassword_reminder_interval(Integer password_reminder_interval) {
        this.password_reminder_interval = password_reminder_interval;
    }

    @Override
    public String getName() {
        return this.getWebsite().getName();
    }

    @Override
    public String getLogo() {
        return this.getWebsite().getLogo();
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("website", this.getWebsite().getCatalogJson());
        res.put("password_reminder_interval", this.getPassword_reminder_interval());
        return res;
    }
}