package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by thomas on 12/04/2017.
 */
@WebServlet("/api/v1/CreateTeam")
public class ServletCreateTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String code = sm.getServletParam("code", false);
            String teamName = sm.getServletParam("teamName", true);
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String email = sm.getServletParam("email", true);
            String username = sm.getServletParam("username", true);
            if (teamName == null || teamName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "teamName is needed.");
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "lastName is needed.");
            if (email == null || email.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "email is needed.");
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (code == null || code.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "code not provided.");
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT id FROM createTeamInvitations WHERE email = ? AND code = ?");
            query.setParameter(1, email);
            query.setParameter(2, code);
            Object id = query.getSingleResult();
            if (id == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "You cannot create a team.");
            Team team = new Team(teamName);
            TeamUser admin = TeamUser.createAdminUser(firstName, lastName, email, username, team);
            team.addTeamUser(admin);
            Channel channel = new Channel(team, "General");
            team.addChannel(channel);
            channel.addTeamUser(admin);
            query.saveOrUpdateObject(team);
            query.querySQLString("DELETE FROM createTeamInvitations WHERE email = ? AND code = ?");
            query.setParameter(1, email);
            query.setParameter(2, code);
            query.executeUpdate();
            query.commit();
            /* TODO */
            /* team_id param and then get teamUser */
            //sm.getUser().setTeamUser(admin, sm);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            teamManager.addTeam(team);
            sm.setResponse(ServletManager.Code.Success, team.getJson().toString());
            sm.setLogResponse("Team created");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
