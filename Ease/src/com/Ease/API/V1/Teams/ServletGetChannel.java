package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
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
 * Created by thomas on 08/05/2017.
 */
@WebServlet("/api/v1/teams/GetChannel")
public class ServletGetChannel extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String team_id = sm.getServletParam("team_id", true);
            String channel_id = sm.getServletParam("channel_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            if (channel_id == null || channel_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Channel is null");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(Integer.parseInt(team_id));
            Channel channel = team.getChannelWithId(Integer.parseInt(channel_id));
            sm.setResponse(ServletManager.Code.Success, channel.getJson().toString());
            sm.setLogResponse("GetChannel done");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
