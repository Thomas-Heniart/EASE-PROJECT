package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
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

@WebServlet("/api/v1/teams/PinLinkApp")
public class ServletPinLinkApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(sm.getIntParam("app_id", true, false));
            App app = (App) shareableApp;
            if (!app.isLinkApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            User user = sm.getUser();
            SharedApp sharedApp = shareableApp.getSharedAppForTeamUser(teamUser);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            if (sharedApp == null) {
                sharedApp = shareableApp.share(teamUser, team, new JSONObject(), sm);
                shareableApp.addSharedApp(sharedApp);
                team.getAppManager().addSharedApp(sharedApp);
            }
            if (profile_id == -1) {
                team.getAppManager().removeSharedApp(sharedApp);
                sharedApp.deleteShared(db);
                shareableApp.removeSharedApp(sharedApp);
            } else {
                String name = sm.getStringParam("app_name", true, false);
                if (name == null || name.equals(""))
                    throw new HttpServletException(HttpStatus.BadRequest, "You cannot leave name empty.");
                Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
                app = (App) sharedApp;
                app.setName(name, db);
                sharedApp.pinToDashboard(profile, db);
            }
            db.commitTransaction(transaction);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), shareableApp.getOrigin()));
            sm.setSuccess(sharedApp.getSharedJSON());
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
