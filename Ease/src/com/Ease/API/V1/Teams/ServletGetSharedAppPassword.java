package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
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

@WebServlet("/api/v1/teams/GetSharedAppPassword")
public class ServletGetSharedAppPassword extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, false);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeAdminOfTeam(team_id);
            Integer app_id = sm.getIntParam("shared_app_id", true);
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            SharedApp sharedApp = teamUser.getTeam().getAppManager().getSharedApp(app_id);
            if (sharedApp.getTeamUser_tenant() != teamUser || !teamUser.isTeamAdmin())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot retrieve account information for this app.");
            App app = (App) sharedApp;
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot retrieve account information for this app.");
            ClassicApp classicApp = (ClassicApp) app;
            String password = classicApp.getAccount().getInformationNamed("password");
            if (password == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No password for this app.");
            JSONObject res = new JSONObject();
            String key = (String) sm.getSession().getAttribute("public_key");
            res.put("password", RSA.Encrypt(password, key));
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