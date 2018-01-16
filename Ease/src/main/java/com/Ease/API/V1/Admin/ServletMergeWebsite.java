package com.Ease.API.V1.Admin;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.NewDashboard.AnyApp;
import com.Ease.NewDashboard.ClassicApp;
import com.Ease.NewDashboard.WebsiteApp;
import com.Ease.Team.TeamCard.TeamWebsiteCard;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            website_to_merge.getWebsiteAppSet().forEach(websiteApp -> {
                if (websiteApp.isAnyApp() && website.getWebsiteAttributes().isIntegrated()) {
                    AnyApp anyApp = (AnyApp) websiteApp;
                    WebsiteApp tmp_app = new ClassicApp(anyApp.getAppInformation(), website, anyApp.getAccount());
                    tmp_app.setProfile(anyApp.getProfile());
                    tmp_app.setPosition(anyApp.getPosition());
                    sm.saveOrUpdate(tmp_app);
                    website.addWebsiteApp(tmp_app);
                    /* @TODO WebSocket message here */
                } else {
                    websiteApp.setWebsite(website);
                    website.addWebsiteApp(websiteApp);
                    sm.saveOrUpdate(websiteApp);
                }
            });
            for (TeamWebsiteCard teamWebsiteCard : website_to_merge.getTeamWebsiteCardSet()) {
                teamWebsiteCard.setWebsite(website);
                website.addTeamWebsiteCard(teamWebsiteCard);
                sm.saveOrUpdate(teamWebsiteCard);
            }
            website_to_merge.getWebsiteAppSet().clear();
            website_to_merge.getTeamWebsiteCardSet().clear();
            website.addWebsiteAlternativeUrl(website_to_merge.getLogin_url(), sm.getHibernateQuery());
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
