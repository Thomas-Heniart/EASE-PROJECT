package com.Ease.API.V1.Teams;

import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Channel;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessage;
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
            sm.needToBeAdminOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeam(team_id, sm.getHibernateQuery());
            Integer team_user_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser_to_delete = team.getTeamUserWithId(team_user_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_connected.isSuperior(teamUser_to_delete))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot do this");
            List<Channel> channelList = new LinkedList<>();
            for (Channel channel : team.getChannelsForTeamUser(teamUser_to_delete)) {
                if (channel.getRoom_manager() == teamUser_to_delete)
                    channelList.add(channel);
            }
            if (!channelList.isEmpty()) {
                String message = "This user cannot be deleted as long as he/she remains Room Manager of ";
                for (Channel channel : channelList) {
                    message += ("#" + channel.getName());
                    if (channelList.indexOf(channel) == channelList.size() - 1)
                        message += ".";
                    else
                        message += ", ";
                }
                throw new HttpServletException(HttpStatus.Forbidden, message);
            }
            String forEmail = "";
            teamUser_to_delete.getTeamCardReceivers().clear();
            team.getTeamCardMap().values().stream().forEach(teamCard -> teamCard.getTeamCardReceiverMap().values().removeIf(teamCardReceiver -> teamCardReceiver.getTeamUser().equals(teamUser_to_delete)));
            teamUser_to_delete.getChannels().forEach(channel -> channel.getTeamCardMap().values().forEach(teamCard -> teamCard.getTeamCardReceiverMap().values().removeIf(teamCardReceiver -> teamCardReceiver.getTeamUser().equals(teamUser_to_delete))));
            /* for (SharedApp sharedApp : team.getAppManager().getSharedAppsForTeamUser(teamUser_to_delete)) {
                App holder = (App) sharedApp.getHolder();
                if (holder.isEmpty() || (holder.isClassicApp() && sharedApp.getHolder().getTeamUser_tenants().size() == 1)) {
                    ClassicApp classicApp = (ClassicApp) sharedApp;
                    String login = classicApp.getAccount().getInformationNamed("login");
                    forEmail += holder.getName();
                    if (login != null)
                        forEmail += " (" + login + ")";
                    forEmail += ", ";
                }

            } */
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
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            teamUser_to_delete.delete(db);
            db.commitTransaction(transaction);
            team.removeTeamUser(teamUser_to_delete);
            sm.deleteObject(teamUser_to_delete);
            WebSocketMessage webSocketMessage = WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.REMOVED, team_user_id, teamUser_to_delete.getOrigin());
            sm.addWebSocketMessage(webSocketMessage);
            /* if (teamUser_to_delete.getDashboard_user() != null)
                teamUser_to_delete.getDashboard_user().getWebSocketManager().sendObject(webSocketMessage); */
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
