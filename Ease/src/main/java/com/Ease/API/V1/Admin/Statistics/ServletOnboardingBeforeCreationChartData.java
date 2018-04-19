package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.EaseEvent;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/onboarding-before-creation-chart-data")
public class ServletOnboardingBeforeCreationChartData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'OnboardingEvent'");
            List<EaseEvent> easeEvents = trackingHibernateQuery.list();
            JSONArray res = new JSONArray();
            res.put(easeEvents.stream().filter(easeEvent -> easeEvent.getType().equalsIgnoreCase("PageVisit")).count());
            res.put(easeEvents.stream().filter(easeEvent -> easeEvent.getType().equalsIgnoreCase("EmailSubmit")).count());
            res.put(easeEvents.stream().filter(easeEvent -> easeEvent.getType().equalsIgnoreCase("DigitsSubmit")).count());
            res.put(easeEvents.stream().filter(easeEvent -> easeEvent.getType().equalsIgnoreCase("CompanyInformationSubmit")).count());
            res.put(easeEvents.stream().filter(easeEvent -> easeEvent.getType().equalsIgnoreCase("PersonalInformationSubmit")).count());
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

    public static void main(String[] args) throws Exception {
        HibernateQuery hibernateQuery = new HibernateQuery("tracking");
        for (int i = 0; i < 30; i++) {
            EaseEvent easeEvent = new EaseEvent();
            easeEvent.setName("OnboardingEvent");
            easeEvent.setData(new JSONObject().put("type", "DigitsSubmit"));
            hibernateQuery.saveOrUpdateObject(easeEvent);
        }
        hibernateQuery.commit();
    }
}
