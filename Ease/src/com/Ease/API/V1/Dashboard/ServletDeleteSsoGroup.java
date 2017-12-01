package com.Ease.API.V1.Dashboard;

import com.Ease.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.SsoApp;
import com.Ease.NewDashboard.SsoGroup;
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

@WebServlet("/api/v1/dashboard/DeleteSsoGroup")
public class ServletDeleteSsoGroup extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer sso_group_id = sm.getIntParam("sso_group_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT s FROM SsoGroup s WHERE s.id = :id");
            hibernateQuery.setParameter("id", sso_group_id);
            SsoGroup ssoGroup = (SsoGroup) hibernateQuery.getSingleResult();
            if (ssoGroup == null || !ssoGroup.getUser().equals(user))
                throw new HttpServletException(HttpStatus.BadRequest, "No such SsoGroup");
            for (SsoApp ssoApp : ssoGroup.getSsoAppMap().values()) {
                Profile profile = ssoApp.getProfile();
                if (profile != null)
                    profile.removeAppAndUpdatePositions(ssoApp, sm.getUserWebSocketManager(user.getDb_id()), hibernateQuery);
            }
            sm.deleteObject(ssoGroup);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.SSO_GROUP, WebSocketMessageAction.REMOVED, sso_group_id));
            sm.setSuccess("SsoGroup deleted");
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
