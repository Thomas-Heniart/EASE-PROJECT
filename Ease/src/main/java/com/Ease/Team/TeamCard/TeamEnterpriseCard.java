package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamEnterpriseCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamEnterpriseCard extends TeamWebsiteCard {

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

    @Override
    public void setLastPasswordScoreAlertDate(Date date) {
        throw new RuntimeException("You shouldn't be there");
    }

    @Override
    public String getType() {
        return "teamEnterpriseCard";
    }

    @Override
    public boolean isTeamEnterpriseCard() {
        return true;
    }
}