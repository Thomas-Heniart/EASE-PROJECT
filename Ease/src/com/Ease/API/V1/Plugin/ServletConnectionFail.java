package com.Ease.API.V1.Plugin;

import com.Ease.Catalog.WebsiteFailure;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/v1/plugin/ConnectionFail")
public class ServletConnectionFail extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            JSONObject websiteFailures = sm.getJsonParam("websiteFailures", false, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            for (Object object : websiteFailures.entrySet()) {
                Map.Entry<String, Long> entry = (Map.Entry<String, Long>) object;
                String url = entry.getKey();
                Long count = entry.getValue();
                hibernateQuery.queryString("SELECT w FROM WebsiteFailure w WHERE w.url = :url");
                hibernateQuery.setParameter("url", url);
                WebsiteFailure websiteFailure = (WebsiteFailure) hibernateQuery.getSingleResult();
                if (websiteFailure == null)
                    websiteFailure = new WebsiteFailure(url, count);
                else
                    websiteFailure.incrementCount(count);
                sm.saveOrUpdate(websiteFailure);
            }
            sm.setSuccess("Email sent");
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
