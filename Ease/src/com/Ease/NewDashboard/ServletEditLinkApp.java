package com.Ease.NewDashboard;

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

@WebServlet("/api/v1/dashboard/ServletEditLinkApp")
public class ServletEditLinkApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer app_id = sm.getIntParam("app_id", true, false);
            User user = sm.getUser();
            App app = user.getDashboardManager().getApp(app_id);
            if (!app.isClassicApp())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (app.getTeamCardReceiver() != null)
                throw new HttpServletException(HttpStatus.Forbidden);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            String url = sm.getStringParam("url", false, false);
            if (url.equals("") || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String img_url = sm.getStringParam("img_url", false, false);
            if (img_url.equals("") || img_url.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter img_url");
            LinkApp linkApp = (LinkApp) app;
            app.getAppInformation().setName(name);
            linkApp.getLinkAppInformation().setUrl(url);
            linkApp.getLinkAppInformation().setImg_url(img_url);
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
