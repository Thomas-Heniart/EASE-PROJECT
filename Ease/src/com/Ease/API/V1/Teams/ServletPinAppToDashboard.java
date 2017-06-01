package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 19/05/2017.
 */
@WebServlet("/ServletPinAppToDashboard")
public class ServletPinAppToDashboard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeTeamUser();
            Integer app_id = sm.getIntParam("app_id", true);
            Integer profile_id = sm.getIntParam("profile_id", true);
            Integer team_id = sm.getIntParam("team_id", true);
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            User user = sm.getUser();
            Profile profile = user.getDashboardManager().getProfile(profile_id);
            App app = (App) teamUser.getSharedAppWithId(app_id);
            app.pinToDashboard(profile, sm.getDB());
            sm.setSuccess("App pined to dashboard");
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
