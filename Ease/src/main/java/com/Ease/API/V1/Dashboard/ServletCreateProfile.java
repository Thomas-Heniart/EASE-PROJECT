package com.Ease.API.V1.Dashboard;

import com.Ease.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
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

@WebServlet("/api/v1/dashboard/CreateProfile")
public class ServletCreateProfile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String name = sm.getStringParam("name", true, false);
            if (name == null || name.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Empty name");
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Integer column_index = sm.getIntParam("column_index", true, false);
            if (column_index > Profile.MAX_COLUMN_INDEX || column_index < Profile.MIN_COLUMN_INDEX)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter column_index");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT MAX(p.position_index) FROM Profile p WHERE p.user = :user AND p.column_index = :column_index");
            hibernateQuery.setParameter("user", user);
            hibernateQuery.setParameter("column_index", column_index);
            Integer position = (Integer) hibernateQuery.getSingleResult();
            if (position == null)
                position = 0;
            else
                position++;
            Profile profile = new Profile(user, column_index, position, new ProfileInformation(name));
            sm.saveOrUpdate(profile);
            user.addProfile(profile);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_PROFILE, WebSocketMessageAction.CREATED, profile.getWebSocketJson()));
            sm.setSuccess(profile.getJson());
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
