package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.GetServletManager;

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
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Integer room_id = sm.getIntParam("channel_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Channel channel = team.getChannelWithId(room_id);
            sm.setSuccess(channel.getJson());
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
