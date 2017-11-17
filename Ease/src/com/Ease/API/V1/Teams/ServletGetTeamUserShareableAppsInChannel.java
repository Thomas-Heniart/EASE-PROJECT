package com.Ease.API.V1.Teams;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/GetUserShareableAppsInChannel")
public class ServletGetTeamUserShareableAppsInChannel extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            throw new HttpServletException(HttpStatus.Forbidden, "This servlet shouldn't be used anymore.");
            /* Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeTeamUserOfTeam(team);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            Integer channel_id = sm.getIntParam("channel_id", true);
            Channel channel = team.getChannelWithId(channel_id);
            Integer teamUser_id = sm.getIntParam("team_user_id", true);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            JSONArray shareableApps = new JSONArray();
            for (ShareableApp shareableApp : team.getAppManager().getShareableAppsForTeamUser(teamUser)) {
                if (shareableApp.getChannel() == channel)
                    shareableApps.add(shareableApp.getShareableJson());
            }
            sm.setSuccess(shareableApps); */
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
