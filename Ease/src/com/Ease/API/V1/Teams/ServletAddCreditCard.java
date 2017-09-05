package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
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
import java.util.Map;

@WebServlet("/api/v1/teams/AddCreditCard")
public class ServletAddCreditCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getUser().getTeamUserForTeam(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            sm.needToBeOwnerOfTeam(team_id);
            String token = sm.getStringParam("token", false);
            Customer customer = Customer.retrieve(team.getCustomer_id());
            String default_source = customer.getDefaultSource();
            if (default_source != null && !default_source.equals(""))
                customer.getSources().retrieve(default_source).delete();
            Map<String, Object> params = new HashMap<>();
            params.put("source", token);
            ExternalAccount externalAccount = customer.getSources().create(params);
            params.clear();
            params.put("default_source", externalAccount.getId());
            customer.update(params);
            JSONParser parser = new JSONParser();
            JSONObject res = (JSONObject) parser.parse(externalAccount.toJson());
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
