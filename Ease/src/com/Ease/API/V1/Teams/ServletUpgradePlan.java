package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Plan;
import com.stripe.model.SubscriptionItem;

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
            Team team = teamManager.getTeamWithId(team_id);
            SubscriptionItem subscriptionItem = team.getSubscription().getSubscriptionItems().getData().get(0);
            Map<String, Object> updateParams = new HashMap<>();
            updateParams.put("plan", "EaseFreemium");
            subscriptionItem.update(updateParams);
            team.getSubscription().setPlan(Plan.retrieve("EaseFreemium"));
            sm.setSuccess(team.getJson());
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
