package com.Ease.API.V1.Admin;

import com.Ease.Metrics.TeamMetrics;
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

@WebServlet("/api/v1/admin/GetPeopleChartData")
public class ServletGetPeopleChartData extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            JSONObject res = new JSONObject();
            res.put("type", "line");
            JSONObject data = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray datasets = new JSONArray();
            JSONObject people_invited = new JSONObject();
            people_invited.put("label", "People invited");
            people_invited.put("borderColor", "rgba(236, 240, 241, 0.75)"); /*ECF0F1*/
            //people_invited.put("backgroundColor", "rgba(236, 240, 241, 0.75)");
            people_invited.put("data", new JSONArray());
            JSONObject people_joined = new JSONObject();
            people_joined.put("label", "People joined");
            people_joined.put("borderColor", "rgba(255, 195, 0, 0.75)"); /*FFC300*/
            //people_joined.put("backgroundColor", "rgba(255, 195, 0, 0.75)");
            people_joined.put("data", new JSONArray());
            JSONObject people_with_cards = new JSONObject();
            people_with_cards.put("label", "People with apps");
            people_with_cards.put("borderColor", "rgba(255, 87, 51, 0.75)"); /*FF5733*/
            //people_with_cards.put("backgroundColor", "rgba(255, 87, 51, 0.75)");
            people_with_cards.put("data", new JSONArray());
            JSONObject people_with_personal_apps = new JSONObject();
            people_with_personal_apps.put("label", "People with personal apps");
            people_with_personal_apps.put("borderColor", "rgba(199, 0, 57, 0.75)"); /*C70039*/
            //people_with_personal_apps.put("backgroundColor", "rgba(199, 0, 57, 0.75)");
            people_with_personal_apps.put("data", new JSONArray());
            JSONObject people_click_on_app_once = new JSONObject();
            people_click_on_app_once.put("label", "People click 1 day");
            people_click_on_app_once.put("borderColor", "rgba(144, 12, 63, 0.75)"); /*900C3F*/
            //people_click_on_app_once.put("backgroundColor", "rgba(144, 12, 63, 0.75)");
            people_click_on_app_once.put("data", new JSONArray());
            JSONObject people_click_on_app_three_times = new JSONObject();
            people_click_on_app_three_times.put("label", "People click 3 days");
            people_click_on_app_three_times.put("borderColor", "rgba(88, 24, 69, 0.75)"); /*581845*/
            //people_click_on_app_three_times.put("backgroundColor", "rgba(88, 24, 69, 0.75)");
            people_click_on_app_three_times.put("data", new JSONArray());
            JSONObject people_click_on_app_five_times = new JSONObject();
            people_click_on_app_five_times.put("label", "People click 5 days");
            people_click_on_app_five_times.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            //people_click_on_app_five_times.put("backgroundColor", "rgba(55, 59, 96, 0.75)");
            people_click_on_app_five_times.put("data", new JSONArray());
            datasets.add(people_invited);
            datasets.add(people_joined);
            datasets.add(people_with_cards);
            datasets.add(people_with_personal_apps);
            datasets.add(people_click_on_app_once);
            datasets.add(people_click_on_app_three_times);
            datasets.add(people_click_on_app_five_times);
            data.put("labels", labels);
            data.put("datasets", datasets);
            res.put("data", data);
            Calendar calendar = Calendar.getInstance();
            int current_year = calendar.get(Calendar.YEAR);
            int current_week = calendar.get(Calendar.WEEK_OF_YEAR);
            calendar.setTime(team.getSubscription_date());
            int i = 0;
            while (calendar.get(Calendar.YEAR) <= current_year && calendar.get(Calendar.WEEK_OF_YEAR) <= current_week) {
                labels.add(i++);
                TeamMetrics teamMetrics = TeamMetrics.getMetrics(team_id, calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                ((JSONArray) people_invited.get("data")).add(teamMetrics.getPeople_invited());
                ((JSONArray) people_joined.get("data")).add(teamMetrics.getPeople_joined());
                ((JSONArray) people_with_cards.get("data")).add(teamMetrics.getPeople_with_cards());
                ((JSONArray) people_with_personal_apps.get("data")).add(teamMetrics.getPeople_with_personnal_apps());
                ((JSONArray) people_click_on_app_once.get("data")).add(teamMetrics.getPeople_click_on_app_once());
                ((JSONArray) people_click_on_app_three_times.get("data")).add(teamMetrics.getPeople_click_on_app_three_times());
                ((JSONArray) people_click_on_app_five_times.get("data")).add(teamMetrics.getPeople_click_on_app_five_times());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
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
