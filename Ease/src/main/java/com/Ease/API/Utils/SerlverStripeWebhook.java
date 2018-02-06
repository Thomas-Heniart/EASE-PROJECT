package com.Ease.API.Utils;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.net.APIResource;
import org.json.JSONObject;

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
            String type = new JSONObject(eventJson.toJson()).getString("type");
            JSONObject jsonObject = new JSONObject(eventJson.getData().getObject().toJson());
            System.out.println(eventJson.getData().toJson());
            System.out.println(jsonObject.toString());
            //String type = jsonObject.getString("type");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            JSONObject data = null;
            String subscription_id = null;
            Team team = null;
            switch (type) {
                case "invoice.payment_succeeded":
                    data = jsonObject.getJSONObject("lines").getJSONArray("data").getJSONObject(0);
                    subscription_id = data.getString("id");
                    hibernateQuery.queryString("SELECT t FROM Team t WHERE subscription_id = :sub_id");
                    hibernateQuery.setParameter("sub_id", subscription_id);
                    team = (Team) hibernateQuery.getSingleResult();
                    if (team != null) {
                        Subscription subscription = Subscription.retrieve(subscription_id);
                        team.setSubscription(subscription);
                        sm.getTeamProperties(team.getDb_id()).put("subscription", subscription);
                        MailJetBuilder mailJetBuilder = new MailJetBuilder();
                        mailJetBuilder.setTemplateId(308436);
                        mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                        mailJetBuilder.addTo("thomas@ease.space");
                        mailJetBuilder.addTo("benjamin@ease.space");
                        mailJetBuilder.addVariable("sub_id", subscription_id);
                        mailJetBuilder.addVariable("state", "success");
                        mailJetBuilder.sendEmail();
                    }
                    break;
                case "invoice.payment_failed":
                    data = jsonObject.getJSONObject("lines").getJSONArray("data").getJSONObject(0);
                    subscription_id = data.getString("id");
                    hibernateQuery.queryString("SELECT t FROM Team t WHERE subscription_id = :sub_id");
                    hibernateQuery.setParameter("sub_id", subscription_id);
                    team = (Team) hibernateQuery.getSingleResult();
                    if (team != null) {
                        Subscription subscription = Subscription.retrieve(subscription_id);
                        team.setSubscription(subscription);
                        sm.getTeamProperties(team.getDb_id()).put("subscription", subscription);
                        MailJetBuilder mailJetBuilder = new MailJetBuilder();
                        mailJetBuilder.setTemplateId(308436);
                        mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                        mailJetBuilder.addTo("thomas@ease.space");
                        mailJetBuilder.addTo("benjamin@ease.space");
                        mailJetBuilder.addVariable("sub_id", subscription_id);
                        mailJetBuilder.addVariable("state", "false");
                        mailJetBuilder.sendEmail();
                    }
                    break;
                case "customer.subscription.updated":
                    break;
                default:
                    break;
            }
            /* if (jsonObject.has("trial_end")) {
                Long trialEnd = jsonObject.getLong("trial_end");
                String subscription_id = jsonObject.getString("id");
                TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
                for (Team team : teamManager.getTeams(sm.getHibernateQuery())) {
                    if (team.getSubscription_id() == null || !team.getSubscription_id().equals(subscription_id))
                        continue;
                    sm.initializeTeamWithContext(team);
                    team.getSubscription().setTrialEnd(trialEnd);
                }
            } */
            sm.setSuccess("success");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }
}