package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/EditTeamUserFirstAndLastName")
public class ServletEditTeamUserFirstAndLastName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamUser teamUser = (TeamUser) hibernateQuery.get(TeamUser.class, teamUser_id);
            if (teamUser == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This user does not exist");
            Team team = teamUser.getTeam();
            sm.initializeTeamWithContext(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.equals(teamUser) && !teamUser_connected.isTeamAdmin())
                throw new HttpServletException(HttpStatus.Forbidden);
            String first_name = sm.getStringParam("first_name", true, false);
            if (first_name.equals("") || first_name.length() > 100)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter first_name");
            String last_name = sm.getStringParam("last_name", true, false);
            if (last_name.equals("") || last_name.length() > 100)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter last_name");
            teamUser.setFirstName(first_name);
            teamUser.setLastName(last_name);
            sm.saveOrUpdate(teamUser);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
            sm.setSuccess(teamUser.getJson());
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
