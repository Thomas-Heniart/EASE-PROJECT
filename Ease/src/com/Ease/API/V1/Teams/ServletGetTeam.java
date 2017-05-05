package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.json.simple.JSONObject;

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
@WebServlet("/api/v1/teams/GetTeam")
public class ServletGetTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String team_id = sm.getServletParam("team_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "team_id is empty.");
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            TeamUser teamUser = sm.getTeamUserForTeam(teamManager.getTeamWithId(Integer.parseInt(team_id)));
            JSONObject res = teamUser.getTeam().getJson();
            res.put("myTeamUserId", teamUser.getDb_id());
            sm.setResponse(ServletManager.Code.Success, res.toString());
            sm.setLogResponse("GetTeam done");
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
