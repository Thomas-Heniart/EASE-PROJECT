package com.Ease.API.V1.Teams;

import com.Ease.Team.Team;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
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

@WebServlet("/api/v1/teams/EditTeamUserPhoneNumber")
public class ServletEditTeamUserPhoneNumber extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser_connected = sm.getTeamUser(team_id);
            TeamUser teamUser = team.getTeamUserWithId(teamUser_id);
            if (!teamUser_connected.isSuperior(teamUser) && teamUser != teamUser_connected)
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot edit this user.");
            String phone_number = sm.getStringParam("phone_number", true, false);
            if (phone_number.equals("") || !Regex.isPhoneNumber(phone_number))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid phone number.");
            teamUser.getUser().getPersonalInformation().setPhone_number(phone_number);
            sm.saveOrUpdate(teamUser.getUser().getPersonalInformation());
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser.getWebSocketJson()));
            sm.setSuccess(teamUser.getJson());
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
