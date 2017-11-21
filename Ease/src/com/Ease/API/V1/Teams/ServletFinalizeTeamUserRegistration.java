package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.User.Notification;
import com.Ease.User.NotificationFactory;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketManager;
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
            User user = sm.getUser();
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
            query.querySQLString("SELECT id, team_id FROM teamUsers WHERE invitation_code = ?");
            query.setParameter(1, code);
            Object idTeamAndTeamUserObj = query.getSingleResult();
            if (idTeamAndTeamUserObj == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot be part of this team");
            Object[] idTeamAndTeamUser = (Object[]) idTeamAndTeamUserObj;
            Integer team_id = (Integer) idTeamAndTeamUser[1];
            if (sm.getTeamUser(team_id) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot have two accounts in a team.");
            Integer teamUser_id = (Integer) idTeamAndTeamUser[0];
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            teamUser.setFirstName(firstName);
            teamUser.setLastName(lastName);
            teamUser.setUsername(username);
            teamUser.setJobTitle(jobRoles[job_index]);
            teamUser.setUser(user);
            teamUser.setInvitation_code(null);
            teamUser.setState(1);
            sm.saveOrUpdate(teamUser);
            if (teamUser.getAdmin_id() == null || teamUser.getAdmin_id() == 0)
                throw new HttpServletException(HttpStatus.BadRequest, "The user must be invited by an admin");
            /* @TODO remove notification and email */
            TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
            Notification notification = NotificationFactory.getInstance().createNotification(teamUser_admin.getUser(), teamUser.getUsername() + " is ready to join your team. Give your final approval to give the access.", "/resources/notifications/flag.png", teamUser);
            sm.saveOrUpdate(notification);
            WebSocketManager webSocketManager = sm.getUserWebSocketManager(teamUser_admin.getUser().getDb_id());
            webSocketManager.sendObject(WebSocketMessageFactory.createNotificationMessage(notification));
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(180141);
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(teamUser_admin.getEmail());
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("user_pseudo", teamUser.getUsername());
            mailJetBuilder.addVariable("user_email", teamUser.getEmail());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teams/" + team.getDb_id() + "/@" + teamUser.getDb_id());
            mailJetBuilder.sendEmail();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Profile profile = teamUser.getOrCreateProfile(hibernateQuery);
            teamUser.getTeamCardReceivers().stream().map(TeamCardReceiver::getApp).forEach(app -> {
                app.setProfile(profile);
                app.setPosition(profile.getSize());
                hibernateQuery.saveOrUpdateObject(app);
                profile.addApp(app);
            });
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
            Team team = teamManager.getTeam((Integer) teamAndTeamUserId[0], sm.getHibernateQuery());
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
