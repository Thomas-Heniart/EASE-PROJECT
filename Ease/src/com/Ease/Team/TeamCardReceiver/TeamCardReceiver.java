package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "teamCardReceivers")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class TeamCardReceiver {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamCard_id")
    private TeamCard teamCard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamUser_id")
    private TeamUser teamUser;

    public TeamCardReceiver() {

    }

    public TeamCardReceiver(App app, TeamCard teamCard, TeamUser teamUser) {
        this.app = app;
        this.teamCard = teamCard;
        this.teamUser = teamUser;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    public TeamCard getTeamCard() {
        return teamCard;
    }

    public void setTeamCard(TeamCard teamCard) {
        this.teamCard = teamCard;
    }

    public TeamUser getTeamUser() {
        return teamUser;
    }

    public void setTeamUser(TeamUser teamUser) {
        this.teamUser = teamUser;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("teamUser_id", this.getTeamUser().getDb_id());
        res.put("teamCard_id", this.getTeamCard().getDb_id());
        res.put("team_id", this.getTeamCard().getTeam().getDb_id());
        res.put("teamCardReceiver_id", this.getDb_id());
        res.put("type", this.getType());
        return res;
    }

    public JSONObject getCardJson() {
        JSONObject res = new JSONObject();
        res.put("teamUser_id", this.getTeamUser().getDb_id());
        res.put("teamCard_id", this.getTeamCard().getDb_id());
        res.put("team_id", this.getTeamCard().getTeam().getDb_id());
        res.put("teamCardReceiver_id", this.getDb_id());
        res.put("name", this.getApp().getAppInformation().getName());
        return res;
    }

    public abstract String getType();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamCardReceiver that = (TeamCardReceiver) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }
}