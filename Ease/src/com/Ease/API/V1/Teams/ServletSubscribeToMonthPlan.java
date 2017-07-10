package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.*;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
                String email = sm.getTeamUserForTeam(team).getEmail();
                Map<String, Object> customerParams = new HashMap<>();
                customerParams.put("email", email);
                String token = sm.getStringParam("token", false);
                if (token == null || token.equals(""))
                    throw new HttpServletException(HttpStatus.BadRequest, "Invalid token.");
                String vat_id = sm.getStringParam("vat_id", true);
                JSONObject customer_metadata = new JSONObject();
                customer_metadata.put("business_type", "B2C");
                if (vat_id != null) {
                    customer_metadata.put("business_type", "B2B");
                    customer_metadata.put("business_vat_id", vat_id);
                }
                customerParams.put("metadata", customer_metadata);
                HibernateQuery hibernateQuery = sm.getHibernateQuery();
                hibernateQuery.querySQLString("SELECT credit FROM waitingCredits WHERE email = ?");
                hibernateQuery.setParameter(1, email);
                Integer amount = (Integer) hibernateQuery.getSingleResult();
                if (amount != null) {
                    customerParams.put("account_balance", -amount);
                    hibernateQuery.querySQLString("DELETE FROM waitingCredits WHERE email = ?");
                    hibernateQuery.setParameter(1, email);
                    hibernateQuery.executeUpdate();
                }
                Customer customer = Customer.create(customerParams);
                Map<String, Object> source = new JSONObject();
                source.put("source", token);
                Card customer_card = (Card) customer.getSources().create(source);
                Map<String, Object> card_information = new JSONObject();
                card_information.put("name", sm.getStringParam("name", true));
                card_information.put("address_line1", sm.getStringParam("address_line1", false));
                card_information.put("address_line2", sm.getStringParam("address_line2", false));
                card_information.put("address_zip", sm.getStringParam("address_zip", false));
                card_information.put("address_state", sm.getStringParam("address_state", false));
                card_information.put("address_country", sm.getStringParam("address_country", false));
                card_information.put("address_city", sm.getStringParam("address_city", false));
                validateCardInformation(card_information);
                customer_card.update(card_information);
                team.setCustomer_id(customer.getId());
            }
            Map<String, Object> subscriptionParams = new HashMap<>();
            subscriptionParams.put("plan", "EasePremium");
            subscriptionParams.put("quantity", 1);
            subscriptionParams.put("customer", team.getCustomer_id());
            subscriptionParams.put("tax_percent", 20.0);
            Subscription subscription = Subscription.create(subscriptionParams);
            team.setSubscription_id(subscription.getId());
            sm.saveOrUpdate(team);
            sm.setSuccess("Subscription done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void validateCardInformation(Map<String, Object> customerParams) throws HttpServletException {
        if (customerParams.get("name") == null || customerParams.get("name").equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "You must provide a name");
        if (customerParams.get("address_line1") == null || customerParams.get("address_line1").equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid address");
        if (customerParams.get("address_line2") == null || customerParams.get("address_line2").equals(""))
            customerParams.put("address_line2", null);
        if (customerParams.get("address_zip") == null || customerParams.get("address_zip").equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid address");
        if (customerParams.get("address_state") == null || customerParams.get("address_state").equals(""))
            customerParams.put("address_state", null);
        if (customerParams.get("address_country") == null || customerParams.get("address_country").equals(""))
            throw new HttpServletException(HttpStatus.BadRequest, "You must provide a valid address");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
