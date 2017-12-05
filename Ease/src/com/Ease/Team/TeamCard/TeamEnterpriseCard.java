package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
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
    public String getType() {
        return "teamEnterpriseCard";
    }

    @Override
    public boolean isTeamEnterpriseCard() {
        return true;
    }

    @Override
    public void decipher(String teamKey) throws HttpServletException {
        for (TeamCardReceiver teamCardReceiver : this.getTeamCardReceiverMap().values()) {
            teamCardReceiver.getApp().decipher(null, teamKey);
        }
    }
}