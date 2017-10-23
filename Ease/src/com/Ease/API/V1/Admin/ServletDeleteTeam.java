package com.Ease.API.V1.Admin;

import com.Ease.Dashboard.App.ShareableApp;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/api/v1/admin/DeleteTeam")
public class ServletDeleteTeam extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer team_id = sm.getIntParam("team_id", true, false);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team;
            try {
                team = teamManager.getTeamWithId(team_id);
                List<ShareableApp> shareableAppList = new LinkedList<>();
                shareableAppList.addAll(team.getAppManager().getShareableApps().values());
                DataBaseConnection db = sm.getDB();
                int transaction = db.startTransaction();
                for (ShareableApp shareableApp : shareableAppList)
                    team.getAppManager().removeShareableApp(shareableApp, db);
                db.commitTransaction(transaction);
                teamManager.removeTeamWithId(team_id);
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
                DataBaseConnection db = sm.getDB();
                int transaction = db.startTransaction();
                for (ShareableApp shareableApp : team.getAppManager().getShareableApps().values())
                    shareableApp.deleteShareable(db);
                db.commitTransaction(transaction);
                for (TeamUser teamUser : team.getTeamUsers().values())
                    sm.deleteObject(teamUser);
                for (Channel channel : team.getChannels().values())
                    sm.deleteObject(channel);
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
