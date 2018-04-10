package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@WebServlet("/api/v1/admin/RetrieveTeamsClickChartData")
public class ServletRetrieveTeamsClickChartData extends HttpServlet {

    private final static int EASE_FIRST_WEEK = 40;
    private final static int EASE_FIRST_YEAR = 2017;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject res = new JSONObject();
            res.put("type", "line");
            int days = 0;
            JSONObject data = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray datasets = new JSONArray();
            JSONObject click_average = new JSONObject();
            click_average.put("label", "Average of clicks");
            click_average.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            click_average.put("backgroundColor", "rgba(55, 59, 96, 0.75)");
            click_average.put("data", new JSONArray());
            datasets.put(click_average);
            Calendar current = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            JSONArray team_ids = sm.getArrayParam("team_ids", false, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Set<Team> teamSet = new HashSet<>();
            for (int i = 0; i < team_ids.length(); i++)
                teamSet.add(teamManager.getTeam(team_ids.getInt(i), sm.getHibernateQuery()));
            Set<Integer> userIds = new HashSet<>();
            teamSet.forEach(team -> userIds.addAll(team.getUserIds()));
            Date start_date = new Date(teamSet.stream().mapToLong(team -> team.getSubscription_date().getTime()).min().getAsLong());
            calendar.setTime(start_date);
            HibernateQuery hibernateQuery = sm.getTrackingHibernateQuery();
            hibernateQuery.querySQLString("SELECT\n" +
                    "  year,\n" +
                    "  day_of_year,\n" +
                    "  COUNT(*) AS clicks\n" +
                    "FROM EASE_EVENT\n" +
                    "WHERE name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser' AND creation_date BETWEEN :startDate AND :endDate AND user_id IN :userIds\n" +
                    "GROUP BY year, day_of_year\n" +
                    "ORDER BY YEAR, day_of_year;");
            hibernateQuery.setDate("startDate", calendar);
            hibernateQuery.setDate("endDate", current);
            hibernateQuery.setParameter("userIds", userIds);
            List<Object> raws = hibernateQuery.list();
            for (Object object : raws) {
                Object[] raw = (Object[]) object;
                Long count = ((BigInteger) raw[2]).longValueExact();
                int year = (Integer) raw[0];
                int dayOfYear = ((Short) raw[1]).intValue();
                while (calendar.get(Calendar.YEAR) < year || (calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.DAY_OF_YEAR) < dayOfYear)) {
                    ((JSONArray) click_average.get("data")).put(0);
                    labels.put(++days);
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                }
                ((JSONArray) click_average.get("data")).put(count.doubleValue() / userIds.size());
            }
            /* while (calendar.get(Calendar.YEAR) < current.get(Calendar.YEAR)) {
                double avgs[] = {0., 0., 0., 0., 0., 0., 0.};
                for (Team team : teamSet) {
                    JSONArray tmp = team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    for (int i = 0; i < tmp.length(); i++)
                        avgs[i] += tmp.getDouble(i);
                }
                for (double avg : avgs) ((JSONArray) click_average.get("data")).put(avg / teamSet.size());
                for (int i = 1; i <= 7; i++)
                    labels.put(++days);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (calendar.get(Calendar.WEEK_OF_YEAR) <= current.get(Calendar.WEEK_OF_YEAR)) {
                double avgs[] = {0., 0., 0., 0., 0., 0., 0.};
                for (Team team : teamSet) {
                    JSONArray tmp = team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    for (int i = 0; i < tmp.length(); i++)
                        avgs[i] += tmp.getDouble(i);
                }
                for (double avg : avgs) ((JSONArray) click_average.get("data")).put(avg / teamSet.size());
                for (int i = 1; i <= 7; i++)
                    labels.put(++days);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            } */
            data.put("labels", labels);
            data.put("datasets", datasets);
            res.put("data", data);
            sm.setSuccess(res);
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
