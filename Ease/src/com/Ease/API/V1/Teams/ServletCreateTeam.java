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
import java.util.Date;

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
            String teamName = sm.getStringParam("team_name", true);
            String firstName = sm.getStringParam("first_name", true);
            String lastName = sm.getStringParam("last_name", true);
            String email = sm.getStringParam("email", true);
            String username = sm.getStringParam("username", true);
            String jobTitle = sm.getStringParam("job_title", true);
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
            if (jobTitle == null || jobTitle.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "job title is needed.");
            if (!user.getVerifiedEmails().contains(email) && (digits == null || digits.equals("") || digits.length() != 6))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot create a team.");
            if (user.getUnverifiedEmails().contains(email)) {
                HibernateQuery query = sm.getHibernateQuery();
                query.querySQLString("SELECT id FROM pendingTeamCreations WHERE email = ? AND digits = ?");
                query.setParameter(1, email);
                query.setParameter(2, digits);
                Object id = query.getSingleResult();
                if (id == null)
                    throw new HttpServletException(HttpStatus.Forbidden, "You cannot create a team.");
                query.querySQLString("DELETE FROM pendingTeamCreations WHERE id = ?");
                query.setParameter(1, id);
                query.executeUpdate();
            }
            String teamKey = AES.keyGenerator();
            Team team = new Team(teamName);
            String teamKey_ciphered = user.encrypt(teamKey);
            Date arrivalDate = new Date(sm.getLongParam("arrival_date", true));
            TeamUser admin = TeamUser.createAdminUser(firstName, lastName, email, username, arrivalDate, teamKey_ciphered, team);
            admin.setJobTitle(jobTitle);
            admin.setDeciphered_teamKey(teamKey);
            admin.setUser_id(user.getDBid());
            sm.saveOrUpdate(team);
            Channel channel = new Channel(team, "General", "This is the general channel");
            sm.saveOrUpdate(channel);
            sm.saveOrUpdate(admin);
            admin.setDashboard_user(user);
            team.addChannel(channel);
            team.addTeamUser(admin);
            sm.saveOrUpdate(team);
            sm.getHibernateQuery().commit();
            user.addTeamUser(admin);
            channel.addTeamUser(admin, sm.getDB());
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
