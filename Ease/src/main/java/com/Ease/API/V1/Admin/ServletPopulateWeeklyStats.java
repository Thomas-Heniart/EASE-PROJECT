package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.ClickOnApp;
import com.Ease.Metrics.WeeklyStats;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@WebServlet("/api/v1/admin/PopulateWeeklyStats")
public class ServletPopulateWeeklyStats extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Calendar calendar = Calendar.getInstance();
            Integer currentYear = calendar.get(Calendar.YEAR);
            Integer currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.set(Calendar.YEAR, 2017);
            calendar.set(Calendar.WEEK_OF_YEAR, 49);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            while (calendar.get(Calendar.YEAR) != currentYear) {
                System.out.println("year: " + calendar.get(Calendar.YEAR) + "week: " + calendar.get(Calendar.WEEK_OF_YEAR));
                Calendar other = Calendar.getInstance();
                other.setTime(calendar.getTime());
                other.add(Calendar.WEEK_OF_YEAR, 1);
                Date this_week = other.getTime();
                hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND t.subscription_date >= :date_start AND t.subscription_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_companies = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT u FROM User u WHERE u.registration_date >= :date_start AND u.registration_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_users = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_apps = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NOT NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end AND r.teamCard.team.active IS true");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_team_apps = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT m FROM ClickOnApp m WHERE m.week_of_year = :week_of_year AND m.year = :year");
                hibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
                hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
                List<ClickOnApp> clickOnApps = hibernateQuery.list();
                int passwords_killed = 0;
                for (ClickOnApp clickOnApp : clickOnApps)
                    passwords_killed += clickOnApp.getTotalClicks();
                WeeklyStats weeklyStats = WeeklyStats.retrieveWeeklyStats(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
                weeklyStats.updateValues(new_companies, new_users, new_apps, new_team_apps, passwords_killed);
                hibernateQuery.saveOrUpdateObject(weeklyStats);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (calendar.get(Calendar.WEEK_OF_YEAR) != currentWeek) {
                System.out.println("year: " + calendar.get(Calendar.YEAR) + "week: " + calendar.get(Calendar.WEEK_OF_YEAR));
                Calendar other = Calendar.getInstance();
                other.setTime(calendar.getTime());
                other.add(Calendar.WEEK_OF_YEAR, 1);
                Date this_week = other.getTime();
                hibernateQuery.queryString("SELECT t FROM Team t WHERE t.active IS true AND t.subscription_date >= :date_start AND t.subscription_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_companies = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT u FROM User u WHERE u.registration_date >= :date_start AND u.registration_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_users = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_apps = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT a FROM App a LEFT JOIN a.teamCardReceiver as r WHERE r IS NOT NULL AND a.insert_date >= :date_start AND a.insert_date < :date_end AND r.teamCard.team.active IS true");
                hibernateQuery.setDate("date_start", calendar.getTime());
                hibernateQuery.setDate("date_end", this_week);
                int new_team_apps = hibernateQuery.list().size();
                hibernateQuery.queryString("SELECT m FROM ClickOnApp m WHERE m.week_of_year = :week_of_year AND m.year = :year");
                hibernateQuery.setParameter("week_of_year", calendar.get(Calendar.WEEK_OF_YEAR));
                hibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
                List<ClickOnApp> clickOnApps = hibernateQuery.list();
                int passwords_killed = 0;
                for (ClickOnApp clickOnApp : clickOnApps)
                    passwords_killed += clickOnApp.getTotalClicks();
                WeeklyStats weeklyStats = WeeklyStats.retrieveWeeklyStats(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
                weeklyStats.updateValues(new_companies, new_users, new_apps, new_team_apps, passwords_killed);
                hibernateQuery.saveOrUpdateObject(weeklyStats);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
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
