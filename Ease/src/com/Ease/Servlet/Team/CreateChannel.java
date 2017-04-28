package com.Ease.Servlet.Team;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
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
 * Created by thomas on 12/04/2017.
 */
@WebServlet("/CreateChannel")
public class CreateChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeTeamUser();
            TeamUser teamUser = sm.getTeamUser();
            Team team = teamUser.getTeam();
            /* if (!teamUser.hasPermission(TeamUserPermissions.Perm.ALL))
                throw new GeneralException(ServletManager.Code.ClientWarning, "You don't have this permission");
            String channelName = sm.getServletParam("channelName", true);
            Channel newChannel = team.createChannel(channelName, sm);
            newChannel.addTeamUser(teamUser, sm);
            sm.setResponse(ServletManager.Code.Success, newChannel.getJson().toString());
            sm.setLogResponse("Channel successfully created");*/
        } catch (GeneralException e) {
            sm.setResponse(e);
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
