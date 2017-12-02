package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.LogWithApp;
import com.Ease.NewDashboard.Profile;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/admin/DeleteWebsite")
public class ServletDeleteWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer website_id = sm.getIntParam("id", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Website website = catalog.getWebsiteWithId(website_id, hibernateQuery);
            for (Team team : website.getTeams())
                team.removeTeamWebsite(website);
            for (App app : website.getWebsiteAppSet()) {
                if (app.isLogWithApp()) {
                    ((LogWithApp) app).setLoginWith_app(null);
                    hibernateQuery.saveOrUpdateObject(app);
                }
            }
            for (TeamWebsiteCard teamWebsiteCard : website.getTeamWebsiteCardSet()) {
                for (TeamCardReceiver teamCardReceiver : teamWebsiteCard.getTeamCardReceiverMap().values()) {
                    Profile profile = teamCardReceiver.getApp().getProfile();
                    if (profile != null)
                        profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), hibernateQuery);
                }
            }
            sm.deleteObject(website);
            sm.setSuccess("Done");
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
