package com.Ease.API.Rest;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/rest/GetRoomApps")
public class GetRoomApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUserWithToken();
            Integer team_id = sm.getIntParam("team_id", true);
            if (team_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter team_id");
            Integer room_id = sm.getIntParam("room_id", true);
            if (room_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Missing parameter room_id");
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            Channel channel = team.getChannelWithId(room_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            JSONObject res = new JSONObject();
            JSONArray apps = new JSONArray();
            /* for (SharedApp sharedApp : team.getAppManager().getSharedAppsForTeamUser(teamUser)) {
                if (sharedApp.getHolder().getChannel() != channel)
                    continue;
                App holder = (App) sharedApp.getHolder();
                if (!sharedApp.canSeeInformation() && !holder.isEmpty() && !(holder.isLinkApp() && sharedApp.isPinned()))
                    continue;
                apps.add(((App) sharedApp).getRestJson());
            } */
            res.put("apps", apps);
            sm.setSuccess(res);
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
