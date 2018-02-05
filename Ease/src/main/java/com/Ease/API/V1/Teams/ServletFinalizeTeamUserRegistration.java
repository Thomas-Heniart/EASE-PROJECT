package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
            String username = sm.getStringParam("username", true, true);
            String code = sm.getStringParam("code", false, true);
            if (username == null || username.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "username is needed.");
            if (code == null || code.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "code is needed.");
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id, team_id FROM teamUsers WHERE invitation_code = ?");
            query.setParameter(1, code);
            Object idTeamAndTeamUserObj = query.getSingleResult();
            if (idTeamAndTeamUserObj == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot be part of this team");
            Object[] idTeamAndTeamUser = (Object[]) idTeamAndTeamUserObj;
            Integer team_id = (Integer) idTeamAndTeamUser[1];
            if (user.getTeamUserOrNull(team_id) != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot have two accounts in a team.");
            Integer teamUser_id = (Integer) idTeamAndTeamUser[0];
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            Date now = new Date();
            if (teamUser.getArrival_date() != null && teamUser.getArrival_date().getTime() > now.getTime())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot register.");
            teamUser.setUsername(username);
            teamUser.setUser(user);
            teamUser.setInvitation_code(null);
            teamUser.setState(1);
            teamUser.setArrival_date(now);
            sm.saveOrUpdate(teamUser);
            if (teamUser.getAdmin_id() == null || teamUser.getAdmin_id() == 0)
                throw new HttpServletException(HttpStatus.BadRequest, "The user must be invited by an admin");
            String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
            if (teamKey != null) {
                String keyUser = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
                TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
                teamUser.lastRegistrationStep(keyUser, teamKey, sm.getUserWebSocketManager(teamUser_admin.getUser().getDb_id()), sm.getHibernateQuery());
            }
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            teamUser.getPendingNotificationSet().forEach(pendingNotification -> pendingNotification.sendToUser(user, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery));
            teamUser.getPendingNotificationSet().clear();
            sm.getUser().addTeamUser(teamUser);
            sm.setTeam(team);
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.updateUserContactLists(user);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
            sm.setSuccess(teamUser.getJson());
        } catch (Exception e) {
            e.printStackTrace();
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
