package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Hibernate.HibernateQuery;
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
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = {@JoinColumn(name = "channel_id")}, inverseJoinColumns = {@JoinColumn(name = "team_user_id")})
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @Transient
    protected List<SharedApp> sharedApps = new LinkedList<>();

    @Transient
    protected Map<String, SharedApp> sharedAppIdMap = new HashMap<>();

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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public List<SharedApp> getSharedApps() {
        return sharedApps;
    }

    public void setSharedApps(List<SharedApp> sharedApps) {
        this.sharedApps = sharedApps;
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
        for (SharedApp sharedApp : this.getSharedApps())
            apps.add(sharedApp.getSharedJSON());
        res.put("apps", apps);
        res.put("purpose", this.purpose);
        return res;
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public void addSharedApp(SharedApp sharedApp) {
        this.sharedApps.add(sharedApp);
    }

    public JSONObject getSimpleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getDb_id());
        jsonObject.put("name", this.getName());
        jsonObject.put("purpose", this.getPurpose());
        return jsonObject;
    }

    public void editName(String name) {
        HibernateQuery hibernateQuery = new HibernateQuery();
        if (name.equals(this.getName()))
            return;
        this.name = name;
        hibernateQuery.saveOrUpdateObject(this);
        hibernateQuery.commit();
    }

    public void editPurpose(String purpose) {
        HibernateQuery hibernateQuery = new HibernateQuery();
        if (purpose.equals(this.getPurpose()))
            return;
        this.purpose = purpose;
        hibernateQuery.saveOrUpdateObject(this);
        hibernateQuery.commit();
    }
}
