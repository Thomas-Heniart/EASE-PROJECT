package com.Ease.Team;

import com.Ease.Utils.*;

import java.util.LinkedList;
import java.util.List;

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
        Team generalTeam = Team.createTeam("general", db_id, sm);

        int single_id = sm.getNextSingle_id();

        /* Create the first team user(admin) */

        List<TeamUser> users = new LinkedList<TeamUser>();
        users.add(admin);

        /* Create the first team */
        List<Team> teams = new LinkedList<Team>();
        teams.add(generalTeam);

        return new Company(db_id, single_id, companyName, users, teams);
    }

    protected String db_id;
    protected int single_id;
    protected String companyName;
    protected List<TeamUser> teamUsers;
    protected List<Team> teams;

    public Company(String db_id, int single_id, String companyName, List<TeamUser> teamUsers, List<Team> teams) {
        this.db_id = db_id;
        this.single_id = single_id;
        this.companyName = companyName;
        this.teamUsers = teamUsers;
        this.teams = teams;
    }
}
