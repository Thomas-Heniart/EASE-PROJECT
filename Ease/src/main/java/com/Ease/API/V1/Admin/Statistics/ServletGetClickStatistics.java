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

@WebServlet("/api/v1/admin/GetClickHistoryStatistics")
public class ServletGetClickStatistics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            JSONObject res = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray clicks = new JSONArray();
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
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, clicks);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, clicks);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            res.put("labels", labels);

            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void trackWeek(HibernateQuery trackingHibernateQuery, Calendar calendar, JSONArray labels, JSONArray clicks) {
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE (e.name LIKE 'PasswordUsed' OR e.name LIKE 'PasswordUser') AND e.year = :year AND e.week_of_year = :week_of_year");
        trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<EaseEvent> easeEvents = trackingHibernateQuery.list();
        clicks.put(easeEvents.size());
        labels.put("Week " + calendar.get(Calendar.WEEK_OF_YEAR) + ", " + calendar.get(Calendar.YEAR));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
