package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/AddCreditCard")
public class ServletAddCreditCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = sm.getUser().getTeamUser(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            String token = sm.getStringParam("token", false, false);
            Customer customer = team.getCustomer();
            String default_source = customer.getDefaultSource();
            if (default_source != null && !default_source.equals(""))
                customer.getSources().retrieve(default_source).delete();
            Map<String, Object> params = new HashMap<>();
            params.put("source", token);
            Card card = (Card) customer.getSources().create(params);
            params.clear();
            customer.setDefaultSource(card.getId());
            params.put("default_source", card.getId());
            customer.update(params);
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(card.toJson());
            team.setCard_entered(true);
            team.setActive(true);
            if (!team.isFreemium()) {
                int qte = team.getActiveTeamUserNumber();
                team.getSubscription().cancel(new HashMap<>());
                Map<String, Object> item = new HashMap<>();
                params.clear();
                item.put("plan", Team.plansMap.get(1));
                params.put("trial_period_days", 30);
                params.put("tax_percent", 20.0);
                item.put("quantity", qte);
                Map<String, Object> items = new HashMap<>();
                items.put("0", item);
                params.put("customer", team.getCustomer_id());
                params.put("items", items);
                Subscription subscription = Subscription.create(params);
                team.setSubscription_id(subscription.getId());
                team.setSubscription(subscription);
                sm.getTeamProperties(team_id).put("subscription", subscription);
            }
            sm.saveOrUpdate(team);
            sm.setSuccess(res);
        } catch (StripeException e) {
            sm.setError(e);
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
