package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.stripe.exception.StripeException;
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

@WebServlet("/api/v1/teams/Unsubscribe")
public class ServletUnsubscribe extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getUser().getTeamUserForTeam(team);
            if (!teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You must be owner of the team.");
            String password = sm.getStringParam("password", false, false);
            if (!sm.getUser().getKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password.");

            /* ==== Stripe start ==== */

            Subscription subscription = team.getSubscription();
            subscription.cancel(new HashMap<>());
            Customer customer = Customer.retrieve(team.getCustomer_id());
            String default_source = customer.getDefaultSource();
            if (default_source != null && !default_source.equals(""))
                customer.getSources().retrieve(default_source).delete();

            /* ===== Stripe end ===== */
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            for (TeamUser teamUser1 : team.getTeamUsers().values()) {
                if (teamUser1.getDashboard_user() != null)
                    teamUser1.getDashboard_user().getTeamUsers().remove(teamUser1);
                for (SharedApp sharedApp : teamUser1.getSharedApps()) {
                    App app = (App) sharedApp;
                    if (app.isPinned())
                        app.unpin(db);
                    sharedApp.setDisableShared(true, db);
                }
            }
            db.commitTransaction(transaction);
            team.setSubscription_id(null);
            team.setCard_entered(false);
            team.setActive(false);
            sm.saveOrUpdate(team);
            teamManager.removeTeamWithId(team_id);
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
