package com.Ease.API.V1.Teams;

import com.Ease.Team.*;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Created by thomas on 02/06/2017.
 */
@WebServlet("/api/v1/teams/EditTeamUserRole")
public class ServletEditTeamUserRole extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUserToModify = team.getTeamUserWithId(teamUser_id);
            if (!(teamUser.isSuperior(teamUserToModify) || teamUser == teamUserToModify))
                throw new HttpServletException(HttpStatus.Forbidden, "You don't have access.");
            if (teamUserToModify.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden, "You are the owner, you can only transfer your ownership to someone else.");
            Integer roleValue = sm.getIntParam("role", true, false);
            if (!team.isValidFreemium() && roleValue != TeamUserRole.Role.MEMBER.getValue())
                throw new HttpServletException(HttpStatus.Forbidden, "You must upgrade to have multiple admins.");
            if (!TeamUserRole.isInferiorToOwner(roleValue))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot transfer your ownership from here.");
            List<Channel> channelList = new LinkedList<>();
            for (Channel channel : team.getChannelsForTeamUser(teamUserToModify)) {
                if (channel.getRoom_manager() == teamUserToModify)
                    channelList.add(channel);
            }
            if (!channelList.isEmpty()) {
                String message = "This user cannot become a member as long as he/she remains Room Manager of ";
                for (Channel channel : channelList) {
                    message += ("#" + channel.getName());
                    if (channelList.indexOf(channel) == channelList.size() - 1)
                        message += ".";
                    else
                        message += ", ";
                }
                throw new HttpServletException(HttpStatus.Forbidden, message);
            }
            teamUserToModify.getTeamUserRole().setRoleValue(roleValue);
            if (teamUser != teamUserToModify)
                teamUserToModify.addNotification(teamUser.getUsername() + " changed your role to " + teamUserToModify.getTeamUserRole().getRoleName(), "@" + teamUserToModify.getDb_id(), "/resources/notifications/user_role_changed.png", sm.getTimestamp(), sm.getDB());
            sm.saveOrUpdate(teamUserToModify.getTeamUserRole());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUserToModify.getJson(), teamUserToModify.getOrigin()));
            sm.setSuccess("TeamUser role edited.");
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
