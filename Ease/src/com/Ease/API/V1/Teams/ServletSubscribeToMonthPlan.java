package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Coupon;
import com.stripe.model.Customer;
import com.stripe.model.Plan;
import com.stripe.model.Subscription;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/SubscribeToMonthPlan")
public class ServletSubscribeToMonthPlan extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            sm.needToBeOwnerOfTeam(team);
            if (team.getSubscription_id() != null)
                throw new HttpServletException(HttpStatus.BadRequest, "You already subscribed to Ease Space");
            if (team.getCustomer_id() == null) {
                Map<String, Object> customerParams = new HashMap<>();
                customerParams.put("email", sm.getTeamUserForTeam(team).getEmail());
                customerParams.put("source", sm.getStringParam("token", true));
                Customer customer = Customer.create(customerParams);
                team.setCustomer_id(customer.getId());
            }
            Map<String, Object> subscriptionParams = new HashMap<>();
            subscriptionParams.put("plan", "EasePremium");
            subscriptionParams.put("quantity", 1);
            team.increaseAccountBalance(10000);
            subscriptionParams.put("customer", team.getCustomer_id());
            Subscription subscription = Subscription.create(subscriptionParams);
            team.setSubscription_id(subscription.getId());
            Calendar calendar = Calendar.getInstance();
            team.setFirst_payment_date(calendar.getTime());
            calendar.add(Calendar.MONTH, 1);
            team.setNext_payment_date(calendar.getTime());
            sm.saveOrUpdate(team);
            sm.setSuccess("Subscription done");
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
