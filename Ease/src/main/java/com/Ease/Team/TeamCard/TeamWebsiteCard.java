package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamWebsiteCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public abstract class TeamWebsiteCard extends TeamCard {

    @ManyToOne
    @JoinColumn(name = "website_id")
    private Website website;

    @Column(name = "password_reminder_interval")
    private Integer password_reminder_interval;

    public TeamWebsiteCard() {

    }

    public TeamWebsiteCard(String name, Team team, Channel channel, String description, Website website, Integer password_reminder_interval) {
        super(name, team, channel, description);
        this.website = website;
        this.password_reminder_interval = password_reminder_interval;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    @Override
    public Integer getPassword_reminder_interval() {
        return password_reminder_interval;
    }

    public void setPassword_reminder_interval(Integer password_reminder_interval) {
        this.password_reminder_interval = password_reminder_interval;
    }

    @Override
    public boolean isTeamWebsiteCard() {
        return true;
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

    @Override
    public String getSubtype() {
        return this.getWebsite().getWebsiteAttributes().isIntegrated() ? "classic" : "any";
    }
}