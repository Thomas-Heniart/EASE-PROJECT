package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/dashboard/MoveProfile")
public class ServletMoveProfile extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Integer column_index = sm.getIntParam("column_index", true, false);
            Integer position = sm.getIntParam("position", true, false);
            user.getDashboardManager().moveProfile(profile_id, column_index, position, sm.getHibernateQuery());
            sm.setSuccess(user.getDashboardManager().getProfile(profile_id).getJson());
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
