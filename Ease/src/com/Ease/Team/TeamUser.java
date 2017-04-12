package com.Ease.Team;

import com.Ease.Utils.*;
import org.json.simple.JSONObject;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by thomas on 10/04/2017.
 */
public class TeamUser {

    public static TeamUser createTeamUser(String email, Company company, String firstName, String lastName, TeamUserPermissions permissions, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("SELECT id FROM teamUsers WHERE email = ? AND company_id = ?;");
        request.setString(email);
        request.setInt(company.getDb_id());
        DatabaseResult rs = request.get();
        if (rs.next())
            throw new GeneralException(ServletManager.Code.ClientWarning, "This team user already exists");
        request = db.prepareRequest("SELECT id FROM users WHERE email = ?;");
        request.setString(email);
        rs = request.get();
        String user_id = null;
        if (rs.next())
            user_id = rs.getString(1);
        request = db.prepareRequest("INSERT INTO teamUsers values(?, ?, ?, ?, ?, ?, ?);");
        request.setNull();
        if (user_id == null)
            request.setNull();
        else
            request.setInt(user_id);
        request.setInt(company.getDb_id());
        request.setString(firstName);
        request.setString(lastName);
        request.setString(email);
        request.setInt(permissions.getDb_id());
        String db_id = request.set().toString();
        int single_id = sm.getNextSingle_id();
        return new TeamUser(company, user_id, db_id, single_id, email, firstName, lastName, permissions);
    }

    public static TeamUser createAdminTeamUser(String adminEmail, Company company, String adminFirstName, String adminLastName, ServletManager sm) throws GeneralException {
        TeamUserPermissions adminPermissions = TeamUserPermissions.createAdminPermissions(sm);
        return createTeamUser(adminEmail, company, adminFirstName, adminLastName, adminPermissions, sm);
    }

    public static TeamUser loadTeamUser(String db_id, Company company, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT * FROM teamUsers WHERE id = ?");
        request.setInt(db_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "This teamUser does not exist");
        String user_id = rs.getString("user_id");
        String permissions_id = rs.getString("permissions_id");
        String firstName = rs.getString("firstName");
        String lastName = rs.getString("lastName");
        String email = rs.getString("email");
        int single_id = sm.getNextSingle_id();
        TeamUserPermissions permissions = TeamUserPermissions.loadTeamUserPermissions(rs.getString("permissions_id"), sm);
        return new TeamUser(company, user_id, db_id, single_id, email, firstName, lastName, permissions);
    }

    public static TeamUser loadTeamUser(String user_id, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT * FROM teamUsers WHERE user_id = ?");
        request.setInt(user_id);
        DatabaseResult rs = request.get();
        if (!rs.next())
            return null;
        String company_id = rs.getString(3);
        Map<String, Company> companyMap = (Map<String, Company>) sm.getContextAttr("companyMap");
        Company company = companyMap.get(company_id);
        if (company == null)
            company = Company.loadCompany(company_id, sm);
        return company.getTeamUserWithDbId(rs.getString(1));
    }

    public static Map<String, TeamUser> loadTeamUsers(Company company, ServletManager sm) throws GeneralException {
        DatabaseRequest request = sm.getDB().prepareRequest("SELECT id FROM teamUsers WHERE company_id = ?;");
        request.setInt(company.getDb_id());
        DatabaseResult rs = request.get();
        if (!rs.next())
            throw new GeneralException(ServletManager.Code.ClientError, "No teamUsers for this company");
        Map<String, TeamUser> teamUsersMap = new HashMap<String, TeamUser>();
        do {
            TeamUser tmp = loadTeamUser(rs.getString(1), company, sm);
            teamUsersMap.put(tmp.getDb_id(), tmp);
        } while (rs.next());
        return teamUsersMap;
    }

    protected Company company;
    protected String user_id;
    protected String db_id;
    protected int single_id;
    protected String firstName;
    protected String lastName;
    protected String email;

    protected TeamUserPermissions permissions;

    public TeamUser(Company company, String user_id, String db_id, int single_id, String email, String firstName, String lastName, TeamUserPermissions permissions) {
        this.company = company;
        this.user_id = user_id;
        this.db_id = db_id;
        this.single_id = single_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.permissions = permissions;
    }

    public String getDb_id() {
        return db_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET user_id = ? WHERE id = ?");
        request.setInt(user_id);
        request.setInt(db_id);
        request.set();
        this.user_id = user_id;
    }

    public int getSingle_id() {
        return single_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET firstName = ? WHERE id = ?;");
        request.setString(firstName);
        request.setInt(db_id);
        request.set();
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET lastName = ? WHERE id = ?;");
        request.setString(lastName);
        request.setInt(db_id);
        request.set();
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET email = ? WHERE id = ?;");
        request.setString(email);
        request.setInt(db_id);
        request.set();
        this.email = email;
    }

    public TeamUserPermissions getPermissions() {
        return permissions;
    }

    public void setPermissions(TeamUserPermissions permissions, ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        DatabaseRequest request = db.prepareRequest("UPDATE teamUsers SET permissions_id = ? WHERE id = ?;");
        request.setString(permissions.getDb_id());
        request.setInt(db_id);
        request.set();
        this.permissions = permissions;
    }

    public Company getCompany() {
        return company;
    }

    public void deleteFromDatabase(ServletManager sm) throws GeneralException {
        DataBaseConnection db = sm.getDB();
        int transaction = db.startTransaction();
        DatabaseRequest request = db.prepareRequest("DELETE FROM teamsAndTeamUsersMap WHERE team_user_id = ?;");
        request.setInt(this.db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM companiesAndTeamUsersMap WHERE team_user_id = ?;");
        request.setInt(this.db_id);
        request.set();
        request = db.prepareRequest("DELETE FROM teamUsers WHERE id = ?");
        request.setInt(this.db_id);
        request.set();
        db.commitTransaction(transaction);
    }

    public boolean hasPermission(TeamUserPermissions.Perm perm) {
        return this.permissions.havePermission(perm.getValue());
    }

    public JSONObject getJson() {
        JSONObject res = new JSONObject();
        res.put("firstName", this.firstName);
        res.put("lastName", this.lastName);
        res.put("email", this.email);
        res.put("single_id", this.single_id);
        return res;
    }
}
