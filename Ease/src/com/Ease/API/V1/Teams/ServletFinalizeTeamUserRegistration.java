package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
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
public class ServletFinalizeTeamUserRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String username = sm.getServletParam("username", true);
            String jobTitle = sm.getServletParam("jobTitle", true);
            String code = sm.getServletParam("code", true);
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "lastName is needed.");
            if (jobTitle == null || jobTitle.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "jobTitle is needed.");

            HibernateQuery query = new HibernateQuery();

            query.querySQLString("SELECT id, team_id, teamUser_id FROM pendingTeamInvitations WHERE code = ?");
            query.setParameter(1, code);
            Object idTeamAndTeamUserObj = query.getSingleResult();
            if (idTeamAndTeamUserObj == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "You cannot be part of this team");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Object[] idTeamAndTeamUser = (Object[]) idTeamAndTeamUserObj;
            Integer id = (Integer) idTeamAndTeamUser[0];
            Integer team_id = (Integer) idTeamAndTeamUser[1];
            Integer teamUser_id = (Integer) idTeamAndTeamUser[2];
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            teamUser.setFirstName(firstName);
            teamUser.setLastName(lastName);
            teamUser.setUsername(username);
            teamUser.setJobTitle(jobTitle);
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
            query.commit();
            teamUser.setDashboard_user(sm.getUser(), sm.getDB());
            team.askVerificationForTeamUser(teamUser, verificationCode);
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
