package com.Ease.Team;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
public class Channel {

    public static Channel createChannel(String channelName, String company_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO channels values(?, ?, ?);");
        request.setNull();
        request.setInt(company_id);
        request.setString(channelName);
        String db_id = request.set().toString();
        int single_id = sm.getNextSingle_id();
        return new Channel(db_id, single_id, channelName, new LinkedList<TeamUser>());
    }

    public static List<Channel> loadChannels(String company_id, Map<String, TeamUser> teamUserMap, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT id FROM channels WHERE company_id = ?;");
        request.setInt(company_id);
        DatabaseResult rs = request.get();
        List<Channel> channels = new LinkedList<Channel>();
        while (rs.next())
            channels.add(loadChannel(rs.getString(1), teamUserMap, sm));
        return channels;
    }

    public static Channel loadChannel(String db_id, Map<String, TeamUser> teamUserMap, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM channels WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This channel does not exist");
        String channelname = rs.getString(3);
        request = db.prepareRequest("SELECT * FROM channelsAndTeamUsersMap WHERE team_id = ?;");
        request.setInt(db_id);
        rs = request.get();
        List<TeamUser> teamUsers = new LinkedList<TeamUser>();
        while (rs.next())
            teamUsers.add(teamUserMap.get(rs.getString(2)));
        int single_id = sm.getNextSingle_id();
        return new Channel(db_id, single_id, channelname, teamUsers);
    }

    protected String db_id;
    protected int single_id;
    protected String channelName;
    protected List<TeamUser> teamUsers;

    public Channel(String db_id, int single_id, String channelName, List<TeamUser> teamUsers) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.channelName = channelName;
        this.teamUsers = teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void addTeamUser(TeamUser teamUser, ServletManager sm) throws GeneralException {
        if (this.teamUsers.contains(teamUser))
            throw new GeneralException(ServletManager.Code.ClientError, "TeamUser already present in this channel");
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO channelsAndTeamUsersMap values(?, ?, ?);");
        request.setNull();
        request.setInt(this.db_id);
        request.setInt(teamUser.getDb_id());
        request.set();
        this.teamUsers.add(teamUser);
    }

    public void addTeamUsers(List<TeamUser> teamUsers, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request;
        for(TeamUser teamUser : teamUsers) {
            if (this.teamUsers.contains(teamUser))
                throw new GeneralException(ServletManager.Code.ClientError, "Some of teamUsers already present in this channel");
            request = db.prepareRequest("INSERT INTO channelsAndTeamUsersMap values(?, ?, ?);");
            request.setNull();
            request.setInt(this.db_id);
            request.setInt(teamUser.getDb_id());
            request.set();
        }
        db.commitTransaction(transaction);
        this.teamUsers.addAll(teamUsers);
    }

    public void removeTeamUser(TeamUser teamUser, ServletManager sm) throws GeneralException {
        if (!this.teamUsers.contains(teamUser))
            throw new GeneralException(ServletManager.Code.ClientError, "TeamUser not present in this channel");
        DatabaseRequest request = sm.getDB().prepareRequest("DELETE FROM channelsAndTeamUsersMap WHERE team_id = ? AND team_user_id = ?;");
        request.setInt(this.db_id);
        request.setInt(teamUser.getDb_id());
        request.set();
        this.teamUsers.remove(teamUser);
    }

    public void deleteFromDatabase(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM channelsAndTeamUsersMap WHERE team_id = ?;");
        request.setInt(this.db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM channels WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
        db.commitTransaction(transaction);
    }

    public String getChannelName() {
        return this.channelName;
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("single_id", this.single_id);
        res.put("channelName", this.channelName);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.teamUsers)
            teamUsers.add(teamUser.getJson());
        res.put("teamUsers", teamUsers);
        return res;
    }
}
