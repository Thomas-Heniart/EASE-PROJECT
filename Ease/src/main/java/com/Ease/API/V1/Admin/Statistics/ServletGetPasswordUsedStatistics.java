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
import java.util.Calendar;
import java.util.List;

@WebServlet("/api/v1/admin/GetPasswordUsedStatistics")
public class ServletGetPasswordUsedStatistics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            JSONObject res = new JSONObject();
            Calendar calendar = Calendar.getInstance();
            JSONArray labels = new JSONArray();
            JSONArray totals = new JSONArray();
            JSONArray fromDashboardClick = new JSONArray();
            JSONArray fromExtension = new JSONArray();
            JSONArray fromFillIn = new JSONArray();
            JSONArray fromCopy = new JSONArray();

            /* for a week */

            trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'PasswordUsed' AND e.year = :year AND e.week_of_year = :week_of_year");
            trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
            trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
            List<EaseEvent> easeEvents = trackingHibernateQuery.list();
            int total = easeEvents.size();
            totals.put(total);
            fromDashboardClick.put(Math.toIntExact(easeEvents.stream().filter(EaseEvent::isFromDashboardClick).count()) / total * 100);
            fromExtension.put(Math.toIntExact(easeEvents.stream().filter(EaseEvent::isFromExtension).count()) / total * 100);
            fromFillIn.put(Math.toIntExact(easeEvents.stream().filter(EaseEvent::isFromFillIn).count()) / total * 100);
            fromCopy.put(Math.toIntExact(easeEvents.stream().filter(EaseEvent::isFromCopy).count()) / total * 100);
            labels.put("Week " + calendar.get(Calendar.WEEK_OF_YEAR) + ", " + calendar.get(Calendar.YEAR));

            /* end of a week */
            res.put("labels", labels);
            res.put("totals", totals);
            res.put("fromDashboardClick", fromDashboardClick);
            res.put("fromExtension", fromExtension);
            res.put("fromFillIn", fromFillIn);
            res.put("fromCopy", fromCopy);
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
