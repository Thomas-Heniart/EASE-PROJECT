package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.Account;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "teamEnterpriseCardReceivers")
@PrimaryKeyJoinColumn(name = "id")
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
        Account account = ((ClassicApp)this.getApp()).getAccount();
        res.put("account_information", new JSONObject());
        if (account == null)
            return res;
        res.put("account_information", account.getJsonWithoutPassword());
        res.put("last_update_date", account.getLast_update().getTime());
        return res;
    }

    public void decipher(String deciphered_teamKey) throws HttpServletException {
        ClassicApp app = (ClassicApp) this.getApp();
        if (app.getAccount() == null || app.getAccount().getDeciphered_private_key() != null)
            return;
        app.getAccount().decipher(deciphered_teamKey);
    }
}