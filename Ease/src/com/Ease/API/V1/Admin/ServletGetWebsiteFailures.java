package com.Ease.API.V1.Admin;

import com.Ease.Catalog.WebsiteFailure;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/GetWebsiteFailures")
public class ServletGetWebsiteFailures extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            JSONArray res = new JSONArray();
            hibernateQuery.queryString("SELECT w FROM WebsiteFailure w ORDER BY w.count DESC");
            List<WebsiteFailure> websiteFailureList = hibernateQuery.list();
            for (WebsiteFailure websiteFailure : websiteFailureList)
                res.add(websiteFailure.getJson());
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
