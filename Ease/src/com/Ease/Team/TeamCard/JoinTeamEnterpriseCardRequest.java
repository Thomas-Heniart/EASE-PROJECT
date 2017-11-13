package com.Ease.Team.TeamCard;

import com.Ease.NewDashboard.Account;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "joinTeamEnterpriseCardRequests")
@PrimaryKeyJoinColumn(name = "id")
public class JoinTeamEnterpriseCardRequest extends JoinTeamCardRequest {

    @OneToOne(cascade = CascadeType.ALL)
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
}