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
import java.util.stream.Collectors;

@WebServlet("/api/v1/admin/GetAppProvenanceStatistics")
public class ServletGetAppProvenanceStatistics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            JSONObject res = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray totals = new JSONArray();
            JSONArray single = new JSONArray();
            JSONArray enterprise = new JSONArray();
            JSONArray pro = new JSONArray();
            JSONArray perso = new JSONArray();
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
                trackWeek(trackingHibernateQuery, start_calendar, labels, totals, single, enterprise, pro, perso);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                trackWeek(trackingHibernateQuery, start_calendar, labels, totals, single, enterprise, pro, perso);
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            res.put("labels", labels);
            res.put("totals", totals);
            res.put("single", single);
            res.put("pro", pro);
            res.put("perso", perso);
            res.put("enterprise", enterprise);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void trackWeek(HibernateQuery trackingHibernateQuery, Calendar calendar, JSONArray labels, JSONArray totals, JSONArray single, JSONArray enterprise, JSONArray pro, JSONArray perso) {
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'CardAdded' AND e.year = :year AND e.week_of_year = :week_of_year");
        trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        List<EaseEvent> easeEvents = trackingHibernateQuery.list();
        easeEvents = easeEvents.stream().filter(easeEvent -> !easeEvent.getType().isEmpty()).collect(Collectors.toList());
        double total = easeEvents.size();
        double proApps = easeEvents.size();
        if (total == 0.) {
            single.put(0);
            enterprise.put(0);
        } else {
            single.put(easeEvents.stream().filter(EaseEvent::isSingle).count() / total * 100);
            enterprise.put(easeEvents.stream().filter(EaseEvent::isEnterprise).count() / total * 100);
        }
        trackingHibernateQuery.queryString("SELECT e FROM EaseEvent e WHERE e.name LIKE 'AppAdded' AND e.year = :year AND e.week_of_year = :week_of_year");
        trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
        trackingHibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
        easeEvents = trackingHibernateQuery.list();
        easeEvents = easeEvents.stream().filter(easeEvent -> !easeEvent.getType().isEmpty()).collect(Collectors.toList());
        double persoApps = easeEvents.size();
        if (proApps == 0.)
            pro.put(0);
        else
            pro.put(proApps / (proApps + persoApps) * 100);
        if (persoApps == 0)
            perso.put(0);
        else
            perso.put(persoApps / (proApps + persoApps) * 100);
        labels.put("Week " + calendar.get(Calendar.WEEK_OF_YEAR) + ", " + calendar.get(Calendar.YEAR) + "(" + Math.round(total) + ")");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
