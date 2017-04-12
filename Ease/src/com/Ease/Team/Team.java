package com.Ease.Team;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
public class Team {

    public static Team createTeam(String teamName, String adminEmail, String adminFirstName, String adminLastName, ServletManager sm) throws GeneralException {

        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();

        /* Create team, admin and general team */
        DatabaseRequest request = db.prepareRequest("INSERT INTO teams values(?, ?);");
        request.setNull();
        request.setString(teamName);
        String db_id = request.set().toString();
        int single_id = sm.getNextSingle_id();
        Team team = new Team(db_id, single_id, teamName);
        team.createChannel("General", sm);
        team.createTeamAdmin(adminEmail, adminFirstName, adminLastName, sm);
        return team;
    }

    public static Map<String, Team> loadTeams(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM teams;");
        DatabaseResult rs = request.get();
        Map<String, Team> teamMap = new HashMap<String, Team>();
        while(rs.next())
            teamMap.put(rs.getString(1), null);
        return teamMap;
    }

    public static Team loadTeam(String db_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT * FROM teams WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This team does not exist");
        int single_id = sm.getNextSingle_id();
        Team team = new Team(db_id, single_id, rs.getString(2));
        team.loadTeamUsers(sm);
        team.loadChannels(sm);
        return team;
    }

    protected String db_id;

    protected int single_id;
    protected String teamName;
    protected List<TeamUser> teamUsers;
    protected Map<String, TeamUser> teamUserMap;
    protected List<Channel> channels;
    public Team(String db_id, int single_id, String teamName) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.teamName = teamName;
        this.teamUserMap = new HashMap<String, TeamUser>();
        this.channels = new LinkedList<Channel>();
    }

    public Team(String db_id, int single_id, String teamName, Map<String, TeamUser> teamUserMap, List<Channel> channels) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.teamName = teamName;
        this.teamUserMap = teamUserMap;
        this.teamUsers = (List<TeamUser>) teamUserMap.values();
        this.channels = channels;
    }

    /* Initializer */

    public void loadTeamUsers(ServletManager sm) throws GeneralException {
        this.teamUserMap = TeamUser.loadTeamUsers(this, sm);
    }

    public void loadChannels(ServletManager sm) throws GeneralException {
        this.channels = Channel.loadChannels(this.db_id, this.teamUserMap, sm);
    }

    /* Getters and setters */

    public String getDb_id() {
        return db_id;
    }

    public Channel getGeneralTeam() throws GeneralException {
        for (Channel channel : this.channels) {
            if (channel.getChannelName().equals("General"))
                return channel;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "No general channel, contact Ease.space support");
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }

    /* Utils */

    public void addTeamUser(TeamUser teamUser) {
        this.teamUserMap.put(teamUser.getDb_id(), teamUser);
    }

    public void addChannel(Channel channel) {
        this.channels.add(channel);
    }

    public Channel createChannel(String channelName, ServletManager sm) throws GeneralException {
        Channel channel = Channel.createChannel(channelName, this.db_id, sm);
        this.addChannel(channel);
        return channel;
    }

    public void createTeamAdmin(String email, String firstName, String lastName, ServletManager sm) throws GeneralException {
        TeamUser teamUser = TeamUser.createAdminTeamUser(email, this, firstName, lastName, sm);
        this.addTeamUser(teamUser);
        this.getGeneralTeam().addTeamUser(teamUser, sm);
    }

    public void createTeamUser(String email, String firstName, String lastName, int permissions, ServletManager sm) throws GeneralException {
        TeamUser teamUser = TeamUser.createTeamUser(email, this, firstName, lastName, TeamUserPermissions.createTeamUserPermissions(permissions, sm), sm);
        this.addTeamUser(teamUser);
        this.getGeneralTeam().addTeamUser(teamUser, sm);
    }

    public void deleteFromDatabase(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        for (Channel channel : this.channels)
            channel.deleteFromDatabase(sm);
        for (TeamUser teamUser : this.teamUsers)
            teamUser.deleteFromDatabase(sm);
        DatabaseRequest request = db.prepareRequest("DELETE FROM teams WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
        db.commitTransaction(transaction);
    }

    public TeamUser getTeamUserWithDbId(String team_user_id) throws GeneralException {
        TeamUser teamUser = teamUserMap.get(team_user_id);
        if (teamUser == null)
            throw new GeneralException(ServletManager.Code.ClientError, "This user does not exist");
        return teamUser;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("teamName", this.teamName);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.teamUserMap.values())
            teamUsers.add(teamUser.getJson());
        res.put("teamUsers", teamUsers);
        JSONArray channels = new JSONArray();
        for (Channel channel : this.channels)
            channels.add(channel.getJson());
        res.put("channels", channels);
        return res;
    }
}
