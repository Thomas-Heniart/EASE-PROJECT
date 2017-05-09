package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.HashMap;
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

    @Column(name = "purpose")
    protected String purpose;

    @ManyToMany
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = { @JoinColumn(name = "channel_id") }, inverseJoinColumns = { @JoinColumn(name = "team_user_id") })
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @Transient
    protected List<App> apps = new LinkedList<>();

    @Transient
    protected Map<String, App> appIdMap = new HashMap<>();

    public Channel(Team team, String name, String purpose, List<TeamUser> teamUsers) {
        this.team = team;
        this.name = name;
        this.teamUsers = teamUsers;
        this.purpose = purpose;
    }

    public Channel(Team team, String name, String purpose) {
        this.team = team;
        this.name = name;
        this.purpose = purpose;
    }

    public Channel(String name, String purpose) {
        this.name = name;
        this.purpose = purpose;
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

    public List<App> getApps() {
        return apps;
    }

    public void setApps(List<App> apps) {
        this.apps = apps;
    }

    public void addApp(App app) {
        this.apps.add(app);
        this.appIdMap.put(app.getDBid(), app);
    }

    public void removeApp(App app) {
        this.apps.remove(app);
        this.appIdMap.remove(app.getDBid());
    }

    public void removeAppWithId(String app_id) throws GeneralException {
        App app = this.getAppWithId(app_id);
        this.removeApp(app);
    }

    public App getAppWithId(String app_id) throws GeneralException {
        App app = this.appIdMap.get(app_id);
        if (app == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No such app in this channel");
        return app;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("id", this.db_id);
        res.put("name", this.name);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            teamUsers.add(teamUser.getDb_id());
        res.put("userIds", teamUsers);
        JSONArray apps = new JSONArray();
        for (App app : this.getApps())
            apps.add(app.getJSON());
        res.put("apps", apps);
        res.put("desc", this.purpose);
        return res;
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }
}
