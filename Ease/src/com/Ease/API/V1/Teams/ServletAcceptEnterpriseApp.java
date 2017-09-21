package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.Crypto.RSA;
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
import java.util.Map;

@WebServlet("/api/v1/teams/AcceptEnterpriseApp")
public class ServletAcceptEnterpriseApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            Integer shared_app_id = sm.getIntParam("shared_app_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            SharedApp sharedApp = team.getAppManager().getSharedApp(shared_app_id);
            App holder = (App) sharedApp.getHolder();
            if (!holder.isEmpty())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamUser teamUser_connected = sm.getTeamUserForTeam(team);
            if (teamUser_connected != sharedApp.getTeamUser_tenant())
                throw new HttpServletException(HttpStatus.Forbidden);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            if (account_information.isEmpty())
                throw new HttpServletException(HttpStatus.BadRequest, "You must fill your account.");
            String key = (String) sm.getContextAttr("privateKey");
            for (Object entry : account_information.entrySet()) {
                Map.Entry<String, String> type_value = (Map.Entry<String, String>) entry;
                account_information.put(type_value.getKey(), RSA.Decrypt(type_value.getValue(), key));
            }
            JSONObject params = new JSONObject();
            params.put("account_information", account_information);
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            sharedApp.modifyShared(db, params);
            sharedApp.accept(db);
            db.commitTransaction(transaction);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, sharedApp.getHolder().getShareableJson(), sharedApp.getHolder().getOrigin()));
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
