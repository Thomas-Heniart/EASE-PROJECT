package com.Ease.API.V1.Dashboard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.User.User;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.SsoApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

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
            Profile profile = app.getProfile();
            if (app.getTeamCardReceiver() != null)
                throw new HttpServletException(HttpStatus.Forbidden);
            if (app.isWebsiteApp()) {
                if (!((WebsiteApp) app).getLogWithAppSet().isEmpty())
                    throw new HttpServletException(HttpStatus.BadRequest, "You must first delete apps using this app before delete it.");
                if (app.isSsoApp())
                    ((SsoApp) app).getSsoGroup().removeSsoApp(app);
            }
            if (profile != null)
                profile.removeAppAndUpdatePositions(app, hibernateQuery);
            sm.deleteObject(app);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.APP, WebSocketMessageAction.REMOVED, app_id));
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
