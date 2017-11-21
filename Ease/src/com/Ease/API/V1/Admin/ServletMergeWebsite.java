package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/api/v1/admin/MergeWebsite")
public class ServletMergeWebsite extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            Integer id = Integer.parseInt(sm.getStringParam("id", true, false));
            Integer id_to_merge = sm.getIntParam("id_to_merge", true, false);
            Catalog catalog = (Catalog) sm.getContextAttr("catalog");
            Website website = catalog.getWebsiteWithId(id, sm.getHibernateQuery());
            Website website_to_merge = catalog.getWebsiteWithId(id_to_merge, sm.getHibernateQuery());
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT app FROM WebsiteApp app WHERE app.website = :website");
            hibernateQuery.setParameter("website", website_to_merge);
            List<WebsiteApp> websiteApps = hibernateQuery.list();
            for (WebsiteApp websiteApp : websiteApps) {
                websiteApp.setWebsite(website);
                sm.saveOrUpdate(websiteApp);
            }
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            List<WebSocketMessage> webSocketMessageList = new LinkedList<>();
            for (Team team : teamManager.getTeams(sm.getHibernateQuery())) {
                System.out.println(webSocketMessageList.size() + " messages send");
                sm.getTeamWebSocketManager(team.getDb_id()).sendObjects(webSocketMessageList);
                webSocketMessageList.clear();
            }
            sm.deleteObject(website_to_merge);
            sm.setSuccess("Websites merged");
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
