package com.Ease.API.V1.Dashboard;

import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
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

@WebServlet("/api/v1/dashboard/MoveProfile")
public class ServletMoveProfile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer column_index = sm.getIntParam("column_index", true, false);
            Integer position = sm.getIntParam("position", true, false);
            user.moveProfile(profile_id, column_index, position, sm.getHibernateQuery());
            Profile profile = (Profile) sm.getHibernateQuery().get(Profile.class, profile_id);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("profile_id", profile_id);
            ws_obj.put("column_index", column_index);
            ws_obj.put("index", position);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.MOVE_PROFILE, ws_obj));
            sm.setSuccess(user.getProfile(profile_id).getJson());
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
