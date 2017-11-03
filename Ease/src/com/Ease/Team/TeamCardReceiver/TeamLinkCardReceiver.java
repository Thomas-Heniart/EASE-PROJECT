package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teamLinkCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
public class TeamLinkCardReceiver extends TeamCardReceiver {

    public TeamLinkCardReceiver() {

    }

    public TeamLinkCardReceiver(App app, TeamCard teamCard, TeamUser teamUser) {
        super(app, teamCard, teamUser);
    }

    @Override
    public String getType() {
        return "teamLinkApp";
    }
}