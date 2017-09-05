package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.GetServletManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.ExternalAccount;
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

@WebServlet("/api/v1/teams/GetTeamPayment")
public class ServletGetTeamPayment extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeOwnerOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            HashMap<String, Object> cardParams = new HashMap<>();
            cardParams.put("object", "card");
            JSONObject res = new JSONObject();

            Customer customer = Customer.retrieve(team.getCustomer_id());
            res.put("credit", (float) customer.getAccountBalance() / 100);
            ExternalAccount externalAccount = customer.getSources().retrieve(customer.getDefaultSource());
            JSONParser jsonParser = new JSONParser();
            JSONObject card = (JSONObject) jsonParser.parse(externalAccount.toJson());
            res.put("invite_people", team.invite_people());
            res.put("valid_subscription", !team.isBlocked());
            res.put("card", card);
            sm.setSuccess(res);
        } catch (StripeException e) {
            sm.setError(e);
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
