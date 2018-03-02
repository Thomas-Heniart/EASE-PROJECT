package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.*;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
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
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.isTeamAdmin() && !teamUser_connected.equals(teamCard.getTeamUser_sender()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit this card");
            TeamUser teamUser_admin = sm.getTeamUser(team);
            Channel channel = teamCard.getChannel();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("DELETE FROM Update u WHERE u.teamCard.db_id = :card_id");
            hibernateQuery.setParameter("card_id", team_card_id);
            hibernateQuery.executeUpdate();
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
                            hibernateQuery.queryString("UPDATE LinkApp l SET l.linkAppInformation = :info WHERE l.db_id = :id");
                            hibernateQuery.setParameter("info", linkAppInformation);
                            hibernateQuery.setParameter("id", linkApp.getDb_id());
                            linkApp.setLinkAppInformation(linkAppInformation);
                        }
                    }
                }
                Profile profile = app.getProfile();
                if (profile != null)
                    profile.removeAppAndUpdatePositions(teamCardReceiver.getApp(), sm.getHibernateQuery());
                TeamUser teamUser = teamCardReceiver.getTeamUser();
                if (!teamUser.equals(sm.getTeamUser(team)))
                    NotificationFactory.getInstance().createRemovedFromTeamCardNotification(teamUser, teamUser_admin, teamCard.getName(), teamCard.getLogo(), teamCard.getChannel(), sm.getUserIdMap(), sm.getHibernateQuery());
                teamUser.removeTeamCardReceiver(teamCardReceiver);
            }
            if (teamCard.isTeamSingleCard()) {
                if (teamCard.isTeamSoftwareCard()) {
                    TeamSingleSoftwareCard teamSingleSoftwareCard = (TeamSingleSoftwareCard) teamCard;
                    TeamUser teamUser = teamSingleSoftwareCard.getTeamUser_filler_test();
                    if (teamUser != null)
                        teamUser.removeTeamSingleSoftwareCardToFill(teamSingleSoftwareCard);
                } else {
                    TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                    TeamUser teamUser = teamSingleCard.getTeamUser_filler();
                    if (teamUser != null)
                        teamUser.removeTeamSingleCardToFill(teamSingleCard);
                }
            }
            team.removeTeamCard(teamCard);
            channel.removeTeamCard(teamCard);
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
