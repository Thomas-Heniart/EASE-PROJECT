package com.Ease.API.V1.Admin.upgrade;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Channel;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.User;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/upgrade/es-137")
public class ServletES127 extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT a FROM App a");
            List<App> appList = hibernateQuery.list();
            for (App app : appList) {
                Profile profile = app.getProfile();
                if (profile == null)
                    continue;
                TeamCardReceiver teamCardReceiver = app.getTeamCardReceiver();
                if (teamCardReceiver == null)
                    moveAppInPersoProfileIfNeeded(app, profile, hibernateQuery);
                else
                    moveAppInRoomProfileIfNeeded(app, teamCardReceiver, profile, hibernateQuery);
            }
            sm.setSuccess("Upgraded");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    private void moveAppInRoomProfileIfNeeded(App app, TeamCardReceiver teamCardReceiver, Profile profile, HibernateQuery hibernateQuery) throws HttpServletException {
        Channel channel = teamCardReceiver.getTeamCard().getChannel();
        Channel profileChannel = profile.getChannel();
        if (channel.equals(profileChannel))
            return;
        profile.removeAppAndUpdatePositions(app, hibernateQuery);
        TeamUser teamUser = teamCardReceiver.getTeamUser();
        Profile newProfile = teamUser.getOrCreateProfile(channel, hibernateQuery);
        newProfile.addAppAndUpdatePositions(app, 0, hibernateQuery);
    }

    private void moveAppInPersoProfileIfNeeded(App app, Profile profile, HibernateQuery hibernateQuery) throws HttpServletException {
        Channel channel = profile.getChannel();
        if (channel == null)
            return;
        User user = profile.getUser();
        Profile newProfile = user.getOrCreatePersonalProfile(hibernateQuery);
        profile.removeAppAndUpdatePositions(app, hibernateQuery);
        newProfile.addAppAndUpdatePositions(app, 0, hibernateQuery);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
