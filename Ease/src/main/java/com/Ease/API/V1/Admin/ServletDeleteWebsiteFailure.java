package com.Ease.API.V1.Admin;

import com.Ease.Catalog.WebsiteFailure;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/DeleteWebsiteFailure")
public class ServletDeleteWebsiteFailure extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            String url = sm.getStringParam("url", false, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT w FROM WebsiteFailure w WHERE w.url = :url");
            hibernateQuery.setParameter("url", url);
            WebsiteFailure websiteFailure = (WebsiteFailure) hibernateQuery.getSingleResult();
            if (websiteFailure != null)
                sm.deleteObject(websiteFailure);
            sm.setSuccess("Website failure deleted");
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
