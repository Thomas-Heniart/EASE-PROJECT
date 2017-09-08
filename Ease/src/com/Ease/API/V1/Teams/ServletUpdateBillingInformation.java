package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Card;
import com.stripe.model.Customer;
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

@WebServlet("/api/v1/teams/UpdateBillingInformation")
public class ServletUpdateBillingInformation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getUser().getTeamUserForTeam(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            Customer customer = Customer.retrieve(team.getCustomer_id());
            String default_source = customer.getDefaultSource();
            if (default_source == null || default_source.equals(""))
                throw new HttpServletException(HttpStatus.BadRequest, "You must enter a credit card first.");
            Card card = (Card) customer.getSources().retrieve(default_source);
            Map<String, Object> updateParams = new HashMap<>();
            String address_city = sm.getStringParam("address_city", true);
            if (address_city != null && !address_city.equals(""))
                updateParams.put("address_city", address_city);
            String address_country = sm.getStringParam("address_country", true);
            if (address_country != null && !address_country.equals(""))
                updateParams.put("address_country", address_country);
            String address_line1 = sm.getStringParam("address_line1", true);
            if (address_line1 != null && !address_line1.equals(""))
                updateParams.put("address_line1", address_line1);
            String address_line2 = sm.getStringParam("address_line2", true);
            if (address_line2 != null && !address_line2.equals(""))
                updateParams.put("address_line2", address_line2);
            String address_state = sm.getStringParam("address_state", true);
            if (address_state != null && !address_state.equals(""))
                updateParams.put("address_state", address_state);
            String address_zip = sm.getStringParam("address_zip", true);
            if (address_zip != null && !address_zip.equals(""))
                updateParams.put("address_zip", address_zip);
            card.update(updateParams);
            updateParams.clear();
            String business_vat_id = sm.getStringParam("business_vat_id", true);
            if (business_vat_id != null && !business_vat_id.equals(""))
                updateParams.put("business_vat_id", business_vat_id);
            if (!updateParams.isEmpty())
                customer.update(updateParams);
            JSONObject res = new JSONObject();
            res.put("credit", (float) customer.getAccountBalance() / 100);
            String vat_id = customer.getBusinessVatId();
            if (vat_id == null)
                vat_id = "";
            res.put("business_vat_id", vat_id);
            res.put("people_invited", team.invite_people());
            res.put("card", card.toJson());
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
