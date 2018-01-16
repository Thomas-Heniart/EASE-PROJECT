package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteRequest;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/DeleteWebsiteRequest")
public class ServletDeleteWebsiteRequest extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer website_id = sm.getIntParam("website_id", true, false);
            Integer website_request_id = sm.getIntParam("website_request_id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(website_id, sm.getHibernateQuery());
            WebsiteRequest websiteRequest = null;
            for (WebsiteRequest websiteRequest1 : website.getWebsiteRequests()) {
                if (websiteRequest1.getDb_id().equals(website_request_id))
                    websiteRequest = websiteRequest1;
            }
            website.removeWebsiteRequest(websiteRequest);
            sm.deleteObject(websiteRequest);
            sm.setSuccess("Done");
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
