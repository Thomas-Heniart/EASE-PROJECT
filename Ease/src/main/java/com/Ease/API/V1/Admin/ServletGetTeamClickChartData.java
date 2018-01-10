package com.Ease.API.V1.Admin;

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
import java.util.Calendar;

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
            click_average.put("label", "Average of clicks");
            click_average.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            click_average.put("backgroundColor", "rgba(55, 59, 96, 0.75)");
            click_average.put("data", new JSONArray());
            datasets.put(click_average);
            Calendar current = Calendar.getInstance();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(team.getSubscription_date());
            while (calendar.get(Calendar.YEAR) < current.get(Calendar.YEAR)) {
                JSONArray tmp = team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                for (int i=0; i < tmp.length(); i++)
                    ((JSONArray) click_average.get("data")).put(tmp.opt(i));
                for (int i = 1; i <= 7; i++)
                    labels.put(++days);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (calendar.get(Calendar.WEEK_OF_YEAR) <= current.get(Calendar.WEEK_OF_YEAR)) {
                JSONArray tmp = team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                for (int i=0; i < tmp.length(); i++)
                    ((JSONArray) click_average.get("data")).put(tmp.opt(i));
                for (int i = 1; i <= 7; i++)
                    labels.put(++days);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            /* if (calendar.get(Calendar.YEAR) > EASE_FIRST_YEAR) {
                do {
                    ((JSONArray) click_average.get("data")).put(team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery()));
                    for (int i = 1; i <= 7; i++)
                        labels.put(++days);
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                } while (calendar.get(Calendar.YEAR) < current.get(Calendar.YEAR));
            }
            do {
                ((JSONArray) click_average.get("data")).put(team.getAverageOfClick(calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery()));
                for (int i = 1; i <= 7; i++)
                    labels.put(++days);
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            } while (calendar.get(Calendar.WEEK_OF_YEAR) <= Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)); */
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
