package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 04/05/2017.
 */
@Entity
@Table(name = "teamUserChannels")
public class TeamUserChannel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    @ManyToOne
    @JoinColumn(name = "teamUser_owner_id", nullable = false)
    protected TeamUser teamUser_owner;

    @ManyToOne
    @JoinColumn(name = "teamUser_tenant_id", nullable = false)
    protected TeamUser teamUser_tenant;

    @Transient
    protected List<App> apps = new LinkedList<>();

    public TeamUserChannel(Team team, TeamUser teamUser_owner, TeamUser teamUser_tenant) {
        this.team = team;
        this.teamUser_owner = teamUser_owner;
        this.teamUser_tenant = teamUser_tenant;
    }

    public TeamUserChannel() {

    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamUser getTeamUser_owner() {
        return teamUser_owner;
    }

    public void setTeamUser_owner(TeamUser teamUser_owner) {
        this.teamUser_owner = teamUser_owner;
    }

    public TeamUser getTeamUser_tenant() {
        return teamUser_tenant;
    }

    public void setTeamUser_tenant(TeamUser teamUser_tenant) {
        this.teamUser_tenant = teamUser_tenant;
    }

    public List<App> getApps() {
        return apps;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("teamUser_owner_id", this.teamUser_owner.getDb_id());
        res.put("teamUser_tenant_id", this.teamUser_tenant.getDb_id());
        return res;
    }
}