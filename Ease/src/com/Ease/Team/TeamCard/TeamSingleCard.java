package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "teamSingleCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamSingleCard extends TeamWebsiteCard {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "teamUser_filler_id")
    private TeamUser teamUser_filler;

    public TeamSingleCard() {

    }

    public TeamSingleCard(String name, Team team, Channel channel, String description, Website website, Integer password_reminder_interval, Account account, TeamUser teamUser_filler) {
        super(name, team, channel, description, website, password_reminder_interval);
        this.account = account;
        this.teamUser_filler = teamUser_filler;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public TeamUser getTeamUser_filler() {
        return teamUser_filler;
    }

    public void setTeamUser_filler(TeamUser teamUser_filler) {
        this.teamUser_filler = teamUser_filler;
    }

    public void decipherAccount(String symmetric_key) throws HttpServletException {
        if (this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.getAccount() == null || !this.getAccount().satisfyWebsite((this.getWebsite())));
        res.put("account_information", new JSONObject());
        res.put("team_user_filler_id", this.getTeamUser_filler() == null ? -1 : this.getTeamUser_filler().getDb_id());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
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

    @Override
    public void decipher(String symmetric_key) throws HttpServletException {
        if (this.getAccount() == null || this.getAccount().getDeciphered_private_key() != null)
            return;
        this.getAccount().decipher(symmetric_key);
        for (TeamCardReceiver teamCardReceiver : this.getTeamCardReceiverMap().values()) {
            teamCardReceiver.getApp().decipher(symmetric_key);
        }
    }
}