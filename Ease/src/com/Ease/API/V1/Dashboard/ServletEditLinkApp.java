package com.Ease.API.V1.Dashboard;

import com.Ease.Dashboard.User.User;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LinkApp;
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

@WebServlet("/api/v1/dashboard/EditLinkApp")
public class ServletEditLinkApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            App app = user.getDashboardManager().getApp(app_id);
            if (!app.isLinkApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (app.getTeamCardReceiver() != null)
                throw new HttpServletException(HttpStatus.Forbidden);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String url = sm.getStringParam("url", true, false);
            if (url.length() > 2000 || url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String img_url = sm.getStringParam("img_url", true, false);
            if (img_url.length() > 2000 || img_url.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            LinkApp linkApp = (LinkApp) app;
            linkApp.getLinkAppInformation().setImg_url(img_url);
            linkApp.getLinkAppInformation().setUrl(url);
            app.getAppInformation().setName(name);
            sm.saveOrUpdate(app);
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
