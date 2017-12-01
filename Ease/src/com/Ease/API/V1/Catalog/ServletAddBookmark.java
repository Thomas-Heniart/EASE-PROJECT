package com.Ease.API.V1.Catalog;

import com.Ease.NewDashboard.AppInformation;
import com.Ease.NewDashboard.LinkApp;
import com.Ease.NewDashboard.LinkAppInformation;
import com.Ease.NewDashboard.Profile;
import com.Ease.User.User;
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

@WebServlet("/api/v1/catalog/AddBookmark")
public class ServletAddBookmark extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String url = sm.getStringParam("url", false, false);
            if (url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Url too long");
            String name = sm.getStringParam("name", true, false);
            if (name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            String img_url = sm.getStringParam("img_url", false, false);
            if (img_url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Name too long");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getProfile(profile_id);
            AppInformation appInformation = new AppInformation(name);
            LinkAppInformation linkAppInformation = new LinkAppInformation(url, img_url);
            LinkApp linkApp = new LinkApp(appInformation, linkAppInformation);
            linkApp.setProfile(profile);
            linkApp.setPosition(profile.getSize());
            sm.saveOrUpdate(linkApp);
            profile.addApp(linkApp);
            sm.addWebSocketMessage(WebSocketMessageFactory.createUserWebSocketMessage(WebSocketMessageType.DASHBOARD_APP, WebSocketMessageAction.CREATED, linkApp.getJson()));
            sm.setSuccess(linkApp.getJson());
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
