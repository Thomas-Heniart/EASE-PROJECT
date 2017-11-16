package com.Ease.API.Utils;

import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Event;
import com.stripe.net.APIResource;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/StripeWebhook")
public class SerlverStripeWebhook extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Event eventJson = APIResource.GSON.fromJson(sm.getBody(), Event.class);
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(eventJson.getData().getObject().toJson());
            Long trialEnd = (Long) jsonObject.get("trial_end");
            if (trialEnd != null) {
                String subscription_id = (String) jsonObject.get("id");
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                for (Team team : teamManager.getTeams(sm.getHibernateQuery())) {
                    if (!team.getSubscription_id().equals(subscription_id))
                        continue;
                    team.getSubscription().setTrialEnd(trialEnd);
                }
            }
            sm.setSuccess("success");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}