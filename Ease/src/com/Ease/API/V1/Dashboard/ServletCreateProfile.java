package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.ProfileInformation;
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
            Integer position = sm.getIntParam("position", true, false);
            Profile profile = new Profile(Integer.valueOf(user.getDBid()), column_index, position, new ProfileInformation(name));
            sm.saveOrUpdate(profile);
            user.getDashboardManager().addProfile(profile);
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
