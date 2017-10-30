package com.Ease.Team;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.websocketV1.WebSocketManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity(name = "channels")
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

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private TeamUser room_manager;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "team_user_id"))
    protected Set<TeamUser> teamUsers = ConcurrentHashMap.newKeySet();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pendingJoinChannelRequests", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "teamUser_id"))
    private Set<TeamUser> pending_teamUsers = ConcurrentHashMap.newKeySet();

    @Transient
    private WebSocketManager webSocketManager = new WebSocketManager();

    public Channel(Team team, String name, String purpose, TeamUser room_manager) {
        this.team = team;
        this.name = name;
        this.purpose = purpose;
        this.room_manager = room_manager;
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

    public TeamUser getRoom_manager() {
        return room_manager;
    }

    public void setRoom_manager(TeamUser room_manager) {
        this.room_manager = room_manager;
    }

    public Set<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public Set<TeamUser> getPending_teamUsers() {
        return pending_teamUsers;
    }

    public void setTeamUsers(Set<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void setPending_teamUsers(Set<TeamUser> pending_teamUsers) {
        this.pending_teamUsers = pending_teamUsers;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.getTeamUsers().add(teamUser);
        teamUser.addChannel(this);
    }

    public void removeTeamUser(TeamUser teamUser) {
        this.getTeamUsers().remove(teamUser);
        teamUser.removeChannel(this);
    }

    public void addPendingTeamUser(TeamUser teamUser) {
        this.getPending_teamUsers().add(teamUser);
        teamUser.addPending_channel(this);
    }

    public void removePendingTeamUser(TeamUser teamUser) {
        this.getPending_teamUsers().remove(teamUser);
        teamUser.removePending_channel(this);
    }

    public JSONObject getJson() {
        JSONObject res = this.getSimpleJson();
        return res;
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public JSONObject getSimpleJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.getDb_id());
        jsonObject.put("name", this.getName());
        jsonObject.put("purpose", this.getPurpose());
        JSONArray jsonArray = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            jsonArray.add(teamUser.getDb_id());
        jsonObject.put("user_ids", jsonArray);
        JSONArray joinRequests = new JSONArray();
        for (TeamUser teamUser : this.getPending_teamUsers())
            joinRequests.add(teamUser.getDb_id());
        jsonObject.put("join_requests", joinRequests);
        jsonObject.put("default", this.isDefault());
        jsonObject.put("room_manager_id", this.getRoom_manager().getDb_id());
        JSONArray apps = new JSONArray();
        for (ShareableApp shareableApp : this.getTeam().getAppManager().getShareableApps().values()) {
            if (shareableApp.getChannel() == this)
                apps.add(((App) shareableApp).getDBid());
        }
        jsonObject.put("app_ids", apps);
        jsonObject.put("team_id", team.getDb_id());
        return jsonObject;
    }

    public boolean isDefault() {
        return this.getName().equals("openspace");
    }

    public void editName(String name) {
        if (name.equals(this.getName()))
            return;
        this.name = name;
    }

    public void editPurpose(String purpose) {
        if (purpose.equals(this.getPurpose()))
            return;
        this.purpose = purpose;
    }

    public void delete() {
        for (TeamUser teamUser : this.getTeamUsers())
            this.removeTeamUser(teamUser);
        for (TeamUser teamUser : this.getPending_teamUsers())
            this.removePendingTeamUser(teamUser);
    }

    public JSONObject getOrigin() {
        JSONObject origin = new JSONObject();
        origin.put("team_id", this.getTeam().getDb_id());
        return origin;
    }

    public WebSocketManager getWebSocketManager() {
        return webSocketManager;
    }

    public void setWebSocketManager(WebSocketManager webSocketManager) {
        this.webSocketManager = webSocketManager;
    }
}
