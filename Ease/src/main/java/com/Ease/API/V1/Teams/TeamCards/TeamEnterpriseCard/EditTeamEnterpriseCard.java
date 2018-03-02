package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.ClassicApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
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

@WebServlet("/api/v1/teams/EditTeamEnterpriseCard")
public class EditTeamEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = (TeamCard) sm.getHibernateQuery().get(TeamEnterpriseCard.class, team_card_id);
            if (teamCard == null)
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
            Team team = teamEnterpriseCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.isTeamAdmin() && !teamUser_connected.equals(teamEnterpriseCard.getTeamUser_sender()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit this card");
            String description = sm.getStringParam("description", true, true);
            if (description == null)
                description = "";
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Description size must be under 255 characters");
            teamEnterpriseCard.setDescription(description);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamEnterpriseCard.setName(name);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0 || !team.isValidFreemium())
                password_reminder_interval = 0;
            String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            teamEnterpriseCard.decipher(teamKey);
            teamEnterpriseCard.setPassword_reminder_interval(password_reminder_interval);
            for (TeamCardReceiver teamCardReceiver : teamEnterpriseCard.getTeamCardReceiverMap().values()) {
                TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCardReceiver;
                ClassicApp classicApp = (ClassicApp) teamEnterpriseCardReceiver.getApp();
                if (classicApp.getAccount() != null)
                    classicApp.getAccount().setReminder_interval(password_reminder_interval);
            }
            sm.saveOrUpdate(teamEnterpriseCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamEnterpriseCard.getWebSocketJson()));
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
