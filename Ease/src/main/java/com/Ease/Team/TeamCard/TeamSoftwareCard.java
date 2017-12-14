package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Software;
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
@Table(name = "teamSoftwareCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public abstract class TeamSoftwareCard extends TeamCard {

    @ManyToOne
    @JoinColumn(name = "software_id")
    private Software software;

    @Column(name = "password_reminder_interval")
    private Integer password_reminder_interval = 0;

    public TeamSoftwareCard() {

    }

    public TeamSoftwareCard(String name, Team team, Channel channel, String description, Software software) {
        super(name, team, channel, description);
        this.software = software;
    }

    public TeamSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval) {
        super(name, team, channel, description);
        this.software = software;
        this.password_reminder_interval = password_reminder_interval;
    }

    public Software getSoftware() {
        return software;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public Integer getPassword_reminder_interval() {
        return password_reminder_interval;
    }

    public void setPassword_reminder_interval(Integer password_reminder_interval) {
        this.password_reminder_interval = password_reminder_interval;
    }

    @Override
    public String getLogo() {
        return this.getSoftware().getLogo();
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("website", this.getSoftware().getJson());
        res.put("password_reminder_interval", this.getPassword_reminder_interval());
        return res;
    }

    @Override
    public String getSubtype() {
        return "software";
    }
}