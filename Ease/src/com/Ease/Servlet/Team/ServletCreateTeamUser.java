package com.Ease.Servlet.Team;

import com.Ease.Dashboard.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Team.TeamUserPermissions;
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
@WebServlet("/ServletCreateTeamUser")
public class ServletCreateTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            HibernateQuery query = new HibernateQuery();
            String firstName = sm.getServletParam("firstName", true);
            String lastName = sm.getServletParam("lastName", true);
            String email = sm.getServletParam("email", true);
            String username = sm.getServletParam("username", true);
            String code = sm.getServletParam("code", true);
            //String team_id = sm.getServletParam("team_id", true);
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "channel_id is needed.");
            if (lastName == null || lastName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "lastName is needed.");
            if (username == null || username.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "username is needed.");
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new GeneralException(ServletManager.Code.ClientWarning, "email is needed.");
            if (code == null || code.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "No code provided");
            query.querySQLString("SELECT id, team_id FROM pendingTeamInvitations WHERE email = ? AND code = ?");
            query.setParameter(1, email);
            query.setParameter(2, code);
            Object queryRs = query.getSingleResult();
            if (queryRs == null)
                throw new GeneralException(ServletManager.Code.ClientWarning, "You can't create an account");
            Object[] rs = (Object[]) queryRs;
            Integer invitation_id = (Integer)rs[0];
            Integer team_id = (Integer) rs[1];
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = new TeamUser(firstName, lastName, email, username, team, new TeamUserPermissions(TeamUserPermissions.Role.MEMBER.getValue()));
            team.addTeamUser(teamUser);
            team.getGeneralChannel().addTeamUser(teamUser);
            for (TeamUser teamUser_tenant : team.getTeamUsers()) {
                if (teamUser_tenant == teamUser)
                    continue;
                query.saveOrUpdateObject(team.createTeamUserChannel(teamUser, teamUser_tenant));
            }
            query.saveOrUpdateObject(teamUser);
            query.saveOrUpdateObject(team);
            query.querySQLString("DELETE FROM pendingTeamInvitations WHERE id = ?");
            query.setParameter(1, invitation_id);
            query.executeUpdate();
            query.commit();
            if (user != null)
                user.setTeamUser(teamUser, sm);
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
