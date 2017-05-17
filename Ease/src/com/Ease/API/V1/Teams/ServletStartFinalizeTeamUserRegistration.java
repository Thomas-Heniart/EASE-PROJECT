package com.Ease.API.V1.Teams;

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
@WebServlet("/finalizeRegistration")
public class ServletStartFinalizeTeamUserRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String username = sm.getServletParam("username", true);
            String team_id = sm.getServletParam("team_id", true);
            String teamUser_id = sm.getServletParam("teamUser_id", true);
            String code = sm.getServletParam("code", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null.");
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "lastName is needed.");
            if (teamUser_id == null || teamUser_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "teamUser_id is needed.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = team.getTeamUserWithId(Integer.parseInt(teamUser_id));
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT id FROM pendingTeamInvitations WHERE teamUser_id = ? AND team_id = ? AND code = ?");
            query.setParameter(1, teamUser_id);
            query.setParameter(2, code);
            query.setParameter(3, team_id);
            Object id = query.getSingleResult();
            if (id == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "You cannot create an account for this team");
            teamUser.setFirstName(firstName);
            teamUser.setLastName(lastName);
            teamUser.setUsername(username);
            query.saveOrUpdateObject(teamUser);
            query.querySQLString("DELETE FROM pendingTeamInvitations WHERE id = ?");
            query.setParameter(1, (String)id);
            query.executeUpdate();
            query.commit();
            teamUser.setDashboard_user(sm.getUser(), sm.getDB());
            team.askVerificationForTeamUser(teamUser);
            sm.setResponse(ServletManager.Code.Success, "Wait for admin validation");
        } catch(Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Redirect to the write page */
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
