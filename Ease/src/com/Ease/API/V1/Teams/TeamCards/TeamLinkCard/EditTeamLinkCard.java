package com.Ease.API.V1.Teams.TeamCards.TeamLinkCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.LinkApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamLinkCardReceiver;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/EditTeamLinkCard")
public class EditTeamLinkCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamCard teamCard = (TeamCard) hibernateQuery.get(TeamLinkCard.class, team_card_id);
            if (teamCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such team link card");
            Team team = teamCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeAdminOfTeam(team);
            if (!teamCard.isTeamLinkCard())
                throw new HttpServletException(HttpStatus.Forbidden, "You can only edit a team link card");
            TeamLinkCard teamLinkCard = (TeamLinkCard) teamCard;
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamLinkCard.setName(name);
            String url = sm.getStringParam("url", true, false);
            if (url.equals("") || url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter url");
            String img_url = sm.getStringParam("img_url", true, false);
            if (img_url.equals("") || img_url.length() > 2000)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter img_url");
            String description = sm.getStringParam("description", true, true);
            if (description != null && description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            teamLinkCard.setDescription(description);
            teamLinkCard.setUrl(url);
            teamLinkCard.setImg_url(img_url);
            for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                TeamLinkCardReceiver teamLinkCardReceiver = (TeamLinkCardReceiver) teamCardReceiver;
                LinkApp linkApp = (LinkApp) teamLinkCardReceiver.getApp();
                linkApp.getLinkAppInformation().setUrl(url);
                linkApp.getLinkAppInformation().setImg_url(img_url);
            }
            sm.saveOrUpdate(teamCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP, WebSocketMessageAction.CHANGED, teamCard.getJson()));

            sm.setSuccess(teamCard.getJson());
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
