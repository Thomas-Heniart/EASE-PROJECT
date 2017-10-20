package com.Ease.API.V1.Admin;

import com.Ease.Catalog.WebsiteRequest;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/GetWebsiteRequests")
public class ServletGetWebsiteRequests extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT w FROM WebsiteRequest w");
            List<WebsiteRequest> websiteRequests = hibernateQuery.list();
            JSONArray requests = new JSONArray();
            for (WebsiteRequest websiteRequest : websiteRequests)
                requests.add(websiteRequest.getJson());
            JSONObject res = new JSONObject();
            res.put("website_requests", websiteRequests);
            sm.setSuccess(res);
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
