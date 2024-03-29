package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamSingleCard;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
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

@WebServlet("/api/v1/renewMagicLink")
public class RenewMagicLink extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCard_id);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!team.isValidFreemium())
                throw new HttpServletException(HttpStatus.BadRequest, "Not allowed to do this");
            if (!teamUser_connected.isTeamAdmin() && !teamUser_connected.equals(teamCard.getTeamUser_sender()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit this card");
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot generate a link for this card");
            if (teamCard.isTeamWebsiteCard())
                ((TeamSingleCard) teamCard).generateMagicLink();
            else
                ((TeamSingleSoftwareCard) teamCard).generateMagicLink();
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamCard.getWebSocketJson()));
            sm.setSuccess(teamCard.getJson());
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
