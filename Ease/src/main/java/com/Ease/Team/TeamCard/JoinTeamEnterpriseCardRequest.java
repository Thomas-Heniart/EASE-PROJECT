package com.Ease.Team.TeamCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "joinTeamEnterpriseCardRequests")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class JoinTeamEnterpriseCardRequest extends JoinTeamCardRequest {

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public JoinTeamEnterpriseCardRequest() {

    }

    public JoinTeamEnterpriseCardRequest(TeamCard teamCard, TeamUser teamUser, Account account) {
        super(teamCard, teamUser);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }

    @Override
    public TeamCardReceiver accept(String symmetric_key, HibernateQuery hibernateQuery) throws HttpServletException {
        this.getAccount().decipher(symmetric_key);
        TeamCard teamCard = this.getTeamCard();
        App app;
        if (teamCard.isTeamSoftwareCard())
            app = new SoftwareApp(new AppInformation(teamCard.getName()), ((TeamEnterpriseSoftwareCard) teamCard).getSoftware(), account);
        else
            app = new ClassicApp(new AppInformation(teamCard.getName()), ((TeamEnterpriseCard) teamCard).getWebsite(), account);
        TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = new TeamEnterpriseCardReceiver(app, teamCard, this.getTeamUser());
        hibernateQuery.saveOrUpdateObject(teamEnterpriseCardReceiver);
        teamCard.removeJoinTeamCardRequest(this);
        hibernateQuery.deleteObject(this);
        return teamEnterpriseCardReceiver;
    }
}