package com.Ease.API.V1.Catalog;

import com.Ease.Catalog.*;
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
import java.util.HashSet;
import java.util.Set;

@WebServlet("/api/v1/catalog/WebsiteRequest")
public class ServletWebsiteRequest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String url = sm.getStringParam("url", false, false);
            if (url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Url too long");
            WebsiteAttributes websiteAttributes = new WebsiteAttributes(true);
            Website website = new Website(url, "TODO", "Undefined", url, websiteAttributes);
            WebsiteInformation loginInformation = new WebsiteInformation("login", "text", 0, "Login", "fa-user-o", website);
            WebsiteInformation passwordInformation = new WebsiteInformation("password", "password", 1, "Password", "fa-lock", website);
            Set<WebsiteInformation> websiteInformationSet = new HashSet<>();
            websiteInformationSet.add(loginInformation);
            websiteInformationSet.add(passwordInformation);
            website.setWebsiteInformationList(websiteInformationSet);
            sm.saveOrUpdate(websiteAttributes);
            sm.saveOrUpdate(website);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            catalog.addWebsite(website);
            String email = user.getEmail();
            WebsiteRequest websiteRequest = new WebsiteRequest(url, email);
            sm.saveOrUpdate(websiteRequest);
            sm.setSuccess("Request sent");
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
