package com.Ease.Servlet.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserPermissions;
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
@WebServlet("/ServletCreateTeamUser")
public class ServletCreateTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            HibernateQuery query = new HibernateQuery();
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String email = sm.getServletParam("email", true);
            String username = sm.getServletParam("username", true);
            String team_id = sm.getServletParam("team_id", true);
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "channel_id is needed.");
            if (lastName == null || lastName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "lastName is needed.");
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (email == null || email.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "email is needed.");
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "team_id is needed.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            TeamUser teamUser = new TeamUser(firstName, lastName, email, username, team, new TeamUserPermissions(TeamUserPermissions.Role.MEMBER.getValue()));
            team.addTeamUser(teamUser);
            team.getGeneralChannel().addTeamUser(teamUser);
            query.saveOrUpdateObject(teamUser);
            query.saveOrUpdateObject(team);
            query.commit();
            sm.setResponse(ServletManager.Code.Success, teamUser.getJson().toString());
            sm.setLogResponse("User created");
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
