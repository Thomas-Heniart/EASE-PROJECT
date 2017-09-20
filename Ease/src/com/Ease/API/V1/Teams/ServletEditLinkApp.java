package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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

@WebServlet("/api/v1/teams/EditLinkApp")
public class ServletEditLinkApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            Integer app_id = sm.getIntParam("app_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            ShareableApp shareableApp = team.getAppManager().getShareableAppWithId(app_id);
            App app = (App) shareableApp;
            if (!app.isLinkApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            JSONObject params = new JSONObject();
            params.put("name", sm.getStringParam("name", true, false));
            params.put("description", sm.getStringParam("description", true, false));
            String url = sm.getStringParam("url", false, false);
            String img_url = sm.getStringParam("img_url", false, false);
            if (!Regex.isValidLink(url))
                throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid url.");
            params.put("url", url);
            if (!img_url.equals("") && !Regex.isValidLink(img_url))
                throw new HttpServletException(HttpStatus.BadRequest, "Url entered for icon is invalid.");
            params.put("img_url", img_url);
            shareableApp.modifyShareable(sm.getDB(), params);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, shareableApp.getShareableJson(), shareableApp.getOrigin()));
            sm.setSuccess(shareableApp.getShareableJson());
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
