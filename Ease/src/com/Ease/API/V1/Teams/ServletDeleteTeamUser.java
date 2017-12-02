package com.Ease.API.V1.Teams;

import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 09/06/2017.
 */
@WebServlet("/api/v1/teams/DeleteTeamUser")
public class ServletDeleteTeamUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_user_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser_to_delete = team.getTeamUserWithId(team_user_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.isSuperior(teamUser_to_delete))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot do this");
            if (!teamUser_to_delete.getTeamSingleCardToFillSet().isEmpty()) {
                StringBuilder message = new StringBuilder("This persone cannot be delete while he/she is responsble to fill credentials. To delete ").append(teamUser_to_delete.getUsername()).append("  you need to delete him/her first from ");
                teamUser_to_delete.getTeamSingleCardToFillSet().forEach(teamSingleCard -> message.append(teamSingleCard.getName()).append(", "));
                message.replace(message.length() - 2, message.length(), ".");
                throw new HttpServletException(HttpStatus.Forbidden, message.toString());
            }
            List<Channel> channelList = new LinkedList<>();
            for (Channel channel : team.getChannelsForTeamUser(teamUser_to_delete)) {
                if (channel.getRoom_manager() == teamUser_to_delete)
                    channelList.add(channel);
            }
            if (!channelList.isEmpty()) {
                StringBuilder message = new StringBuilder("This user cannot be deleted as long as he/she remains Room Manager of ");
                channelList.forEach(channel -> message.append("#").append(channel.getName()).append(", "));
                message.replace(message.length() - 2, message.length(), ".");
                throw new HttpServletException(HttpStatus.Forbidden, message.toString());
            }
            String forEmail = "";
            teamUser_to_delete.getTeamCardReceivers().forEach(sm::deleteObject);
            if (forEmail.length() != 0 && teamUser_to_delete.getAdmin_id() != null && teamUser_to_delete.getAdmin_id() > 0) {
                forEmail = forEmail.substring(0, forEmail.length() - 2);
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.setTemplateId(180165);
                mailJetBuilder.addTo(teamUser_connected.getEmail());
                mailJetBuilder.addVariable("first_name", teamUser_to_delete.getFirstName());
                mailJetBuilder.addVariable("last_name", teamUser_to_delete.getLastName());
                mailJetBuilder.addVariable("team_name", team.getName());
                mailJetBuilder.addVariable("apps", forEmail);
                mailJetBuilder.sendEmail();
            }
            for (TeamUser teamUser : team.getTeamUsers().values()) {
                if (teamUser.getAdmin_id() == null)
                    continue;
                if (teamUser.getAdmin_id().equals(teamUser_to_delete.getDb_id())) {
                    teamUser.setAdmin_id(teamUser_connected.getDb_id());
                    sm.saveOrUpdate(teamUser);
                }
            }
            team.getChannels().values().forEach(channel -> {
                channel.removeTeamUser(teamUser_to_delete);
                channel.removePendingTeamUser(teamUser_to_delete);
            });
            team.removeTeamUser(teamUser_to_delete);
            sm.deleteObject(teamUser_to_delete);
            JSONObject ws_obj = new JSONObject();
            ws_obj.put("team_id", team_id);
            ws_obj.put("team_user_id", team_user_id);
            WebSocketMessage webSocketMessage = WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.REMOVED, ws_obj);
            sm.addWebSocketMessage(webSocketMessage);
            sm.setSuccess("TeamUser deleted");
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
