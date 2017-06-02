package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.Account;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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
@WebServlet("/ServletAcceptSharedApp")
public class ServletAcceptSharedApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeTeamUser();
            Integer team_id = sm.getIntParam("team_id", true);
            Integer app_id = sm.getIntParam("app_id", true);
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            App app = (App) teamUser.getSharedAppWithId(app_id);
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.BadRequest, "Impossible to accept this app");
            if (((App) app.getHolder()).isClassicApp()) {
                Account account = ((ClassicApp) app).getAccount();
                account.decipherWithTeamKeyIfNeeded(teamUser.getDeciphered_teamKey());
                account.cipherWithKeyUser(sm);
            }
            app.beReceived(sm.getDB());
            sm.setSuccess("App accepted");
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
