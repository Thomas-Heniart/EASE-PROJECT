package com.Ease.API.V1.Admin.upgrade;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Metrics.TeamMetrics;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebServlet("/api/v1/admin/update-team-metrics")
public class ServletUpdateTeamMetrics extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            HibernateQuery trackingHibernateQuery = sm.getTrackingHibernateQuery();
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.set(2017, Calendar.SEPTEMBER, 14);
            while (calendar.get(Calendar.YEAR) < year || (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.WEEK_OF_YEAR) <= weekOfYear)) {
                this.updateMetrics(hibernateQuery, trackingHibernateQuery, calendar, (TeamManager) sm.getContextAttr("teamManager"));
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            sm.setSuccess("Success");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void updateMetrics(HibernateQuery hibernateQuery, HibernateQuery trackingHibernateQuery, Calendar calendar, TeamManager teamManager) {
        for (Team team : teamManager.getTeams(hibernateQuery)) {
            TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), hibernateQuery);
            int people_click_on_app_once = 0;
            StringBuilder people_click_on_app_once_emails = new StringBuilder();
            int people_click_on_app_three_times = 0;
            StringBuilder people_click_on_app_three_times_emails = new StringBuilder();
            int people_click_on_app_five_times = 0;
            StringBuilder people_click_on_app_five_times_emails = new StringBuilder();
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (!teamUser.isRegistered())
                    continue;
                User user = teamUser.getUser();
                trackingHibernateQuery.querySQLString("SELECT\n" +
                        "        year,\n" +
                        "        day_of_year,\n" +
                        "        COUNT(*) AS clicks\n" +
                        "      FROM (\n" +
                        "             SELECT\n" +
                        "               year,\n" +
                        "               day_of_year,\n" +
                        "               week_of_year,\n" +
                        "               id\n" +
                        "             FROM ease_tracking.EASE_EVENT\n" +
                        "             WHERE (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser') AND user_id = :userId) AS t\n" +
                        "      WHERE year = :year AND week_of_year = :weekOfYear\n" +
                        "      GROUP BY year, day_of_year, week_of_year\n" +
                        "      ORDER BY year, day_of_year;");
                trackingHibernateQuery.setParameter("userId", user.getDb_id());
                trackingHibernateQuery.setParameter("weekOfYear", calendar.get(Calendar.WEEK_OF_YEAR));
                trackingHibernateQuery.setParameter("year", calendar.get(Calendar.YEAR));
                int weekClicks = trackingHibernateQuery.list().size();
                if (weekClicks >= 1) {
                    people_click_on_app_once++;
                    people_click_on_app_once_emails.append(teamUser.getEmail()).append(";");
                }
                if (weekClicks >= 3) {
                    people_click_on_app_three_times++;
                    people_click_on_app_three_times_emails.append(teamUser.getEmail()).append(";");
                }
                if (weekClicks >= 5) {
                    people_click_on_app_five_times++;
                    people_click_on_app_five_times_emails.append(teamUser.getEmail()).append(";");
                }
            }
            if (people_click_on_app_once_emails.length() > 0)
                people_click_on_app_once_emails.deleteCharAt(people_click_on_app_once_emails.length() - 1);
            if (people_click_on_app_three_times_emails.length() > 0)
                people_click_on_app_three_times_emails.deleteCharAt(people_click_on_app_three_times_emails.length() - 1);
            if (people_click_on_app_five_times_emails.length() > 0)
                people_click_on_app_five_times_emails.deleteCharAt(people_click_on_app_five_times_emails.length() - 1);
            teamMetrics.setPeople_click_on_app_once(people_click_on_app_once);
            teamMetrics.setPeople_click_on_app_three_times(people_click_on_app_three_times);
            teamMetrics.setPeople_click_on_app_five_times(people_click_on_app_five_times);
            teamMetrics.setPeople_click_on_app_once_emails(people_click_on_app_once_emails.toString());
            teamMetrics.setPeople_click_on_app_three_times_emails(people_click_on_app_three_times_emails.toString());
            teamMetrics.setPeople_click_on_app_five_times_emails(people_click_on_app_five_times_emails.toString());
            hibernateQuery.saveOrUpdateObject(teamMetrics);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
