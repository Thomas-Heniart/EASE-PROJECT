package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/PopulateAlternativeUrls")
public class ServletPopulateAlternativeUrls extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            List<Website> websites = (List<Website>) catalog.getWebsites(sm.getHibernateQuery());
            for (Website website : websites) {
                String login_url = website.getLogin_url();
                String[] protocol_and_url = login_url.split("\\/\\/");
                if (protocol_and_url.length < 2)
                    continue;
                String protocol = protocol_and_url[0];
                String url = protocol_and_url[1];
                String[] domain = url.split("\\.");
                if (domain.length == 2) {
                    if (protocol.endsWith("s:")) {
                        website.addWebsiteAlternativeUrl("http://" + url, sm.getHibernateQuery());
                        website.addWebsiteAlternativeUrl("http://www." + url, sm.getHibernateQuery());
                    }
                    sm.saveOrUpdate(website);
                }
            }
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
