package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.ServletManager2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 31/05/2017.
 */
@WebServlet("/api/v1/teams/EditChannelName")
public class ServletEditChannelName extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager2 sm = new ServletManager2(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = Math.toIntExact((Long) sm.getParam("team_id", true));
            sm.needToBeAdminOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Integer channel_id = Math.toIntExact((Long) sm.getParam("channel_id", true));
            String name = (String) sm.getParam("name", true);
            if (name == null || name.equals(""))
                throw new HttpServletException(ServletManager2.HttpStatus.BadRequest, "Empty name.");
            Channel channel = team.getChannelWithId(channel_id);
            for (Channel channel1 : team.getChannels()) {
                if (channel1 == channel)
                    continue;
                if (channel1.getName().equals(name))
                    throw new HttpServletException(ServletManager2.HttpStatus.BadRequest, "Channel name already taken.");
            }
            channel.editName(name);
            sm.setSuccess("Channel name edited");
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
