package com.Ease.API.Rest;

import com.Ease.User.User;
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

@WebServlet("/api/rest/GetGroupApps")
public class GetGroupApps extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUserWithToken();
            JSONObject res = new JSONObject();
            JSONArray apps = new JSONArray();
            Integer profile_id = sm.getIntParam("group_id", true);
            if (profile_id == null)
                throw new HttpServletException(HttpStatus.BadRequest, "Group id cannot be null");
            user.getProfile(profile_id).getApps().forEach(app -> {
                if (app.isLinkApp() || app.isClassicApp())
                    apps.add(app.getRestJson());
            });
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
