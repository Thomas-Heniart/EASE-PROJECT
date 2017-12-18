package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Software;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamEnterpriseSoftwareCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamEnterpriseSoftwareCard extends TeamSoftwareCard {

    public TeamEnterpriseSoftwareCard() {

    }

    public TeamEnterpriseSoftwareCard(String name, Team team, Channel channel, String description, Software software) {
        super(name, team, channel, description, software);
    }

    public TeamEnterpriseSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval) {
        super(name, team, channel, description, software, password_reminder_interval);
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