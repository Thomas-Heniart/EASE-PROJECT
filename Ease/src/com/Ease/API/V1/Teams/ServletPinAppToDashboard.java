package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String app_id = sm.getServletParam("app_id", true);
            String profile_id = sm.getServletParam("profile_id", true);
            String team_id = sm.getServletParam("team_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            if (app_id == null || app_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "App is null");
            if (profile_id == null || profile_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Profile is null");
            TeamUser teamUser = sm.getTeamUserForTeamId(Integer.parseInt(team_id));
            User user = sm.getUser();
            Profile profile = user.getDashboardManager().getProfile(Integer.parseInt(profile_id));
            App app = (App) teamUser.getSharedAppWithId(Integer.parseInt(app_id));
            app.pinToDashboard(profile, sm);
            sm.setResponse(ServletManager.Code.Success, "App pined to dashboard");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
