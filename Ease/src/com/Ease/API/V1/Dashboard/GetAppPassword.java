package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamSingleCardReceiver;
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

@WebServlet("/api/v1/dashboard/GetAppPassword")
public class GetAppPassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer app_id = sm.getIntParam("app_id", true, false);
            App app = user.getDashboardManager().getApp(app_id);
            JSONObject res = new JSONObject();
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot ask password for this app");
            ClassicApp classicApp = (ClassicApp) app;
            if (classicApp.getAccount() == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This app is empty");
            TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
            if (teamCardReceiver != null) {
                if (teamCardReceiver.isTeamSingleCardReceiver() && !((TeamSingleCardReceiver) teamCardReceiver).isAllowed_to_see_password())
                    throw new HttpServletException(HttpStatus.Forbidden, "You are not allowed to see the password");
            }
            /* @TODO cipher password with server private key */
            res.put("password", classicApp.getAccount().getInformationNamed("password"));
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
