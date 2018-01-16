package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/UpdateMetrics")
public class ServletUpdateMetrics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT c FROM ClickOnApp c");
            List<ClickOnApp> metrics = hibernateQuery.list();
            for (ClickOnApp metric : metrics) {
                hibernateQuery.querySQLString("SELECT team_id FROM teamCards JOIN teamCardReceivers ON teamCardReceivers.teamCard_id = teamCards.id WHERE teamCardReceivers.app_id = :id");
                hibernateQuery.setParameter("id", metric.getApp_id());
                Integer team_id = (Integer) hibernateQuery.getSingleResult();
                if (team_id != null) {
                    metric.setTeam_id(team_id);
                    sm.saveOrUpdate(metric);
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
