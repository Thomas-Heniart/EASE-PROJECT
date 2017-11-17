package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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

@WebServlet("/api/v1/teams/GetTeamPaymentInformation")
public class ServletGetTeamPaymentInformation extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = sm.getUser().getTeamUser(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            HashMap<String, Object> cardParams = new HashMap<>();
            cardParams.put("object", "card");
            JSONObject res = new JSONObject();
            Customer customer = Customer.retrieve(team.getCustomer_id());
            res.put("credit", (float) -customer.getAccountBalance() / 100);
            JSONObject card = null;
            String default_source = customer.getDefaultSource();
            if (default_source != null && !default_source.equals("")) {
                ExternalAccount externalAccount = customer.getSources().retrieve(customer.getDefaultSource());
                JSONParser jsonParser = new JSONParser();
                card = (JSONObject) jsonParser.parse(externalAccount.toJson());
            }
            String business_vat_id = customer.getBusinessVatId();
            if (business_vat_id == null)
                business_vat_id = "";
            res.put("business_vat_id", business_vat_id);
            res.put("people_invited", team.invite_people());
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
