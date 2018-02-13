package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/DeleteApp")
public class ServletDeleteApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer app_id = sm.getIntParam("app_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            App app = user.getApp(app_id, hibernateQuery);
            hibernateQuery.queryString("DELETE FROM Update u WHERE u.app.db_id = :app_id");
            hibernateQuery.setParameter("app_id", app.getDb_id());
            hibernateQuery.executeUpdate();
            Profile profile = app.getProfile();
            if (app.getTeamCardReceiver() != null)
                throw new HttpServletException(HttpStatus.Forbidden);
            SsoGroup ssoGroup = null;
            if (app.isWebsiteApp()) {
                if (!((WebsiteApp) app).getLogWithAppSet().isEmpty())
                    throw new HttpServletException(HttpStatus.BadRequest, "You must first delete apps using this app before delete it.");
                if (app.isSsoApp()) {
                    SsoApp ssoApp = (SsoApp) app;
                    ssoGroup = ssoApp.getSsoGroup();
                    ssoGroup.removeSsoApp(ssoApp);
                }

            }
            if (profile != null)
                profile.removeAppAndUpdatePositions(app, hibernateQuery);
            sm.deleteObject(app);
            if (ssoGroup != null && ssoGroup.getSsoAppMap().isEmpty()) {
                user.removeSsoGroup(ssoGroup);
                sm.deleteObject(ssoGroup);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("app_id", app_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.REMOVED, jsonObject));
            sm.setSuccess("App deleted");
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
