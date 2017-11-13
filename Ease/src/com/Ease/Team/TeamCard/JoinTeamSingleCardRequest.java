package com.Ease.Team.TeamCard;

import com.Ease.Team.TeamUser;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "joinTeamSingleCardRequests")
@PrimaryKeyJoinColumn(name = "id")
public class JoinTeamSingleCardRequest extends JoinTeamCardRequest {

    public JoinTeamSingleCardRequest(TeamCard teamCard, TeamUser teamUser) {
        super(teamCard, teamUser);
    }

    public JoinTeamSingleCardRequest() {
    }
}
