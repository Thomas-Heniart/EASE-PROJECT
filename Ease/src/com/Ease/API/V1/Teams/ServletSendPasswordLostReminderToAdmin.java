package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/teams/SendPasswordLostReminderToAdmin")
public class ServletSendPasswordLostReminderToAdmin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamUser teamUser = (TeamUser) hibernateQuery.get(TeamUser.class, teamUser_id);
            if (teamUser == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such teamUser");
            Team team = teamUser.getTeam();
            if (!teamUser.isDisabled())
                throw new HttpServletException(HttpStatus.BadRequest, "You must be disable to send a notification");
            if (teamUser.getAdmin_id() == null)
                throw new HttpServletException(HttpStatus.Forbidden, "Not allowed");
            TeamUser teamUser_admin = team.getTeamUserWithId(teamUser.getAdmin_id());
            NotificationFactory.getInstance().createRemindAdminPasswordLost(teamUser, teamUser_admin, sm.getUserWebSocketManager(teamUser_admin.getUser().getDb_id()), hibernateQuery);
            sm.setSuccess("Notification sent");
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
