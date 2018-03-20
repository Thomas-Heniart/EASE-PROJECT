package com.Ease.API.V1.Admin.Statistics;

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

@WebServlet("/api/v1/admin/RetrieveTeamsAccountChartData")
public class ServletRetrieveTeamsAccountChartData extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            JSONObject res = new JSONObject();
            res.put("type", "line");
            JSONObject data = new JSONObject();
            JSONArray labels = new JSONArray();
            JSONArray datasets = new JSONArray();
            JSONObject cards = new JSONObject();
            cards.put("label", "Cards");
            cards.put("borderColor", "rgba(255, 195, 0, 0.75)"); /*FFC300*/
            cards.put("data", new JSONArray());
            JSONObject cards_with_receiver = new JSONObject();
            cards_with_receiver.put("label", "Cards with tags");
            cards_with_receiver.put("borderColor", "rgba(255, 87, 51, 0.75)"); /*FF5733*/
            cards_with_receiver.put("data", new JSONArray());
            JSONObject cards_with_password_policy = new JSONObject();
            cards_with_password_policy.put("label", "Cards + PWP");
            cards_with_password_policy.put("borderColor", "rgba(199, 0, 57, 0.75)"); /*C70039*/
            cards_with_password_policy.put("data", new JSONArray());
            JSONObject single_cards = new JSONObject();
            single_cards.put("label", "Single cards");
            single_cards.put("borderColor", "rgba(144, 12, 63, 0.75)"); /*900C3F*/
            single_cards.put("data", new JSONArray());
            JSONObject enterprise_cards = new JSONObject();
            enterprise_cards.put("label", "Enterprise cards");
            enterprise_cards.put("borderColor", "rgba(88, 24, 69, 0.75)");
            enterprise_cards.put("data", new JSONArray());
            JSONObject link_cards = new JSONObject();
            link_cards.put("label", "Link cards");
            link_cards.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            link_cards.put("data", new JSONArray());
            datasets.put(cards);
            datasets.put(cards_with_receiver);
            datasets.put(cards_with_password_policy);
            datasets.put(single_cards);
            datasets.put(enterprise_cards);
            datasets.put(link_cards);
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
            Date start_date = new Date(teamSet.stream().mapToLong(team -> team.getSubscription_date().getTime()).min().getAsLong());
            calendar.setTime(start_date);
            int i = 0;
            while (calendar.get(Calendar.YEAR) < current_year) {
                labels.put(i++);
                double cards_avg = 0.;
                double cards_with_receiver_avg = 0.;
                double cards_with_password_policy_avg = 0.;
                double single_cards_avg = 0.;
                double enterprise_cards_avg = 0.;
                double link_cards_avg = 0.;
                for (Team team : teamSet) {
                    TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    cards_avg += teamMetrics.getCards();
                    cards_with_receiver_avg += teamMetrics.getCards_with_receiver();
                    cards_with_password_policy_avg += teamMetrics.getCards_with_receiver_and_password_policy();
                    single_cards_avg += teamMetrics.getSingle_cards();
                    enterprise_cards_avg += teamMetrics.getEnterprise_cards();
                    link_cards_avg += teamMetrics.getLink_cards();
                }
                ((JSONArray) cards.get("data")).put(cards_avg / teamSet.size());
                ((JSONArray) cards_with_receiver.get("data")).put(cards_with_receiver_avg / teamSet.size());
                ((JSONArray) cards_with_password_policy.get("data")).put(cards_with_password_policy_avg / teamSet.size());
                ((JSONArray) single_cards.get("data")).put(single_cards_avg / teamSet.size());
                ((JSONArray) enterprise_cards.get("data")).put(enterprise_cards_avg / teamSet.size());
                ((JSONArray) link_cards.get("data")).put(link_cards_avg / teamSet.size());
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
            }
            while (calendar.get(Calendar.WEEK_OF_YEAR) <= current_week) {
                labels.put(i++);
                double cards_avg = 0.;
                double cards_with_receiver_avg = 0.;
                double cards_with_password_policy_avg = 0.;
                double single_cards_avg = 0.;
                double enterprise_cards_avg = 0.;
                double link_cards_avg = 0.;
                for (Team team : teamSet) {
                    TeamMetrics teamMetrics = TeamMetrics.getMetrics(team.getDb_id(), calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR), sm.getHibernateQuery());
                    cards_avg += teamMetrics.getCards();
                    cards_with_receiver_avg += teamMetrics.getCards_with_receiver();
                    cards_with_password_policy_avg += teamMetrics.getCards_with_receiver_and_password_policy();
                    single_cards_avg += teamMetrics.getSingle_cards();
                    enterprise_cards_avg += teamMetrics.getEnterprise_cards();
                    link_cards_avg += teamMetrics.getLink_cards();
                }
                ((JSONArray) cards.get("data")).put(cards_avg / teamSet.size());
                ((JSONArray) cards_with_receiver.get("data")).put(cards_with_receiver_avg / teamSet.size());
                ((JSONArray) cards_with_password_policy.get("data")).put(cards_with_password_policy_avg / teamSet.size());
                ((JSONArray) single_cards.get("data")).put(single_cards_avg / teamSet.size());
                ((JSONArray) enterprise_cards.get("data")).put(enterprise_cards_avg / teamSet.size());
                ((JSONArray) link_cards.get("data")).put(link_cards_avg / teamSet.size());
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
