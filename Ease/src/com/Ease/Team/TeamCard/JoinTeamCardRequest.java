package com.Ease.Team.TeamCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import org.json.simple.JSONObject;

import javax.persistence.*;

@Entity
@Table(name = "joinTeamCardRequests")
@Inheritance(strategy = InheritanceType.JOINED)
abstract public class JoinTeamCardRequest {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer db_id;

    @ManyToOne
    @JoinColumn(name = "teamCard_id")
    private TeamCard teamCard;

    @ManyToOne
    @JoinColumn(name = "teamUser_id")
    private TeamUser teamUser;

    public JoinTeamCardRequest() {

    }

    public JoinTeamCardRequest(TeamCard teamCard, TeamUser teamUser) {
        this.teamCard = teamCard;
        this.teamUser = teamUser;
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
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
        res.put("id", this.getDb_id());
        res.put("team_user_id", this.getTeamUser().getDb_id());
        res.put("team_card_id", this.getTeamUser().getDb_id());
        return res;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("request", this.getJson());
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JoinTeamCardRequest that = (JoinTeamCardRequest) o;

        return db_id.equals(that.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public abstract TeamCardReceiver accept(String symmetric_key, HibernateQuery hibernateQuery) throws HttpServletException;
}