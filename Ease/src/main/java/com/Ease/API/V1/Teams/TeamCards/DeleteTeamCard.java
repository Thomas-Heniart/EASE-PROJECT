package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/DeleteTeamCard")
public class DeleteTeamCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = (TeamCard) sm.getHibernateQuery().get(TeamCard.class, team_card_id);
            Team team = teamCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeAdminOfTeam(teamCard.getTeam());
            TeamUser teamUser_admin = sm.getTeamUser(team);
            for (TeamCardReceiver teamCardReceiver : teamCard.getTeamCardReceiverMap().values()) {
                App app = teamCardReceiver.getApp();
                if (app.isWebsiteApp()) {
                    WebsiteApp websiteApp = (WebsiteApp) app;
                    websiteApp.getLogWithAppSet().forEach(logWithApp -> {
                        Profile profile1 = logWithApp.getProfile();
                        profile1.removeAppAndUpdatePositions(logWithApp, sm.getHibernateQuery());
                        sm.deleteObject(logWithApp);
                    });
                } else if (app.isLinkApp()) {
                    TeamLinkCard teamLinkCard = (TeamLinkCard) teamCard;
                    LinkApp linkApp = (LinkApp) app;
                    TeamCardReceiver other_receiver = teamCard.getTeamCardReceiverMap().values().stream().filter(teamCardReceiver1 -> !teamCardReceiver.equals(teamCardReceiver1)).findFirst().orElse(null);
                    if (other_receiver != null) {
                        LinkApp linkApp1 = (LinkApp) other_receiver.getApp();
                        if (linkApp.getLinkAppInformation().equals(linkApp1.getLinkAppInformation())) {
                            LinkAppInformation linkAppInformation = new LinkAppInformation(teamLinkCard.getUrl(), teamLinkCard.getImg_url());
                            sm.saveOrUpdate(linkAppInformation);
                            linkApp.setLinkAppInformation(linkAppInformation);
                            sm.saveOrUpdate(linkApp);
                        }
                    }
                }
                Profile profile = app.getProfile();
                if (profile != null)
                    profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
                TeamUser teamUser = teamCardReceiver.getTeamUser();
                if (!teamUser.equals(sm.getTeamUser(team)))
                    NotificationFactory.getInstance().createRemovedFromTeamCardNotification(teamUser, teamUser_admin, teamCard.getName(), teamCard.getLogo(), teamCard.getChannel(), sm.getUserIdMap(), sm.getHibernateQuery());
            }
            sm.deleteObject(teamCard);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("team_id", team.getDb_id());
            jsonObject.put("team_card_id", team_card_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.REMOVED, jsonObject));
            sm.setSuccess("Team card deleted");
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
