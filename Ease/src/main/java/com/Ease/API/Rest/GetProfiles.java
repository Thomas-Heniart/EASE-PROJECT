package com.Ease.API.Rest;

import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

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
            JSONObject apps = new JSONObject();
            for (Profile profile : user.getProfileSet()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", profile.getDb_id());
                jsonObject.put("name", profile.getProfileInformation().getName());
                JSONArray app_ids = new JSONArray();
                for (App app : profile.getAppSet()) {
                    if (app.isLogWithApp())
                        continue;
                    if (app.getAccount() != null) {
                        if (app.isEmpty())
                            continue;
                        String symmetric_key = null;
                        String team_key = null;
                        if (app.getTeamCardReceiver() != null) {
                            TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
                            if (teamCardReceiver.getTeamCard().isTeamSingleCard() && !teamCardReceiver.getTeamUser().isTeamAdmin() && !((TeamSingleCardReceiver) teamCardReceiver).isAllowed_to_see_password())
                                continue;
                            Team team = app.getTeamCardReceiver().getTeamCard().getTeam();
                            if (!sm.getTeamUser(team).isDisabled())
                                team_key = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
                        } else
                            symmetric_key = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
                        app.decipher(symmetric_key, team_key);
                        apps.put(String.valueOf(app.getDb_id()), app.getRestJson());
                        app_ids.put(app.getDb_id());
                    }
                }
                jsonObject.put("app_ids", app_ids);
                profiles.put(String.valueOf(profile.getDb_id()), jsonObject);
            }
            JSONObject res = new JSONObject();
            res.put("profiles", profiles);
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
