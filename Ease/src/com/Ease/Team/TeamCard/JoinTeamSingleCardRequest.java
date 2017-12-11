package com.Ease.Team.TeamCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "joinTeamSingleCardRequests")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class JoinTeamSingleCardRequest extends JoinTeamCardRequest {

    public JoinTeamSingleCardRequest(TeamCard teamCard, TeamUser teamUser) {
        super(teamCard, teamUser);
    }

    public JoinTeamSingleCardRequest() {
    }

    @Override
    public TeamCardReceiver accept(String symmetric_key, HibernateQuery hibernateQuery) throws HttpServletException {
        TeamSingleCard teamCard = (TeamSingleCard) this.getTeamCard();
        Account account = AccountFactory.getInstance().createAccountFromAccount(teamCard.getAccount(), symmetric_key);
        App app = new ClassicApp(new AppInformation(teamCard.getName()), teamCard.getWebsite(), account);
        TeamSingleCardReceiver teamSingleCardReceiver = new TeamSingleCardReceiver(app, teamCard, this.getTeamUser(), false);
        hibernateQuery.saveOrUpdateObject(teamSingleCardReceiver);
        teamCard.addTeamCardReceiver(teamSingleCardReceiver);
        teamCard.removeJoinTeamCardRequest(this);
        hibernateQuery.saveOrUpdateObject(teamCard);
        return teamSingleCardReceiver;
    }
}
