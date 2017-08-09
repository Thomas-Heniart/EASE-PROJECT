package com.Ease.Team;

import com.Ease.Utils.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

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

    @Column(name = "creator_id")
    protected Integer creator_id;

    @ManyToMany
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = {@JoinColumn(name = "channel_id")}, inverseJoinColumns = {@JoinColumn(name = "team_user_id")})
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @ManyToMany
    @JoinTable(name = "pendingJoinChannelRequests", joinColumns = {@JoinColumn(name = "channel_id")}, inverseJoinColumns = {@JoinColumn(name = "teamUser_id")})
    protected List<TeamUser> pending_teamUsers = new LinkedList<>();

    public Channel(Team team, String name, String purpose, Integer creator_id) {
        this.team = team;
        this.name = name;
        this.purpose = purpose;
        this.creator_id = creator_id;
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

    public Integer getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Integer creator_id) {
        this.creator_id = creator_id;
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public List<TeamUser> getPending_teamUsers() {
        return pending_teamUsers;
    }

    public void setPending_teamUsers(List<TeamUser> pending_teamUsers) {
        this.pending_teamUsers = pending_teamUsers;
    }

    private void addTeamUser(TeamUser teamUser) {
        this.teamUsers.add(teamUser);
    }

    public void addTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            if (this.getTeamUsers().contains(teamUser))
                throw new HttpServletException(HttpStatus.BadRequest, "This channel already contains this user");
            if (this.getPending_teamUsers().contains(teamUser))
                this.removePendingTeamUser(teamUser, db);
            DatabaseRequest request = db.prepareRequest("INSERT INTO channelAndTeamUserMap values (null, ?, ?);");
            request.setInt(this.getDb_id());
            request.setInt(teamUser.getDb_id());
            request.set();
            db.commitTransaction(transaction);
            this.addTeamUser(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    private void removeTeamUser(TeamUser teamUser) {
        this.teamUsers.remove(teamUser);
    }

    public void removeTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        System.out.println("Remove contains teamUser: " + this.getTeamUsers().contains(teamUser));
        try {
            if (this.getPending_teamUsers().contains(teamUser))
                this.removePendingTeamUser(teamUser, db);
            else {
                DatabaseRequest request = db.prepareRequest("DELETE FROM channelAndTeamUserMap WHERE team_user_id = ? AND channel_id = ?;");
                request.setInt(teamUser.getDb_id());
                request.setInt(this.getDb_id());
                request.set();
                System.out.println("Remove contains teamUser: " + this.getTeamUsers().contains(teamUser));
                this.removeTeamUser(teamUser);
            }
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }

    private void addPendingTeamUser(TeamUser teamUser) {
        this.pending_teamUsers.add(teamUser);
    }

    public void addPendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        try {
            if (this.getTeamUsers().contains(teamUser))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already in this channel");
            if (this.getPending_teamUsers().contains(teamUser))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already pending for this channel");
            DatabaseRequest request = db.prepareRequest("INSERT INTO pendingJoinChannelRequests VALUE (null, ?, ?);");
            request.setInt(this.getDb_id());
            request.setInt(teamUser.getDb_id());
            request.set();
            this.addPendingTeamUser(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    private void removePendingTeamUser(TeamUser teamUser) {
        this.pending_teamUsers.remove(teamUser);
    }

    public void removePendingTeamUser(TeamUser teamUser, DataBaseConnection db) throws HttpServletException {
        try {
            DatabaseRequest request = db.prepareRequest("DELETE FROM pendingJoinChannelRequests WHERE channel_id = ? AND teamUser_id = ?");
            request.setInt(this.getDb_id());
            request.setInt(teamUser.getDb_id());
            request.set();
            this.removePendingTeamUser(teamUser);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
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
        jsonObject.put("userIds", jsonArray);
        JSONArray joinRequests = new JSONArray();
        for (TeamUser teamUser : this.getPending_teamUsers())
            joinRequests.add(teamUser.getDb_id());
        jsonObject.put("join_requests", joinRequests);
        return jsonObject;
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

    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder();
        hcb.append(this.db_id);
        return hcb.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TeamUser))
            return false;
        TeamUser teamUser = (TeamUser) obj;
        EqualsBuilder eb = new EqualsBuilder();
        eb.append(this.db_id, teamUser.db_id);
        return eb.isEquals();
    }

    public void delete(DataBaseConnection db) throws HttpServletException {
        try {
            int transaction = db.startTransaction();
            List<TeamUser> teamUsersToRemove = new LinkedList<>();
            teamUsersToRemove.addAll(this.getTeamUsers());
            teamUsersToRemove.addAll(this.getPending_teamUsers());
            System.out.println("TeamUsers size: " + teamUsersToRemove.size());
            for (TeamUser teamUser : teamUsersToRemove) {
                this.removeTeamUser(teamUser, db);
            }

            db.commitTransaction(transaction);
        } catch (GeneralException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }
}
