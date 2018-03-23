package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.EaseEvent;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

@WebServlet("/api/v1/admin/GetAppTypesStatistics")
public class ServletGetAppTypesStatistics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            JSONObject res = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray totals = new JSONArray();
            JSONArray classic = new JSONArray();
            JSONArray any = new JSONArray();
            Calendar start_calendar = Calendar.getInstance();
            Calendar end_calendar = Calendar.getInstance();
            Long start_week_ms = sm.getLongParam("start_week_ms", true, true);
            if (start_week_ms == null)
                start_calendar.add(Calendar.WEEK_OF_YEAR, -4);
            else
                start_calendar.setTimeInMillis(start_week_ms);
            start_calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Long end_week_ms = sm.getLongParam("end_week_ms", true, true);
            if (end_week_ms != null) {
                if (start_week_ms != null && end_week_ms < start_week_ms)
                    throw new HttpServletException(HttpStatus.BadRequest, "End cannot be before start");
                end_calendar.setTimeInMillis(end_week_ms);
            }
            /* for a week */
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, totals, classic, any);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, totals, classic, any);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            /* end of a week */
            res.put("labels", labels);
            res.put("totals", totals);
            res.put("classicApps", classic);
            res.put("anyApps", any);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void trackWeek(HibernateQuery trackingHibernateQuery, Calendar calendar, JSONArray labels, JSONArray totals, JSONArray classic, JSONArray any) {
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'PasswordUsed' AND e.year = :year AND e.week_of_year = :week_of_year");
        trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<EaseEvent> easeEvents = trackingHibernateQuery.list();
        double total = easeEvents.size();
        totals.put(total);
        if (total == 0.) {
            classic.put(0);
            any.put(0);
        } else {
            classic.put(easeEvents.stream().filter(EaseEvent::isClassicAppUsed).count() / total * 100);
            any.put(easeEvents.stream().filter(EaseEvent::isAnyAppUsed).count() / total * 100);
        }
        labels.put("Week " + calendar.get(Calendar.WEEK_OF_YEAR) + ", " + calendar.get(Calendar.YEAR) + "(" + Math.round(total) + ")");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
