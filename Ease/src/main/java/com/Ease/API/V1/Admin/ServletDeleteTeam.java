package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/api/v1/admin/DeleteTeam")
public class ServletDeleteTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            Subscription subscription = team.getSubscription();
            if (subscription != null) {
                try {
                    subscription.cancel(new HashMap<>());
                    Customer customer = team.getCustomer();
                    String default_source = customer.getDefaultSource();
                    if (default_source != null && !default_source.equals(""))
                        customer.getSources().retrieve(default_source).delete();
                } catch (InvalidRequestException e) {
                    e.printStackTrace();
                }
            }
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            for (TeamCard teamCard : team.getTeamCardSet()) {
                hibernateQuery.queryString("DELETE FROM Update u WHERE u.teamCard.db_id = :card_id");
                hibernateQuery.setParameter("card_id", teamCard.getDb_id());
                hibernateQuery.executeUpdate();
            }
            team.getTeamCardSet().stream().flatMap(teamCard -> teamCard.getTeamCardReceiverMap().values().stream()).forEach(teamCardReceiver -> {
                Profile profile = teamCardReceiver.getApp().getProfile();
                if (profile != null) {
                    profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), hibernateQuery);
                    teamCardReceiver.getApp().setProfile(null);
                    teamCardReceiver.getApp().setPosition(null);
                }
            });
            team.getTeamCardSet().clear();
            team.setSubscription_id(null);
            team.setCard_entered(false);
            team.setActive(false);
            sm.setTeam(team);
            sm.saveOrUpdate(team);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM, WebSocketMessageAction.REMOVED, ws_obj));
            sm.setSuccess("Subscription ended");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        try {
            sm.sendResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
