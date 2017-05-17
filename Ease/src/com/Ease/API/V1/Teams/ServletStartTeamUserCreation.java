package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserPermissions;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 02/05/2017.
 */
@WebServlet("/api/v1/teams/StartTeamUserCreation")
public class ServletStartTeamUserCreation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            HibernateQuery query = new HibernateQuery();
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String email = sm.getServletParam("email", true);
            String username = sm.getServletParam("username", true);
            String team_id = sm.getServletParam("team_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null.");
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new GeneralException(ServletManager.Code.ClientWarning, "email is needed.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            sm.needToBeAdminOfTeam(team);
            TeamUser adminTeamUser = sm.getTeamUserForTeam(team);
            query.querySQLString("SELECT id FROM teamUsers WHERE email = ? AND team_id = ?");
            query.setParameter(1, email);
            query.setParameter(2, team_id);
            Object queryRs = query.getSingleResult();
            if (queryRs != null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "This teamUser already exists");
            query.querySQLString("SELECT * FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE users.email = ? OR teamUsers.email = ?;");
            query.setParameter(1, email);
            query.setParameter(2, email);
            if (query.getSingleResult() != null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "Email already taken");
            TeamUser teamUser = new TeamUser(firstName, lastName, email, username, null, false, team, new TeamUserPermissions(TeamUserPermissions.Role.MEMBER.getValue()));
            team.addTeamUser(teamUser);
            team.getGeneralChannel().addTeamUser(teamUser);
            query.saveOrUpdateObject(teamUser);
            query.saveOrUpdateObject(team);
            String code = CodeGenerator.generateNewCode();
            query.querySQLString("INSERT INTO pendingTeamInvitations values(NULL, ?, ?, ?);");
            query.setParameter(1, teamUser.getDb_id());
            query.setParameter(2, code);
            query.setParameter(3, team.getDb_id());
            query.executeUpdate();
            query.commit();
            SendGridMail sendGridMail = new SendGridMail("Thomas @EaseSpace", "thomas@ease.space");
            sendGridMail.sendInvitationToJoinTeamEmail(team.getName(), adminTeamUser.getFirstName(), firstName, email, username, email, code);
            /* Send an email */
            sm.setResponse(ServletManager.Code.Success, teamUser.getJson().toString());
            sm.setLogResponse("TeamUser created");
        } catch(Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
