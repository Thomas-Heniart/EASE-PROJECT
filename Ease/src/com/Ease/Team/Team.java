package com.Ease.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.*;
import com.google.common.primitives.UnsignedInts;
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
@Table(name = "teams")
public class Team {

    public static List<Team> loadTeams() {
        List<Team> teams = new LinkedList<>();
        HibernateQuery query = new HibernateQuery();
        query.queryString("SELECT t FROM Team t");
        teams = query.list();
        for (Team team : teams)
            team.lazyInitialize();
        query.commit();
        return teams;
    }

    @Id
    @GeneratedValue
    @Column(name = "id")
    protected Integer db_id;

    @Column(name = "name")
    protected String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    protected List<Channel> channels = new LinkedList<>();

    @Transient
    protected Map<Integer, Channel> channelIdMap = new HashMap<>();

    @Transient
    protected Map<Integer, TeamUser> teamUserIdMap = new HashMap<>();

    public Team(String name, List<TeamUser> teamUsers, List<Channel> channels) {
        this.name = name;
        this.teamUsers = teamUsers;
        this.channels = channels;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    public Integer getDb_id() {
        return db_id;
    }

    public void setDb_id(Integer db_id) {
        this.db_id = db_id;
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

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    public void lazyInitialize() {
        for (Channel channel : this.getChannels())
            this.channelIdMap.put(channel.getDb_id(), channel);
        for (TeamUser teamUser : this.teamUsers)
            this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
    }

    public Channel getChannelWithId(Integer channel_id) throws GeneralException {
        Channel channel = this.channelIdMap.get(channel_id);
        if (channel == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This channel does not exist");
        return channel;
    }

    public TeamUser getTeamUserWithId(Integer teamUser_id) throws GeneralException {
        TeamUser teamUser = this.teamUserIdMap.get(teamUser_id);
        if (teamUser == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This teamUser does not exist");
        return teamUser;
    }

    public void addTeamUser(TeamUser teamUser) {
        this.teamUsers.add(teamUser);
        this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
        this.channelIdMap.put(channel.getDb_id(), channel);
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("id", this.db_id);
        /* Channels and TeamUsers JSON */
        return res;
    }
}
