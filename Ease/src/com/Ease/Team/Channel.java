package com.Ease.Team;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    @Column(name = "name")
    protected String name;

    @ManyToMany
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = { @JoinColumn(name = "channel_id") }, inverseJoinColumns = { @JoinColumn(name = "team_user_id") })
    protected List<TeamUser> teamUsers = new LinkedList<>();

    public Channel(Team team, String name, List<TeamUser> teamUsers) {
        this.team = team;
        this.name = name;
        this.teamUsers = teamUsers;
    }

    public Channel(Team team, String name) {
        this.team = team;
        this.name = name;
    }

    public Channel(String name) {
        this.name = name;
    }

    public Channel() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.teamUsers.add(teamUser);
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.teamUsers.remove(teamUser);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("name", this.name);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            teamUsers.add(teamUser.getJson());
        return res;
    }
}
