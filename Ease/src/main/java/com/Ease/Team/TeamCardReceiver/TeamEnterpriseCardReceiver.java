package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSoftwareCard;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamEnterpriseCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamEnterpriseCardReceiver extends TeamCardReceiver {


    public TeamEnterpriseCardReceiver() {

    }

    public TeamEnterpriseCardReceiver(App app, TeamCard teamCard, TeamUser teamUser) {
        super(app, teamCard, teamUser);
    }

    @Override
    public String getType() {
        return "teamEnterpriseApp";
    }

    @Override
    public JSONObject getCardJson() {
        JSONObject res = super.getCardJson();
        Account account = this.getApp().getAccount();
        res.put("account_information", new JSONObject());
        res.put("empty", this.isEmpty());
        if (account == null)
            return res;
        res.put("account_information", account.getJsonWithoutPassword());
        res.put("last_update_date", account.getLast_update().getTime());
        return res;
    }

    private boolean isEmpty() {
        Account account = this.getApp().getAccount();
        if (account == null)
            return true;
        TeamCard teamCard = this.getTeamCard();
        if (teamCard.isTeamWebsiteCard()) {
            TeamWebsiteCard teamWebsiteCard = (TeamWebsiteCard) teamCard;
            return !account.satisfyWebsite(teamWebsiteCard.getWebsite());
        } else if (teamCard.isTeamSoftwareCard()) {
            TeamSoftwareCard teamSoftwareCard = (TeamSoftwareCard) teamCard;
            return !account.satisfySoftware(teamSoftwareCard.getSoftware());
        }
        return false;
    }

    public void decipher(String deciphered_teamKey) throws HttpServletException {
        App app = this.getApp();
        if (app.getAccount() == null || app.getAccount().getDeciphered_private_key() != null)
            return;
        app.getAccount().decipher(deciphered_teamKey);
    }
}