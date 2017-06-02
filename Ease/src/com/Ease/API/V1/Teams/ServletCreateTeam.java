package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.AES;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 12/04/2017.
 */
@WebServlet("/api/v1/teams/CreateTeam")
public class ServletCreateTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String digits = sm.getStringParam("digits", false);
            String teamName = sm.getStringParam("teamName", true);
            String firstName = sm.getStringParam("firstName", true);
            String lastName = sm.getStringParam("lastName", true);
            String email = sm.getStringParam("email", true);
            String username = sm.getStringParam("username", true);
            if (teamName == null || teamName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "teamName is needed.");
            if (firstName == null || firstName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "firstName is needed.");
            if (lastName == null || lastName.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "lastName is needed.");
            if (email == null || email.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "email is needed.");
            if (username == null || username.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "username is needed.");
            if (digits == null || digits.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "code not provided.");
            HibernateQuery query = sm.getHibernateQuery();
            query.querySQLString("SELECT id FROM createTeamInvitations WHERE email = ? AND code = ?");
            query.setParameter(1, email);
            query.setParameter(2, digits);
            Object id = query.getSingleResult();
            if (id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot create a team.");
            String teamKey = AES.keyGenerator();
            Team team = new Team(teamName);
            String teamKey_ciphered = user.encrypt(teamKey);
            TeamUser admin = TeamUser.createAdminUser(firstName, lastName, email, username, teamKey_ciphered, team);
            admin.setDeciphered_teamKey(teamKey);
            Channel channel = new Channel(team, "General", "This is the general channel");
            team.addChannel(channel);
            channel.addTeamUser(admin);
            sm.saveOrUpdate(team);
            sm.saveOrUpdate(admin);
            sm.saveOrUpdate(channel);
            query.querySQLString("DELETE FROM createTeamInvitations WHERE id = ?");
            query.setParameter(1, id);
            query.executeUpdate();
            team.addTeamUser(admin);
            admin.setDashboard_user(user, sm.getDB());
            user.addTeamUser(admin);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            teamManager.addTeam(team);
            sm.setSuccess(team.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
