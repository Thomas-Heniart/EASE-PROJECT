package com.Ease.Team.TeamCard;

import com.Ease.Catalog.Software;
import com.Ease.NewDashboard.Account;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "teamSingleSoftwareCards")
@PrimaryKeyJoinColumn(name = "id")
@OnDelete(action = OnDeleteAction.CASCADE)
public class TeamSingleSoftwareCard extends TeamSoftwareCard {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "teamUser_filler_id")
    private TeamUser teamUser_filler;

    public TeamSingleSoftwareCard() {

    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software) {
        super(name, team, channel, description, software);
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Account account) {
        super(name, team, channel, description, software);
        this.account = account;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval, Account account) {
        super(name, team, channel, description, software, password_reminder_interval);
        this.account = account;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, TeamUser teamUser_filler) {
        super(name, team, channel, description, software);
        this.teamUser_filler = teamUser_filler;
    }

    public TeamSingleSoftwareCard(String name, Team team, Channel channel, String description, Software software, Integer password_reminder_interval, TeamUser teamUser_filler) {
        super(name, team, channel, description, software, password_reminder_interval);
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

    @Override
    public String getType() {
        return "teamSingleCard";
    }

    @Override
    public JSONObject getJson() {
        JSONObject res = super.getJson();
        res.put("empty", this.getAccount() == null);
        res.put("account_information", new JSONObject());
        res.put("team_user_filler_id", this.getTeamUser_filler() == null ? -1 : this.getTeamUser_filler().getDb_id());
        if (this.getAccount() == null)
            return res;
        res.put("last_update_date", this.getAccount().getLast_update().getTime());
        res.put("account_information", this.getAccount().getJsonWithoutPassword());
        return res;
    }
}