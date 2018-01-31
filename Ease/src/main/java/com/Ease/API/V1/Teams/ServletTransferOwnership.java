package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.User.User;
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

/**
 * Created by thomas on 02/06/2017.
 */
@WebServlet("/api/v1/teams/TransferOwnership")
public class ServletTransferOwnership extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeOwnerOfTeam(team);
            User user = sm.getUser();
            String password = sm.getStringParam("password", false, false);
            if (!user.getUserKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password.");
            TeamUser teamUser = sm.getTeamUser(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser new_teamUser_owner = team.getTeamUserWithId(teamUser_id);
            if (!new_teamUser_owner.isVerified() || new_teamUser_owner.isDisabled())
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot transfer your ownership to this user.");
            teamUser.transferOwnershipTo(new_teamUser_owner);
            if (!team.isValidFreemium()) {
                for (Channel channel : team.getChannels().values()) {
                    channel.setRoom_manager(new_teamUser_owner);
                    sm.saveOrUpdate(channel);
                }
            }
            NotificationFactory.getInstance().createTransferOwnershipNotification(new_teamUser_owner, teamUser, sm.getUserWebSocketManager(new_teamUser_owner.getUser().getDb_id()),  sm.getHibernateQuery());
            new_teamUser_owner.setDepartureDate(null);
            sm.saveOrUpdate(new_teamUser_owner);
            sm.saveOrUpdate(teamUser);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(new_teamUser_owner.getEmail());
            mailJetBuilder.setTemplateId(180252);
            mailJetBuilder.addVariable("team_name", team.getName());
            mailJetBuilder.addVariable("first_name", teamUser.getUser().getPersonalInformation().getFirst_name());
            mailJetBuilder.addVariable("last_name", teamUser.getUser().getPersonalInformation().getLast_name());
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "#/teams/" + team.getDb_id());
            mailJetBuilder.sendEmail();
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, new_teamUser_owner.getWebSocketJson()));
            sm.setSuccess("Ownership transferred");
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
