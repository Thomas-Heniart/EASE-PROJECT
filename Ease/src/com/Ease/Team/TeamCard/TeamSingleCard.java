package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "teamSingleCards")
@PrimaryKeyJoinColumn(name = "id")
public class TeamSingleCard extends TeamWebsiteCard {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    private Account account;

    public TeamSingleCard() {

    }

    public TeamSingleCard(Team team, Channel channel, Website website, Integer password_reminder_interval, Account account) {
        super(team, channel, website, password_reminder_interval);
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void decipherAccount(String symmetric_key) throws HttpServletException {
        if (this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        res.put("last_update", this.getAccount().getLast_update().getTime());
        return res;
    }

    @Override
    public String getType() {
        return "teamSingleCard";
    }

    @Override
    public boolean isTeamSingleCard() {
        return true;
    }

    public void decipher(String symmetric_key) throws HttpServletException {
        if (this.getAccount() == null || this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
        for (TeamCardReceiver teamCardReceiver : this.getTeamCardReceiverMap().values()) {
            teamCardReceiver.getApp().decipher(symmetric_key);
        }
    }
}