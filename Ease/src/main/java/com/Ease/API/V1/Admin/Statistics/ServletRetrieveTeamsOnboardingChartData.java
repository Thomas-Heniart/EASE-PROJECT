package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.OnboardingStatus;
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

@WebServlet("/api/v1/admin/RetrieveTeamsOnboardingChartData")
public class ServletRetrieveTeamsOnboardingChartData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject res = new JSONObject();
            res.put("type", "bar");
            JSONArray labels = new JSONArray()
                    .put("Subscription")
                    .put("Channels creation")
                    .put("Invite people")
                    .put("Add people to rooms")
                    .put("Importation done")
                    .put("Apps added");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT t.onboardingStatus FROM Team t WHERE t.active IS true");
            List<OnboardingStatus> onboardingStatuses = hibernateQuery.list();
            JSONArray datasetsData = new JSONArray();
            for (int i = 0; i < 6; i++)
                datasetsData.put(0);
            for (OnboardingStatus onboardingStatus : onboardingStatuses) {
                int step = onboardingStatus.getStep();
                for (int i=0; i <= step; i++) {
                    int val = datasetsData.getInt(i);
                    datasetsData.put(i, val + 1);
                }
            }
            JSONObject data = new JSONObject();
            JSONArray datasets = new JSONArray();
            datasets.put(new JSONObject()
                    .put("label", "Onboarding steps")
                    .put("data", datasetsData)
            );
            data.put("datasets", datasets);
            data.put("labels", labels);
            res.put("data", data);
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
