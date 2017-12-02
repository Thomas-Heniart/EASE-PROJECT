package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;

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
            Team team;
            try {
                team = sm.getTeam(team_id);
                for (TeamUser teamUser : team.getTeamUsers().values()) {
                    teamUser.setAdmin_id(null);
                    sm.saveOrUpdate(teamUser);
                }
                sm.deleteObject(team);
                sm.setSuccess("Team deleted");
            } catch (HttpServletException e) {
                HibernateQuery hibernateQuery = sm.getHibernateQuery();
                hibernateQuery.querySQLString("SELECT * FROM teams WHERE id = ?");
                hibernateQuery.setParameter(1, team_id);
                if (hibernateQuery.getSingleResult() == null)
                    throw new HttpServletException(HttpStatus.BadRequest, "No team with this id");
                hibernateQuery.querySQLString("UPDATE teamUsers SET admin_id = null WHERE team_id = ?");
                hibernateQuery.setParameter(1, team_id);
                hibernateQuery.executeUpdate();
                hibernateQuery.queryString("SELECT t FROM Team t WHERE t.id = ?");
                hibernateQuery.setParameter(1, team_id);
                team = (Team) hibernateQuery.getSingleResult();
                Subscription subscription = team.getSubscription();
                subscription.cancel(new HashMap<>());
                Customer customer = team.getCustomer();
                String default_source = customer.getDefaultSource();
                if (default_source != null && !default_source.equals(""))
                    customer.getSources().retrieve(default_source).delete();
                team.getTeamCardMap().values().stream().flatMap(teamCard -> teamCard.getTeamCardReceiverMap().values().stream()).forEach(teamCardReceiver -> {
                    Profile profile = teamCardReceiver.getApp().getProfile();
                    if (profile != null) {
                        profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), hibernateQuery);
                        teamCardReceiver.getApp().setProfile(null);
                        teamCardReceiver.getApp().setPosition(null);
                    }
                });
                team.getTeamCardMap().clear();
                team.setSubscription_id(null);
                team.setCard_entered(false);
                team.setActive(false);
                sm.saveOrUpdate(team);
                for (TeamUser teamUser : team.getTeamUsers().values())
                    sm.deleteObject(teamUser);
                for (Channel channel : team.getChannels().values())
                    sm.deleteObject(channel);
                sm.deleteObject(team);
                sm.setSuccess("team deleted");
            }
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
