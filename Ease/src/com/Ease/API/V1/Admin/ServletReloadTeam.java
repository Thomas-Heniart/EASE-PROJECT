package com.Ease.API.V1.Admin;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/ServletReloadTeam")
public class ServletReloadTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            String team_key = null;
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.getDeciphered_teamKey() == null || teamUser.getDeciphered_teamKey().equals("") || (teamUser.getState() != 3))
                    continue;
                team_key = teamUser.getDeciphered_teamKey();
            }
            team.lazyInitialize();
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            team.getAppManager().setShareableApps(App.loadShareableAppsForTeam(team, sm.getServletContext(), db));
            for (ShareableApp shareableApp : team.getAppManager().getShareableApps()) {
                List<SharedApp> sharedApps = App.loadSharedAppsForShareableApp(shareableApp, team, sm.getServletContext(), db);
                shareableApp.setSharedApps(sharedApps);
                team.getAppManager().setSharedApps(sharedApps);
            }
            db.commitTransaction(transaction);
            for (Channel channel : team.getChannels().values()) {
                if (!channel.getTeamUsers().isEmpty()) {

                }
            }
            sm.setSuccess("team reloaded");
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
