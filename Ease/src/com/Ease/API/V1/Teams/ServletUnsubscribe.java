package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@WebServlet("/api/v1/teams/Unsubscribe")
public class ServletUnsubscribe extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = sm.getUser().getTeamUser(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            String password = sm.getStringParam("password", false, false);
            if (!sm.getUser().getUserKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password.");
            Subscription subscription = team.getSubscription();
            subscription.cancel(new HashMap<>());
            Customer customer = team.getCustomer();
            String default_source = customer.getDefaultSource();
            if (default_source != null && !default_source.equals(""))
                customer.getSources().retrieve(default_source).delete();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            team.getTeamCardMap().values().stream().flatMap(teamCard -> teamCard.getTeamCardReceiverMap().values().stream()).forEach(teamCardReceiver -> {
                Profile profile = teamCardReceiver.getApp().getProfile();
                if (profile != null) {
                    profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getUserWebSocketManager(profile.getUser().getDb_id()), hibernateQuery);
                    teamCardReceiver.getApp().setProfile(null);
                    teamCardReceiver.getApp().setPosition(null);
                }
            });
            team.getTeamCardMap().clear();
            team.setSubscription_id(null);
            team.setCard_entered(false);
            team.setActive(false);
            sm.saveOrUpdate(team);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM, WebSocketMessageAction.REMOVED, ws_obj));
            sm.setSuccess("Subscription ended");
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
