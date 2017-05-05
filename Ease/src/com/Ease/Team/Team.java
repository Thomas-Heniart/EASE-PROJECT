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

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<TeamUser> teamUsers = new LinkedList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<Channel> channels = new LinkedList<>();

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    protected List<TeamUserChannel> teamUserChannels = new LinkedList<>();

    @Transient
    protected Map<Integer, Channel> channelIdMap = new HashMap<>();

    @Transient
    protected Map<Integer, TeamUser> teamUserIdMap = new HashMap<>();

    @Transient
    protected Map<Integer, TeamUserChannel> teamUserChannelIdMap = new HashMap<>();

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

    public List<TeamUserChannel> getTeamUserChannels() {
        return teamUserChannels;
    }

    public void lazyInitialize() {
        for (Channel channel : this.getChannels())
            this.channelIdMap.put(channel.getDb_id(), channel);
        for (TeamUser teamUser : this.teamUsers) {
            this.teamUserIdMap.put(teamUser.getDb_id(), teamUser);
            System.out.println("Permissions member: " + teamUser.getTeamUserPermissions().haveRole(TeamUserPermissions.Role.MEMBER));
            System.out.println("Permissions moderator: " + teamUser.getTeamUserPermissions().haveRole(TeamUserPermissions.Role.MODERATOR));
            System.out.println("Permissions admin: " + teamUser.getTeamUserPermissions().haveRole(TeamUserPermissions.Role.ADMINISTRATOR));
        }

    }

    public Channel getChannelWithId(Integer channel_id) throws GeneralException {
        Channel channel = this.channelIdMap.get(channel_id);
        if (channel == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This channel does not exist");
        return channel;
    }

    public TeamUser getTeamUserWithId(Integer teamUser_id) throws GeneralException {
        for (Integer i : this.teamUserIdMap.keySet())
            System.out.println("Integer key: " + i);
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

    public Channel getGeneralChannel() throws GeneralException {
        for (Channel channel : this.getChannels()) {
            if (channel.getName().equals("General"))
                return channel;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "No general channel");
    }

    public void removeTeamUser(TeamUser teamUser) {
        for(Channel channel : this.getChannels())
            channel.removeTeamUser(teamUser);
        this.teamUserIdMap.remove(teamUser.getDb_id());
        this.teamUsers.remove(teamUser);
    }

    public void removeChannel(Channel channel) {
        this.channelIdMap.remove(channel.getDb_id());
        this.channels.remove(channel);
    }

    public void edit(JSONObject editJson) {
        String name = (String) editJson.get("name");
        if (name != null)
            this.name = name;
    }

    public TeamUserChannel createTeamUserChannel(TeamUser teamUser, TeamUser teamUser_tenant) {
        TeamUserChannel teamUserChannel = new TeamUserChannel(this, teamUser, teamUser_tenant);
        this.teamUserChannels.add(teamUserChannel);
        this.teamUserChannelIdMap.put(teamUserChannel.getDb_id(), teamUserChannel);
        return teamUserChannel;
    }

    public TeamUserChannel getTeamUserChannel(Integer teamUserChannel_id) throws GeneralException {
        TeamUserChannel teamUserChannel = this.teamUserChannelIdMap.get(teamUserChannel_id);
        if (teamUserChannel == null)
            throw new GeneralException(ServletManager.Code.ClientError, "No such teamUserChannel");
        return teamUserChannel;
    }

    public void removeTeamUserChannel(Integer teamUserChannel_id) throws GeneralException {
        TeamUserChannel teamUserChannel = this.getTeamUserChannel(teamUserChannel_id);
        this.removeTeamUserChannel(teamUserChannel);
    }

    public void removeTeamUserChannel(TeamUserChannel teamUserChannel) {
        this.teamUserChannels.remove(teamUserChannel);
        this.teamUserChannelIdMap.remove(teamUserChannel.getDb_id());
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("name", this.name);
        res.put("id", this.db_id);
        JSONArray channels = new JSONArray();
        for (Channel channel : this.getChannels())
            channels.add(channel.getJson());
        res.put("channels", channels);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.getTeamUsers())
            teamUsers.add(teamUser.getJson());
        res.put("teamUsers", teamUsers);
        JSONArray teamUserChannels = new JSONArray();
        for (TeamUserChannel teamUserChannel : this.getTeamUserChannels())
            teamUserChannels.add(teamUserChannel.getJson());
        res.put("teamUserChannels", teamUserChannels);
        return res;
    }
}
