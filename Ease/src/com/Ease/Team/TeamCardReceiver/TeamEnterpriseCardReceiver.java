package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.ClassicApp;
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

    @Override
    public String getType() {
        return "teamEnterpriseApp";
    }

    @Override
    public JSONObject getCardJson() {
        JSONObject res = super.getCardJson();
        res.put("account_information", ((ClassicApp)this.getApp()).getAccount().getJsonWithoutPassword());
        res.put("last_update", ((ClassicApp)this.getApp()).getAccount().getLast_update().getTime());
        return res;
    }
}