package com.Ease.API.V1.Admin;

import com.Ease.Metrics.TeamMetrics;
import com.Ease.Team.Team;
import com.Ease.Utils.Servlets.GetServletManager;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;

@WebServlet("/api/v1/admin/GetAccountChartData")
public class ServletGetAccountChartData extends HttpServlet {
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
            JSONObject rooms = new JSONObject();
            rooms.put("label", "rooms");
            rooms.put("borderColor", "rgba(236, 240, 241, 0.75)"); /*ECF0F1*/
            //rooms.put("backgroundColor", "rgba(236, 240, 241, 0.75)");
            rooms.put("data", new JSONArray());
            JSONObject cards = new JSONObject();
            cards.put("label", "Cards");
            cards.put("borderColor", "rgba(255, 195, 0, 0.75)"); /*FFC300*/
            //cards.put("backgroundColor", "rgba(255, 195, 0, 0.75)");
            cards.put("data", new JSONArray());
            JSONObject cards_with_receiver = new JSONObject();
            cards_with_receiver.put("label", "Cards with tags");
            cards_with_receiver.put("borderColor", "rgba(255, 87, 51, 0.75)"); /*FF5733*/
            //cards_with_receiver.put("backgroundColor", "rgba(255, 87, 51, 0.75)");
            cards_with_receiver.put("data", new JSONArray());
            JSONObject cards_with_password_policy = new JSONObject();
            cards_with_password_policy.put("label", "Cards + PWP");
            cards_with_password_policy.put("borderColor", "rgba(199, 0, 57, 0.75)"); /*C70039*/
            //cards_with_password_policy.put("backgroundColor", "rgba(199, 0, 57, 0.75)");
            cards_with_password_policy.put("data", new JSONArray());
            JSONObject single_cards = new JSONObject();
            single_cards.put("label", "Single cards");
            single_cards.put("borderColor", "rgba(144, 12, 63, 0.75)"); /*900C3F*/
            //single_cards.put("backgroundColor", "rgba(144, 12, 63, 0.75)");
            single_cards.put("data", new JSONArray());
            JSONObject enterprise_cards = new JSONObject();
            enterprise_cards.put("label", "Enterprise cards");
            enterprise_cards.put("borderColor", "rgba(88, 24, 69, 0.75)");
            //enterprise_cards.put("backgroundColor", "rgba(88, 24, 69, 0.75)");
            enterprise_cards.put("data", new JSONArray());
            JSONObject link_cards = new JSONObject();
            link_cards.put("label", "Link cards");
            link_cards.put("borderColor", "rgba(55, 59, 96, 0.75)"); /*373B60*/
            //link_cards.put("backgroundColor", "rgba(55, 59, 96, 0.75)");
            link_cards.put("data", new JSONArray());
            datasets.add(rooms);
            datasets.add(cards);
            datasets.add(cards_with_receiver);
            datasets.add(cards_with_password_policy);
            datasets.add(single_cards);
            datasets.add(enterprise_cards);
            datasets.add(link_cards);
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
                ((JSONArray) rooms.get("data")).add(teamMetrics.getRoom_number());
                ((JSONArray) cards.get("data")).add(teamMetrics.getCards());
                ((JSONArray) cards_with_receiver.get("data")).add(teamMetrics.getCards_with_receiver());
                ((JSONArray) cards_with_password_policy.get("data")).add(teamMetrics.getCards_with_receiver_and_password_policy());
                ((JSONArray) single_cards.get("data")).add(teamMetrics.getSingle_cards());
                ((JSONArray) enterprise_cards.get("data")).add(teamMetrics.getEnterprise_cards());
                ((JSONArray) link_cards.get("data")).add(teamMetrics.getLink_cards());
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
