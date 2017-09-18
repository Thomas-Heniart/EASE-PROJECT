package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 02/05/2017.
 */
@WebServlet("/api/v1/teams/FinalizeRegistration")
public class ServletFinalizeTeamUserRegistration extends HttpServlet {

    private static final String[] jobRoles = {
            "Adminisrative/Facilities",
            "Accounting/Finance",
            "Business Development",
            "Business Owner",
            "Customer Support",
            "Data/Analytics/Business Intelligence",
            "Design",
            "Engineering (Software)",
            "Marketing",
            "Media/Communications",
            "Operations",
            "Product Management",
            "Program/Project Management",
            "Research",
            "Sales",
            "Other"
    };

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String firstName = sm.getStringParam("first_name", true, true);
            String lastName = sm.getStringParam("last_name", true, true);
            String username = sm.getStringParam("username", true, true);
            String job_details = sm.getStringParam("job_details", true, true);
            Integer job_index = sm.getIntParam("job_index", true, true);
            String code = sm.getStringParam("code", false, true);
            if (username == null || username.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "username is needed.");
            if (firstName == null || firstName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "lastName is needed.");
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "code is needed.");
            if (job_index == null || job_index < 0 || job_index >= jobRoles.length)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid job index.");
            if (job_index == (jobRoles.length - 1) && (job_details == null || job_details.equals("")))
                throw new HttpServletException(HttpStatus.BadRequest, "It would be awesome to know more about your work!");
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id, team_id, teamUser_id FROM pendingTeamInvitations WHERE code = ?");
            query.setParameter(1, code);
            Object idTeamAndTeamUserObj = query.getSingleResult();
            if (idTeamAndTeamUserObj == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot be part of this team");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Object[] idTeamAndTeamUser = (Object[]) idTeamAndTeamUserObj;
            Integer id = (Integer) idTeamAndTeamUser[0];
            Integer team_id = (Integer) idTeamAndTeamUser[1];
            if (sm.getTeamUserForTeamId(team_id) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot have two accounts in a team.");
            Integer teamUser_id = (Integer) idTeamAndTeamUser[2];
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            teamUser.setFirstName(firstName);
            teamUser.setLastName(lastName);
            teamUser.setUsername(username);
            teamUser.setJobTitle(jobRoles[job_index]);
            query.saveOrUpdateObject(teamUser);
            query.querySQLString("DELETE FROM pendingTeamInvitations WHERE id = ?");
            query.setParameter(1, id);
            query.executeUpdate();
            String verificationCode;
            do {
                verificationCode = CodeGenerator.generateNewCode();
                query.querySQLString("SELECT * FROM pendingTeamUserVerifications WHERE code = ?");
                query.setParameter(1, verificationCode);
            } while (query.getSingleResult() != null);
            query.querySQLString("INSERT INTO pendingTeamUserVerifications values(NULL, ?, ?);");
            query.setParameter(1, teamUser.getDb_id());
            query.setParameter(2, code);
            query.executeUpdate();
            teamUser.setUser_id(sm.getUser().getDBid());
            teamUser.setDashboard_user(sm.getUser());
            teamUser.setState(1);
            sm.saveOrUpdate(teamUser);
            team.askVerificationForTeamUser(teamUser, verificationCode);
            if (teamUser.getAdmin_id() == null || teamUser.getAdmin_id() == 0)
                throw new HttpServletException(HttpStatus.BadRequest, "The user must be invited by an admin");
            TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
            teamUser_admin.addNotification(teamUser.getUsername() + " is ready to join your team. Give your final approval to give the access.", "@" + teamUser.getDb_id().toString(), "/resources/notifications/flag.png", sm.getTimestamp(), sm.getDB());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(180141);
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(teamUser_admin.getEmail());
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("user_pseudo", teamUser.getUsername());
            mailJetBuilder.addVariable("user_email", teamUser.getEmail());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "teams#/teams/" + team.getDb_id() + "/@" + teamUser.getDb_id());
            mailJetBuilder.sendEmail();
            sm.getUser().addTeamUser(teamUser);
            sm.setParam("team_id", team_id.longValue());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getJson(), teamUser.getOrigin()));
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            String code = sm.getParam("code", false);
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "No code provided.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT team_id, teamUser_id FROM pendingTeamInvitations WHERE code = ?");
            hibernateQuery.setParameter(1, code);
            Object[] teamAndTeamUserId = (Object[]) hibernateQuery.getSingleResult();
            if (teamAndTeamUserId == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Your code is invalid.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId((Integer) teamAndTeamUserId[0]);
            TeamUser teamUser = team.getTeamUserWithId((Integer) teamAndTeamUserId[1]);
            JSONObject res = teamUser.getJson();
            res.put("team_id", team.getDb_id());
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}
