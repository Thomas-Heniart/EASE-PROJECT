package com.Ease.Team;

import com.Ease.Utils.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
        int single_id = sm.getNextSingle_id();
        Company company = new Company(db_id, single_id, companyName);
        company.createTeam("General", sm);
        company.createTeamAdmin(adminEmail, adminFirstName, adminLastName, sm);
        return company;
    }

    public static Map<String, Company> loadCompanies(DataBaseConnection db) throws GeneralException {
        DatabaseRequest request = db.prepareRequest("SELECT * FROM companies;");
        DatabaseResult rs = request.get();
        Map<String, Company> companyMap = new HashMap<String, Company>();
        while(rs.next())
            companyMap.put(rs.getString(1), null);
        return companyMap;
    }

    public static Company loadCompany(String db_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT * FROM companies WHERE id = ?;");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This company does not exist");
        int single_id = sm.getNextSingle_id();
        Company company = new Company(db_id, single_id, rs.getString(2));
        company.loadTeamUsers(sm);
        company.loadTeams(sm);
        return company;
    }

    protected String db_id;

    protected int single_id;
    protected String companyName;
    protected List<TeamUser> teamUsers;
    protected Map<String, TeamUser> teamUserMap;
    protected List<Team> teams;
    public Company(String db_id, int single_id, String companyName) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.companyName = companyName;
        this.teamUserMap = new HashMap<String, TeamUser>();
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

    /* Initializer */

    public void loadTeamUsers(ServletManager sm) throws GeneralException {
        this.teamUserMap = TeamUser.loadTeamUsers(this, sm);
    }

    public void loadTeams(ServletManager sm) throws GeneralException {
        this.teams = Team.loadTeams(this.db_id, this.teamUserMap, sm);
    }

    /* Getters and setters */

    public String getDb_id() {
        return db_id;
    }

    public Team getGeneralTeam() throws GeneralException {
        for (Team team : this.teams) {
            if (team.getTeamName().equals("General"))
                return team;
        }
        throw new GeneralException(ServletManager.Code.ClientError, "No general team, contact Ease.space support");
    }

    public List<TeamUser> getTeamUsers() {
        return teamUsers;
    }

    public void setTeamUsers(List<TeamUser> teamUsers) {
        this.teamUsers = teamUsers;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /* Utils */

    public void addTeamUser(TeamUser teamUser) {
        this.teamUserMap.put(teamUser.getDb_id(), teamUser);
    }

    public void addTeam(Team team) {
        this.teams.add(team);
    }

    public Team createTeam(String teamName, ServletManager sm) throws GeneralException {
        Team team = Team.createTeam(teamName, this.db_id, sm);
        this.addTeam(team);
        return team;
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
        for (Team team : this.teams)
            team.deleteFromDatabase(sm);
        for (TeamUser teamUser : this.teamUsers)
            teamUser.deleteFromDatabase(sm);
        DatabaseRequest request = db.prepareRequest("DELETE FROM companies WHERE id = ?;");
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
        res.put("companyName", this.companyName);
        JSONArray teamUsers = new JSONArray();
        for (TeamUser teamUser : this.teamUserMap.values())
            teamUsers.add(teamUser.getJson());
        res.put("teamUsers", teamUsers);
        JSONArray teams = new JSONArray();
        for (Team team : this.teams)
            teams.add(team.getJson());
        res.put("teams", teams);
        return res;
    }
}
