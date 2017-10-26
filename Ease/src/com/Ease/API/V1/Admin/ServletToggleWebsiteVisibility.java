package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.WebsiteAttributes;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/ToggleWebsiteVisibility")
public class ServletToggleWebsiteVisibility extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer website_id = sm.getIntParam("id", true, false);
            Boolean is_private = sm.getBooleanParam("private", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            WebsiteAttributes websiteAttributes = catalog.getWebsiteWithId(website_id).getWebsiteAttributes();
            websiteAttributes.setPublic_website(!is_private);
            sm.saveOrUpdate(websiteAttributes);
            sm.setSuccess("Catalog edited");
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
