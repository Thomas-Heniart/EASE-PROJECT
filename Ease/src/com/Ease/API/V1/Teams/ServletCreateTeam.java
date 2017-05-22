package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
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
@WebServlet("/api/v1/teams/CreateTeam")
public class ServletCreateTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String digits = sm.getServletParam("digits", false);
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
            if (digits == null || digits.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "code not provided.");
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT id FROM createTeamInvitations WHERE email = ? AND code = ?");
            query.setParameter(1, email);
            query.setParameter(2, digits);
            Object id = query.getSingleResult();
            if (id == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "You cannot create a team.");
            Map.Entry<String, String> publicAndPrivateKey = RSA.generateKeys();
            Team team = new Team(teamName, publicAndPrivateKey.getKey());
            String deciphered_privateKey = publicAndPrivateKey.getValue();
            String privateKey = user.encrypt(deciphered_privateKey);
            TeamUser admin = TeamUser.createAdminUser(firstName, lastName, email, username, privateKey, team);
            admin.setDeciphered_teamPrivateKey(privateKey);
            team.setDeciphered_privateKey(deciphered_privateKey);
            Channel channel = new Channel(team, "General", "This is the general channel");
            team.addChannel(channel);
            channel.addTeamUser(admin);
            query.saveOrUpdateObject(team);
            query.querySQLString("DELETE FROM createTeamInvitations WHERE id = ?");
            query.setParameter(1, id);
            query.executeUpdate();
            query.commit();
            team.addTeamUser(admin);
            admin.setDashboard_user(user, sm.getDB());
            user.addTeamUser(admin);
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
