package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/MoveApp")
public class ServletMoveApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer position = sm.getIntParam("position", true, false);
            User user = sm.getUser();
            App app = user.getApp(app_id, sm.getHibernateQuery());
            Profile old_profile = app.getProfile();
            Profile new_profile = user.getProfile(profile_id);
            if (position < 0 || position > new_profile.getSize())
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter position");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            if (old_profile.equals(new_profile))
                old_profile.updateAppPositions(app, position, hibernateQuery);
            else {
                old_profile.removeAppAndUpdatePositions(app, hibernateQuery);
                new_profile.addAppAndUpdatePositions(app, position, hibernateQuery);
            }
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("app_id", app_id);
            ws_obj.put("profile_id", profile_id);
            ws_obj.put("index", position);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.MOVE_APP, ws_obj));
            String symmetric_key;
            if (app.getTeamCardReceiver() != null)
                symmetric_key = (String) sm.getTeamProperties(app.getTeamCardReceiver().getTeamCard().getTeam().getDb_id()).get("teamKey");
            else
                symmetric_key = (String) sm.getUserProperties(user.getDb_id()).get("keyUser");
            app.decipher(symmetric_key);
            sm.setSuccess(app.getJson());
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
