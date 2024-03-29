package com.Ease.API.V1.Teams;

import com.Ease.Team.Channel;
import com.Ease.Team.Onboarding.OnboardingSteps;
import com.Ease.Team.OnboardingStatus;
import com.Ease.Team.Team;
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

/**
 * Created by thomas on 13/06/2017.
 */
@WebServlet("/api/v1/teams/CreateChannel")
public class ServletCreateChannel extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            String name = sm.getStringParam("name", true, false);
            String purpose = sm.getStringParam("purpose", true, true);
            if (name == null || name.equals("") || !Regex.isValidRoomName(name))
                throw new HttpServletException(HttpStatus.BadRequest, "Room names can't contain spaces, periods or most punctuation and must be shorter than 22 characters.");
            if (purpose == null)
                purpose = "";
            if (purpose.length() >= 250)
                throw new HttpServletException(HttpStatus.BadRequest, "Purpose of room cannot be greater than 250 characters");
            if (team.getChannelNamed(name) != null)
                throw new HttpServletException(HttpStatus.BadRequest, name + " is already used for another room");
            Channel channel = new Channel(team, name, purpose, sm.getTeamUser(team_id));
            sm.saveOrUpdate(channel);
            OnboardingStatus onboardingStatus = team.getOnboardingStatus();
            if (onboardingStatus.getStep() < OnboardingSteps.ROOMS_CREATED.ordinal()) {
                onboardingStatus.setStep(OnboardingSteps.ROOMS_CREATED.ordinal());
                sm.saveOrUpdate(onboardingStatus);
            }
            team.addChannel(channel);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_ROOM, WebSocketMessageAction.CREATED, channel.getWebSocketJson()));
            sm.setSuccess(channel.getJson());
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
