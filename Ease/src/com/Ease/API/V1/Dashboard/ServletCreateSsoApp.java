package com.Ease.API.V1.Dashboard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.User.User;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.AppInformation;
import com.Ease.NewDashboard.Profile;
import com.Ease.NewDashboard.SsoApp;
import com.Ease.NewDashboard.SsoGroup;
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

@WebServlet("/api/v1/dashboard/CreateSsoApp")
public class ServletCreateSsoApp extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            Integer ssoGroup_id = sm.getIntParam("sso_group_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT ssoGroup FROM SsoGroup ssoGroup WHERE ssoGroup.id = :id");
            hibernateQuery.setParameter("id", ssoGroup_id);
            SsoGroup ssoGroup = (SsoGroup) hibernateQuery.getSingleResult();
            if (ssoGroup == null || !ssoGroup.getUser().equals(user))
                throw new HttpServletException(HttpStatus.BadRequest, "No such SsoGroup");
            Integer website_id = sm.getIntParam("website_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id, sm.getHibernateQuery());
            if (!ssoGroup.getSso().equals(website.getSso()))
                throw new HttpServletException(HttpStatus.BadRequest, "This website is not part of the sso");
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            Integer profile_id = sm.getIntParam("profile_id", true, false);
            Profile profile = user.getProfile(profile_id);
            SsoApp ssoApp = new SsoApp(new AppInformation(name), website, ssoGroup);
            ssoApp.setProfile(profile);
            ssoApp.setPosition(profile.getSize());
            profile.addApp(ssoApp);
            sm.saveOrUpdate(ssoApp);
            ssoGroup.addSsoApp(ssoApp);
            sm.setSuccess(ssoApp.getJson());
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
