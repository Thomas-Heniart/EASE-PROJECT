package com.Ease.Team;

import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by thomas on 10/04/2017.
 */
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "channels")
public class Channel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    protected Team team;

    @Column(name = "name")
    protected String name;

    @Column(name = "purpose")
    protected String purpose;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private TeamUser room_manager;

    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "channelAndTeamUserMap", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "team_user_id"))
    protected Set<TeamUser> teamUsers = ConcurrentHashMap.newKeySet();

    @ManyToMany
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(name = "pendingJoinChannelRequests", joinColumns = @JoinColumn(name = "channel_id"), inverseJoinColumns = @JoinColumn(name = "teamUser_id"))
    private Set<TeamUser> pending_teamUsers = ConcurrentHashMap.newKeySet();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<TeamCard> teamCardSet = ConcurrentHashMap.newKeySet();

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

    public synchronized Set<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public synchronized Set<TeamUser> getPending_teamUsers() {
        return pending_teamUsers;
    }

    public void setTeamUsers(Set<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void setPending_teamUsers(Set<TeamUser> pending_teamUsers) {
        this.pending_teamUsers = pending_teamUsers;
    }

    public synchronized Set<TeamCard> getTeamCardSet() {
        return teamCardSet;
    }

    public void setTeamCardSet(Set<TeamCard> teamCardSet) {
        this.teamCardSet = teamCardSet;
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
            jsonArray.put(teamUser.getDb_id());
        jsonObject.put("team_user_ids", jsonArray);
        JSONArray joinRequests = new JSONArray();
        for (TeamUser teamUser : this.getPending_teamUsers())
            joinRequests.put(teamUser.getDb_id());
        jsonObject.put("join_requests", joinRequests);
        jsonObject.put("default", this.isDefault());
        jsonObject.put("room_manager_id", this.getRoom_manager().getDb_id());
        JSONArray teamCards = new JSONArray();
        this.getTeamCardSet().stream().sorted((t1, t2) -> Long.compare(t2.getCreation_date().getTime(), t1.getCreation_date().getTime())).forEach(teamCard -> teamCards.put(teamCard.getDb_id()));
        jsonObject.put("team_card_ids", teamCards);
        jsonObject.put("team_id", team.getDb_id());
        return jsonObject;
    }

    public JSONObject getWebSocketJson() {
        JSONObject res = new JSONObject();
        res.put("room", this.getJson());
        return res;
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

    public void addTeamCard(TeamCard teamCard) {
        this.getTeamCardSet().add(teamCard);
    }

    public void removeTeamCard(TeamCard teamCard) {
        this.getTeamCardSet().remove(teamCard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        return db_id.equals(channel.db_id);
    }

    @Override
    public int hashCode() {
        return db_id.hashCode();
    }

    public TeamUser getTeamUser(Integer teamUser_id) throws HttpServletException {
        TeamUser teamUser = this.getTeamUsers().stream().filter(teamUser1 -> teamUser1.getDb_id().equals(teamUser_id)).findAny().orElse(null);
        if (teamUser == null)
            throw new HttpServletException(HttpStatus.BadRequest, "No such teamUser in this team");
        return teamUser;
    }
}
