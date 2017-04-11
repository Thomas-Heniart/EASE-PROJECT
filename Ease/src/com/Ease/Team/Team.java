package com.Ease.Team;

import com.Ease.Utils.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
public class Team {

    public static Team createTeam(String teamName, String company_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO teams values(?, ?, ?);");
        request.setNull();
        request.setInt(company_id);
        request.setString(teamName);
        String db_id = request.set().toString();
        int single_id = sm.getNextSingle_id();
        return new Team(db_id, single_id, teamName, new LinkedList<TeamUser>());
    }

    public static List<Team> loadTeams(String company_id, Map<String, TeamUser> teamUserMap, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT id FROM teams WHERE company_id = ?;");
        request.setInt(company_id);
        DatabaseResult rs = request.get();
        List<Team> teams = new LinkedList<Team>();
        while (rs.next())
            teams.add(loadTeam(rs.getString(1), teamUserMap, sm));
        return teams;
    }

    public static Team loadTeam(String db_id, Map<String, TeamUser> teamUserMap, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT * FROM teams WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This team does not exist");
        String teamName = rs.getString(3);
        request = db.prepareRequest("SELECT * FROM teamsAndTeamUsersMap WHERE team_id = ?;");
        request.setInt(db_id);
        rs = request.get();
        List<TeamUser> teamUsers = new LinkedList<TeamUser>();
        while (rs.next())
            teamUsers.add(teamUserMap.get(rs.getString(2)));
        int single_id = sm.getNextSingle_id();
        return new Team(db_id, single_id, teamName, teamUsers);
    }

    protected String db_id;
    protected int single_id;
    protected String teamName;
    protected List<TeamUser> teamUsers;

    public Team(String db_id, int single_id, String teamName, List<TeamUser> teamUsers) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.teamName = teamName;
        this.teamUsers = teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void addTeamUser(TeamUser teamUser, ServletManager sm) throws GeneralException {
        if (this.teamUsers.contains(teamUser))
            throw new GeneralException(ServletManager.Code.ClientError, "TeamUser already present in this team");
        DatabaseRequest request = sm.getDB().prepareRequest("INSERT INTO teamsAndTeamUsersMap values(?, ?, ?);");
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
                throw new GeneralException(ServletManager.Code.ClientError, "Some of teamUsers already present in this team");
            request = db.prepareRequest("INSERT INTO teamsAndTeamUsersMap values(?, ?, ?);");
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
            throw new GeneralException(ServletManager.Code.ClientError, "TeamUser not present in this team");
        DatabaseRequest request = sm.getDB().prepareRequest("DELETE FROM teamsAndTeamUsersMap WHERE team_id = ? AND team_user_id = ?;");
        request.setInt(this.db_id);
        request.setInt(teamUser.getDb_id());
        request.set();
        this.teamUsers.remove(teamUser);
    }

    public void deleteFromDatabase(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM teamsAndTeamUsersMap WHERE team_id = ?;");
        request.setInt(this.db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM teams WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
        db.commitTransaction(transaction);
    }

    public String getTeamName() {
        return this.teamName;
    }
}
