package com.Ease.API.V1.Teams;

import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
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
            JSONObject teamUsers_and_channels = sm.getJsonParam("team_users_and_channels", true, false);
            JSONObject res = new JSONObject();
            Integer my_id = sm.getTeamUser(team).getDb_id();
            JSONArray profiles = new JSONArray();
            for (Object o : teamUsers_and_channels.keySet()) {
                String key = String.valueOf(o);
                Integer teamUser_id = Integer.valueOf(key);
                JSONArray channels = teamUsers_and_channels.getJSONArray(key);
                TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
                for (int i = 0; i < channels.length(); i++) {
                    Integer channel_id = channels.getInt(i);
                    Channel channel = team.getChannelWithId(channel_id);
                    Profile tmp = teamUser.createTeamProfile(channel, sm.getHibernateQuery());
                    if (my_id.equals(teamUser_id))
                        profiles.put(tmp.getJson());
                }
            }
            res.put("profiles", profiles);
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
