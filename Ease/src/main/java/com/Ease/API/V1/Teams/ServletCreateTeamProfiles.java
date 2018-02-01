package com.Ease.API.V1.Teams;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/CreateTeamProfiles")
public class ServletCreateTeamProfiles extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeTeamUserOfTeam(team);
            JSONArray teamUser_ids = sm.getArrayParam("team_user_ids", true, false);
            JSONObject res = new JSONObject();
            Integer my_id = sm.getTeamUser(team).getDb_id();
            Profile profile = null;
            for (int i = 0; i < teamUser_ids.length(); i++) {
                Integer teamUser_id = teamUser_ids.getInt(i);
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                Profile tmp = teamUser.createTeamProfile(sm.getHibernateQuery());
                if (my_id.equals(teamUser_id))
                    profile = tmp;
            }
            res.put("profile", profile == null ? JSONObject.NULL : profile.getJson());
            sm.setSuccess(res);
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
