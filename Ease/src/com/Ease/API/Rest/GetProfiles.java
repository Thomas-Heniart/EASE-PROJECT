package com.Ease.API.Rest;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.User.User;
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

@WebServlet("/api/rest/GetProfiles")
public class GetProfiles extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            JSONObject profiles = new JSONObject();
            for (Profile profile : user.getProfileSet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", profile.getProfileInformation().getName());
                JSONArray apps = new JSONArray();
                for (App app : profile.getAppSet()) {
                    if (!app.isEmpty() && !app.isLogWithApp()) {
                        if (app.isClassicApp()) {
                            String symmetric_key = null;
                            String team_key = null;
                            if (app.getTeamCardReceiver() != null) {
                                Team team = app.getTeamCardReceiver().getTeamCard().getTeam();
                                if (!sm.getTeamUser(team).isDisabled())
                                    team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                            } else
                                symmetric_key = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
                            app.decipher(symmetric_key, team_key);
                        }
                    }
                    apps.add(app.getRestJson());
                }
                jsonObject.put("apps", apps);
                profiles.put(profile.getDb_id(), jsonObject);
            }
            JSONObject res = new JSONObject();
            res.put("profiles", profiles);
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
