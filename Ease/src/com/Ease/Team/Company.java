package com.Ease.Team;

import com.Ease.Utils.*;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
public class Company {

    public static Company createCompany(String companyName, String adminEmail, String adminFirstName, String adminLastName, ServletManager sm) throws GeneralException {

        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();

        /* Create company, admin and general team */
        DatabaseRequest request = db.prepareRequest("INSERT INTO companies values(?, ?);");
        request.setNull();
        request.setString(companyName);
        String db_id = request.set().toString();
        TeamUser admin = TeamUser.createAdminTeamUser(adminEmail, db_id, adminFirstName, adminLastName, sm);
        Team generalTeam = Team.createTeam("General", db_id, sm);

        int single_id = sm.getNextSingle_id();

        /* Create the first team user(admin) */

        Map<String, TeamUser> teamUserMap = new HashMap<String, TeamUser>();
        teamUserMap.put(admin.getDb_id(), admin);

        /* Create the first team */
        List<Team> teams = new LinkedList<Team>();
        teams.add(generalTeam);

        return new Company(db_id, single_id, companyName, teamUserMap, teams);
    }

    public static Company loadCompany(String db_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT * FROM companies WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This company does not exist");
        Map<String, TeamUser> teamUserMap = TeamUser.loadTeamUsers(db_id, sm);
        List<Team> teams = Team.loadTeams(db_id, teamUserMap, sm);
        int single_id = sm.getNextSingle_id();
        return new Company(db_id, single_id, rs.getString(2), teamUserMap, teams);
    }

    protected String db_id;
    protected int single_id;
    protected String companyName;
    protected List<TeamUser> teamUsers;
    protected Map<String, TeamUser> teamUserMap;
    protected List<Team> teams;

    public Company(String db_id, int single_id, String companyName, Map<String, TeamUser> teamUserMap) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.companyName = companyName;
        this.teamUserMap = teamUserMap;
        this.teamUsers = (List<TeamUser>) teamUserMap.values();
        this.teams = new LinkedList<Team>();
    }

    public Company(String db_id, int single_id, String companyName, Map<String, TeamUser> teamUserMap, List<Team> teams) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.companyName = companyName;
        this.teamUserMap = teamUserMap;
        this.teamUsers = (List<TeamUser>) teamUserMap.values();
        this.teams = teams;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public void createTeam(String teamName, ServletManager sm) throws GeneralException {
        Team team = Team.createTeam(teamName, this.db_id, sm);
        this.teams.add(team);
    }

    public void createTeamUser(String email, String firstName, String lastName, int permissions, ServletManager sm) throws GeneralException {
        TeamUser teamUser = TeamUser.createTeamUser(email, this.db_id, firstName, lastName, TeamUserPermissions.createTeamUserPermissions(permissions, sm), sm);
        this.teamUserMap.put(teamUser.getDb_id(), teamUser);
        this.getGeneralTeam().addTeamUser(teamUser, sm);
    }

    public void deleteFromDatabase(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        for (Team team : this.teams)
            team.deleteFromDatabase(sm);
        for (TeamUser teamUser : this.teamUsers)
            teamUser.deleteFromDatabase(sm);
        DatabaseRequest request = db.prepareRequest("DELETE FROM companies WHERE id = ?;");
        request.setInt(this.db_id);
        request.set();
        db.commitTransaction(transaction);
    }

    public Team getGeneralTeam() throws GeneralException {
        for (Team team : this.teams) {
            if (team.getTeamName().equals("General"))
                return team;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "No general team, contact Ease.space support");
    }
}
