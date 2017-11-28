package com.Ease.API.V1.Teams.TeamCards.TeamLinkCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.NewDashboard.App;
import com.Ease.NewDashboard.AppFactory;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamLinkCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamLinkCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/teams/AddTeamLinkCardReceiver")
public class AddTeamLinkCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamLinkCard teamLinkCard = (TeamLinkCard) hibernateQuery.get(TeamLinkCard.class, teamCard_id);
            if (teamLinkCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "no such team link card");
            Team team = teamLinkCard.getTeam();
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            App app;
            if (teamUser.isVerified())
                app = AppFactory.getInstance().createLinkApp(teamLinkCard.getName(), teamLinkCard.getUrl(), teamLinkCard.getImg_url(), teamUser.getOrCreateProfile(hibernateQuery));
            else
                app = AppFactory.getInstance().createLinkApp(teamLinkCard.getName(), teamLinkCard.getUrl(), teamLinkCard.getImg_url());
            TeamCardReceiver teamCardReceiver = new TeamLinkCardReceiver(app, teamLinkCard, teamUser);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (teamUser.isVerified() && !teamUser_connected.equals(teamUser))
                NotificationFactory.getInstance().createAppSentNotification(teamUser.getUser(), teamUser_connected, teamCardReceiver, sm.getUserWebSocketManager(teamUser.getUser().getDb_id()), sm.getHibernateQuery());
            sm.saveOrUpdate(teamCardReceiver);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_APP_RECEIVER, WebSocketMessageAction.CREATED, teamCardReceiver.getJson()));
            sm.setSuccess(teamCardReceiver.getCardJson());
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
