package com.Ease.API.V1.Admin.Statistics;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
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
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;

@WebServlet("/api/v1/teams/GetTeamClickChartData")
public class ServletGetTeamClickChartData extends HttpServlet {

    private final static int EASE_FIRST_WEEK = 40;
    private final static int EASE_FIRST_YEAR = 2017;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            JSONObject res = new JSONObject();
            res.put("type", "line");
            int days = 0;
            JSONObject data = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray datasets = new JSONArray();
            JSONObject click_average = new JSONObject();
            click_average.put("label", "Number of clicks");
            click_average.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            click_average.put("backgroundColor", "rgba(55, 59, 96, 0.75)");
            click_average.put("data", new JSONArray());
            datasets.put(click_average);
            Calendar current = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(team.getSubscription_date());
            List<Integer> userIds = team.getUserIds();
            HibernateQuery hibernateQuery = sm.getTrackingHibernateQuery();
            hibernateQuery.querySQLString("SELECT\n" +
                    "  year,\n" +
                    "  day_of_year,\n" +
                    "  COUNT(*) AS clicks\n" +
                    "FROM EASE_EVENT\n" +
                    "WHERE (name LIKE 'PasswordUsed' OR name LIKE 'PasswordUser') AND creation_date BETWEEN :startDate AND :endDate AND user_id IN :userIds\n" +
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
                ((JSONArray) click_average.get("data")).put(count);
                labels.put(++days);
            }
            data.put("labels", labels);
            data.put("datasets", datasets);
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
