package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Catalog.WebsiteFactory;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/EditTeamAnyEnterpriseCard")
public class EditTeamAnyEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) hibernateQuery.get(TeamEnterpriseCard.class, team_card_id);
            if (teamEnterpriseCard == null || teamEnterpriseCard.getWebsite().getWebsiteAttributes().isIntegrated())
                throw new HttpServletException(HttpStatus.BadRequest, "no such teamCard");
            Team team = teamEnterpriseCard.getTeam();
            sm.needToBeAdminOfTeam(team);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamEnterpriseCard.setName(name);
            String description = sm.getStringParam("description", true, false);
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            teamEnterpriseCard.setDescription(description);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            teamEnterpriseCard.setPassword_reminder_interval(password_reminder_interval);
            String url = sm.getStringParam("url", true, false);
            if (!Regex.isSimpleUrl(url) || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            if (!url.equals(teamEnterpriseCard.getWebsite().getLogin_url())) {
                Catalog catalog = (Catalog) sm.getContextAttr("catalog");
                JSONObject connection_information = sm.getJsonParam("connection_information", false, false);
                Website website = catalog.getWebsiteWithUrl(url, connection_information, sm.getHibernateQuery());
                if (website == null) {
                    String img_url = sm.getStringParam("img_url", false, true);
                    website = WebsiteFactory.getInstance().createWebsiteAndLogo(sm.getUser().getEmail(), url, name, img_url, connection_information, sm.getHibernateQuery());
                }
                teamEnterpriseCard.setWebsite(website);
            }
            sm.saveOrUpdate(teamEnterpriseCard);
            sm.setSuccess(teamEnterpriseCard.getJson());
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
