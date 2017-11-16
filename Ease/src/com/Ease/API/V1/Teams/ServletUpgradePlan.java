package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/v1/teams/UpgradePlan")
public class ServletUpgradePlan extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeOwnerOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            Integer plan_id = sm.getIntParam("plan_id", true, false);
            if (plan_id <= team.getPlan_id())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot downgrade your plan");
            int qte = team.getActiveTeamUserNumber();
            team.getSubscription().cancel(new HashMap<>());
            Map<String, Object> item = new HashMap<>();
            Map<String, Object> params = new HashMap<>();
            switch (plan_id) {
                case 1:
                    item.put("plan", Team.plansMap.get(plan_id));
                    params.put("trial_period_days", 30);
                    params.put("tax_percent", 20.0);
                    break;

                default:
                    throw new HttpServletException(HttpStatus.BadRequest, "No such plan for the moment");
            }
            item.put("quantity", qte);
            Map<String, Object> items = new HashMap<>();
            items.put("0", item);
            params.put("customer", team.getCustomer_id());
            params.put("items", items);
            Subscription subscription = Subscription.create(params);
            team.setSubscription_id(subscription.getId());
            team.setSubscription(subscription);
            sm.saveOrUpdate(team);
            sm.setSuccess(team.getSimpleJson());
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
