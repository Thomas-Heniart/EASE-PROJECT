package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
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
@WebServlet("/ServletAcceptSharedApp")
public class ServletAcceptSharedApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            sm.needToBeTeamUser();
            String team_id = sm.getServletParam("team_id", true);
            String app_id = sm.getServletParam("app_id", true);
            if (team_id == null || team_id.equals(""))
                throw new GeneralException(ServletManager.Code.ClientError, "Team is null");
            TeamUser teamUser = sm.getTeamUserForTeamId(Integer.parseInt(team_id));
            App app = (App) teamUser.getSharedAppWithId(Integer.parseInt(app_id));
            if (!app.isClassicApp())
                throw new GeneralException(ServletManager.Code.ClientError, "Impossible to accept this app");
            if (app.isClassicApp())
                ((ClassicApp)app).getAccount().decipherAndCipher(teamUser.getDeciphered_teamPrivateKey(), sm);
            sm.setResponse(ServletManager.Code.Success, "App accepted");
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
