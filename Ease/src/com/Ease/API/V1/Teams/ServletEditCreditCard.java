package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/EditCreditCard")
public class ServletEditCreditCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeOwnerOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            if (team.getCustomer_id() == null)
                throw new HttpServletException(HttpStatus.BadRequest, "You don't have credit card registered on Ease.space.");
            String token = sm.getStringParam("token", false, false);
            if (token == null || token.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid token.");
            Customer customer = Customer.retrieve(team.getCustomer_id());
            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("source", token);
            customer.update(updateParams);
            sm.setSuccess(team.getJson());
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
