package com.Ease.API.V1.Common;

import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/GetMyInformation")
public class ServletGetMyInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            JSONObject res = new JSONObject();
            try {
                sm.needToBeConnected();
                User user = sm.getUser();
                for (TeamUser teamUser : user.getTeamUsers())
                    sm.initializeTeamWithContext(teamUser.getTeam());
                res.put("user", user.getJson());
                sm.setSuccess(res);
            } catch (HttpServletException e) {
                if (e.getHttpStatus() == HttpStatus.AccessDenied) {
                    res.put("user", null);
                    sm.setSuccess(res);
                } else
                    throw e;
            }
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
