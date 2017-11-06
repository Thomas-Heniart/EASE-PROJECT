package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;

import javax.persistence.*;

@Entity
@Table(name = "teamEnterpriseCards")
@PrimaryKeyJoinColumn(name = "id")
public class TeamEnterpriseCard extends TeamWebsiteCard {

    public TeamEnterpriseCard() {

    }

    public TeamEnterpriseCard(Team team, Channel channel, String description, Website website, Integer password_reminder_interval) {
        super(team, channel, description, website, password_reminder_interval);
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