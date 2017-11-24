package com.Ease.Team.TeamCardReceiver;

import com.Ease.NewDashboard.App;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamUser;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "teamCardReceivers")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class TeamCardReceiver {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "sharing_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sharing_date = new Date();

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne
    @JoinColumn(name = "teamCard_id")
    private TeamCard teamCard;

    @ManyToOne
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

    public Date getSharing_date() {
        return sharing_date;
    }

    public void setSharing_date(Date sharing_date) {
        this.sharing_date = sharing_date;
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
        res.put("team_user_id", this.getTeamUser().getDb_id());
        res.put("team_card_id", this.getTeamCard().getDb_id());
        res.put("team_id", this.getTeamCard().getTeam().getDb_id());
        res.put("team_card_receiver_id", this.getDb_id());
        res.put("sharing_date", this.getSharing_date().getTime());
        res.put("type", this.getType());
        return res;
    }

    public JSONObject getCardJson() {
        JSONObject res = new JSONObject();
        res.put("team_user_id", this.getTeamUser().getDb_id());
        res.put("team_card_id", this.getTeamCard().getDb_id());
        res.put("team_id", this.getTeamCard().getTeam().getDb_id());
        res.put("app_id", this.getApp().getDb_id());
        res.put("id", this.getDb_id());
        res.put("sharing_date", this.getSharing_date().getTime());
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

    public boolean isTeamSingleCardReceiver() {
        return false;
    }
}