package com.Ease.API.V1.Admin;

import com.Ease.Metrics.TeamMetrics;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/api/v1/admin/RetrieveTeamsPeopleChartData")
public class ServletRetrieveTeamsPeopleChartData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject res = new JSONObject();
            res.put("type", "line");
            JSONObject data = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray datasets = new JSONArray();
            JSONObject people_invited = new JSONObject();
            people_invited.put("label", "People invited");
            people_invited.put("borderColor", "rgba(236, 240, 241, 0.75)"); /*ECF0F1*/
            people_invited.put("data", new JSONArray());
            JSONObject people_joined = new JSONObject();
            people_joined.put("label", "People joined");
            people_joined.put("borderColor", "rgba(255, 195, 0, 0.75)"); /*FFC300*/
            people_joined.put("data", new JSONArray());
            JSONObject people_with_cards = new JSONObject();
            people_with_cards.put("label", "People with apps");
            people_with_cards.put("borderColor", "rgba(255, 87, 51, 0.75)"); /*FF5733*/
            people_with_cards.put("data", new JSONArray());
            JSONObject people_with_personal_apps = new JSONObject();
            people_with_personal_apps.put("label", "People with personal apps");
            people_with_personal_apps.put("borderColor", "rgba(199, 0, 57, 0.75)"); /*C70039*/
            people_with_personal_apps.put("data", new JSONArray());
            JSONObject people_click_on_app_once = new JSONObject();
            people_click_on_app_once.put("label", "People click 1 day");
            people_click_on_app_once.put("borderColor", "rgba(144, 12, 63, 0.75)"); /*900C3F*/
            people_click_on_app_once.put("data", new JSONArray());
            JSONObject people_click_on_app_three_times = new JSONObject();
            people_click_on_app_three_times.put("label", "People click 3 days");
            people_click_on_app_three_times.put("borderColor", "rgba(88, 24, 69, 0.75)"); /*581845*/
            people_click_on_app_three_times.put("data", new JSONArray());
            JSONObject people_click_on_app_five_times = new JSONObject();
            people_click_on_app_five_times.put("label", "People click 5 days");
            people_click_on_app_five_times.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            people_click_on_app_five_times.put("data", new JSONArray());
            datasets.put(people_invited);
            datasets.put(people_joined);
            datasets.put(people_with_cards);
            datasets.put(people_with_personal_apps);
            datasets.put(people_click_on_app_once);
            datasets.put(people_click_on_app_three_times);
            datasets.put(people_click_on_app_five_times);
            data.put("labels", labels);
            data.put("datasets", datasets);
            res.put("data", data);
            Calendar calendar = Calendar.getInstance();
            int current_year = calendar.get(Calendar.YEAR);
            int current_week = calendar.get(Calendar.WEEK_OF_YEAR);
            JSONArray team_ids = sm.getArrayParam("team_ids", false, false);
            Set<Team> teamSet = new HashSet<>();
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            for (int i=0; i < team_ids.length(); i++)
                teamSet.add(teamManager.getTeam(team_ids.getInt(i), sm.getHibernateQuery()));
            Date start_date = new Date(teamSet.stream().mapToLong(team -> team.getSubscription_date().getTime()).max().getAsLong());
            calendar.setTime(start_date);
            int i = 0;
            while (calendar.get(Calendar.YEAR) < current_year) {
                labels.put(i++);
                double people_invited_avg = 0.;
                double people_with_cards_avg = 0.;
                double people_with_personal_apps_avg = 0.;
                double people_click_on_app_once_avg = 0.;
                double people_click_on_app_three_times_avg = 0.;
                double people_click_on_app_five_times_avg = 0.;
                for (Team team : teamSet) {
                    TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    people_invited_avg += teamMetrics.getPeople_invited();
                    people_with_cards_avg += teamMetrics.getPeople_with_cards();
                    people_with_personal_apps_avg += teamMetrics.getPeople_with_personnal_apps();
                    people_click_on_app_once_avg += teamMetrics.getPeople_click_on_app_once();
                    people_click_on_app_three_times_avg += teamMetrics.getPeople_click_on_app_three_times();
                    people_click_on_app_five_times_avg += teamMetrics.getPeople_click_on_app_five_times();
                }
                ((JSONArray) people_invited.get("data")).put(people_invited_avg / teamSet.size());
                ((JSONArray) people_with_cards.get("data")).put(people_with_cards_avg / teamSet.size());
                ((JSONArray) people_with_personal_apps.get("data")).put(people_with_personal_apps_avg / teamSet.size());
                ((JSONArray) people_click_on_app_once.get("data")).put(people_click_on_app_once_avg / teamSet.size());
                ((JSONArray) people_click_on_app_three_times.get("data")).put(people_click_on_app_three_times_avg / teamSet.size());
                ((JSONArray) people_click_on_app_five_times.get("data")).put(people_click_on_app_five_times_avg / teamSet.size());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (calendar.get(Calendar.WEEK_OF_YEAR) <= current_week) {
                labels.put(i++);
                double people_invited_avg = 0.;
                double people_with_cards_avg = 0.;
                double people_with_personal_apps_avg = 0.;
                double people_click_on_app_once_avg = 0.;
                double people_click_on_app_three_times_avg = 0.;
                double people_click_on_app_five_times_avg = 0.;
                for (Team team : teamSet) {
                    TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    people_invited_avg += teamMetrics.getPeople_invited();
                    people_with_cards_avg += teamMetrics.getPeople_with_cards();
                    people_with_personal_apps_avg += teamMetrics.getPeople_with_personnal_apps();
                    people_click_on_app_once_avg += teamMetrics.getPeople_click_on_app_once();
                    people_click_on_app_three_times_avg += teamMetrics.getPeople_click_on_app_three_times();
                    people_click_on_app_five_times_avg += teamMetrics.getPeople_click_on_app_five_times();
                }
                ((JSONArray) people_invited.get("data")).put(people_invited_avg / teamSet.size());
                ((JSONArray) people_with_cards.get("data")).put(people_with_cards_avg / teamSet.size());
                ((JSONArray) people_with_personal_apps.get("data")).put(people_with_personal_apps_avg / teamSet.size());
                ((JSONArray) people_click_on_app_once.get("data")).put(people_click_on_app_once_avg / teamSet.size());
                ((JSONArray) people_click_on_app_three_times.get("data")).put(people_click_on_app_three_times_avg / teamSet.size());
                ((JSONArray) people_click_on_app_five_times.get("data")).put(people_click_on_app_five_times_avg / teamSet.size());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
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
