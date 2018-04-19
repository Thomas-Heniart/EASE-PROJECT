package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
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
@Table(name = "teamEnterpriseCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamEnterpriseCard extends TeamWebsiteCard {

    @Column(name = "lastPasswordScoreAlertDate")
    private Date lastPasswordScoreAlertDate;

    public TeamEnterpriseCard() {

    }

    public TeamEnterpriseCard(String name, Team team, Channel channel, String description, Website website, Integer password_reminder_interval) {
        super(name, team, channel, description, website, password_reminder_interval);
    }

    @Override
    public Integer getPasswordScore() {
        throw new RuntimeException("You shouldn't be there");
    }

    @Override
    public void setPasswordScore(Integer passwordScore) {
        throw new RuntimeException("You shouldn't be there");
    }

    public Date getLastPasswordScoreAlertDate() {
        return lastPasswordScoreAlertDate;
    }

    @Override
    public void setLastPasswordScoreAlertDate(Date date) {
        this.lastPasswordScoreAlertDate = date;
    }

    @Override
    public String getType() {
        return "teamEnterpriseCard";
    }

    @Override
    public boolean isTeamEnterpriseCard() {
        return true;
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("last_password_score_alert_date", this.getLastPasswordScoreAlertDate() == null ? JSONObject.NULL : this.getLastPasswordScoreAlertDate().getTime());
        return res;
    }
}