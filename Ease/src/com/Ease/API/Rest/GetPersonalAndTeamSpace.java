package com.Ease.API.Rest;

import com.Ease.User.User;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.TeamUser;
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

@WebServlet("/api/rest/GetPersonalAndTeamSpace")
public class GetPersonalAndTeamSpace extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUserWithToken();
            JSONObject res = new JSONObject();
            JSONArray personnalSpace = new JSONArray();
            for (Profile profile : user.getProfileSet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", profile.getProfileInformation().getName());
                jsonObject.put("id", profile.getDb_id());
                personnalSpace.add(jsonObject);
            }
            res.put("personal_space", personnalSpace);
            JSONArray teams = new JSONArray();
            for (TeamUser teamUser : user.getTeamUsers()) {
                if (teamUser.isDisabled() || (teamUser.getDepartureDate() != null && teamUser.getDepartureDate().getTime() <= sm.getTimestamp().getTime()))
                    continue;
                JSONObject team = new JSONObject();
                team.put("name", teamUser.getTeam().getName());
                team.put("id", teamUser.getTeam().getDb_id());
                JSONArray rooms = new JSONArray();
                for (Channel channel : teamUser.getChannels()) {
                    JSONObject room = new JSONObject();
                    room.put("name", channel.getName());
                    room.put("id", channel.getDb_id());
                    rooms.add(room);
                }
                team.put("rooms", rooms);
                teams.add(team);
            }
            res.put("teams", teams);
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
