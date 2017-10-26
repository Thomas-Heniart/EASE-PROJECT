package com.Ease.API.V1.Catalog;

import com.Ease.Dashboard.App.LinkApp.LinkApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
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

@WebServlet("/api/v1/catalog/AddBookmark")
public class ServletAddBookmark extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            User user = sm.getUser();
            if (user == null)
                user = sm.getUserWithToken();
            String url = sm.getStringParam("url", false, false);
            if (url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Url too long");
            String name = sm.getStringParam("name", true, false);
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            String img_url = sm.getStringParam("img_url", false, false);
            if (img_url.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getDashboardManager().getProfileWithId(profile_id);
            LinkApp app = LinkApp.createLinkApp(profile, profile.getApps().size(), name, url, img_url, sm.getDB());
            profile.addApp(app);
            sm.setSuccess(app.getJson());
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
