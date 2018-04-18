package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.JSONArray;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/api/v1/admin/GetTeamsCohortData")
public class ServletGetTeamsCohortData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
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
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            end_calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            Integer avg_clicks = sm.getIntParam("avg_clicks", true, true);
            if (avg_clicks == null)
                avg_clicks = 1;
            JSONArray cohort = new JSONArray();
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR))
                trackFullWeek(start_calendar, end_calendar, avg_clicks, trackingHibernateQuery, hibernateQuery, cohort);
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR))
                trackFullWeek(start_calendar, end_calendar, avg_clicks, trackingHibernateQuery, hibernateQuery, cohort);
            sm.setSuccess(cohort);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void trackFullWeek(Calendar start_calendar, Calendar end_calendar, Integer avg_clicks, HibernateQuery trackingHibernateQuery, HibernateQuery hibernateQuery, JSONArray cohort) {
        Calendar subscription_week = Calendar.getInstance();
        subscription_week.setTime(start_calendar.getTime());
        subscription_week.add(Calendar.WEEK_OF_YEAR, 1);
        JSONArray this_week = new JSONArray();
        hibernateQuery.queryString("SELECT t.db_id FROM Team t WHERE t.subscription_date BETWEEN :start_week AND :subscription_week");
        hibernateQuery.setDate("start_week", start_calendar);
        hibernateQuery.setDate("subscription_week", subscription_week);
        List<Integer> teamIds = hibernateQuery.list();
        this_week.put(teamIds.size());
        int weeksAdded = 0;
        List<Integer> userIds = new LinkedList<>();
        if (!teamIds.isEmpty()) {
            hibernateQuery.queryString("SELECT DISTINCT tu.user.db_id FROM TeamUser tu WHERE tu.team.db_id IN (:teamIds)");
            hibernateQuery.setParameter("teamIds", teamIds);
            userIds = hibernateQuery.list();
        }
        if (!userIds.isEmpty()) {
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR)) {
                trackWeek(start_calendar, avg_clicks, trackingHibernateQuery, this_week, userIds, hibernateQuery);
                weeksAdded++;
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                trackWeek(start_calendar, avg_clicks, trackingHibernateQuery, this_week, userIds, hibernateQuery);
                weeksAdded++;
            }
        } else {
            while (start_calendar.get(Calendar.YEAR) < end_calendar.get(Calendar.YEAR)) {
                this_week.put(0);
                weeksAdded++;
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (start_calendar.get(Calendar.WEEK_OF_YEAR) <= end_calendar.get(Calendar.WEEK_OF_YEAR)) {
                this_week.put(0);
                weeksAdded++;
                start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }

        }
        start_calendar.add(Calendar.WEEK_OF_YEAR, -weeksAdded);
        cohort.put(this_week);
        start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
    }

    private void trackWeek(Calendar start_calendar, Integer avg_clicks, HibernateQuery trackingHibernateQuery, JSONArray this_week, List<Integer> userIds, HibernateQuery hibernateQuery) {
        trackingHibernateQuery.querySQLString("SELECT DISTINCT t.uID FROM (SELECT user_id AS uId, COUNT(name) AS n FROM EASE_EVENT WHERE user_id IN (:userIds) AND (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser') AND creation_date BETWEEN :start_week AND :end_week GROUP BY user_id) AS t WHERE t.n >= :avg_clicks");
        trackingHibernateQuery.setParameter("start_week", start_calendar.getTime());
        trackingHibernateQuery.setParameter("avg_clicks", avg_clicks);
        start_calendar.add(Calendar.WEEK_OF_YEAR, 1);
        trackingHibernateQuery.setParameter("end_week", start_calendar.getTime());
        trackingHibernateQuery.setParameter("userIds", userIds);
        List<Integer> activeUserIds = trackingHibernateQuery.list();
        int teamCount = 0;
        if (!activeUserIds.isEmpty()) {
            hibernateQuery.queryString("SELECT DISTINCT tu.team FROM TeamUser tu WHERE tu.user IS NOT NULL AND tu.user.db_id IN (:userIds)");
            hibernateQuery.setParameter("userIds", activeUserIds);
            teamCount = hibernateQuery.list().size();
        }
        this_week.put(teamCount);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
