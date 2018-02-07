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

@WebServlet("/api/v1/teams/EditTeamCardFiller")
public class EditTeamCardFiller extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamSingleCard())
                throw new HttpServletException(HttpStatus.BadRequest, "No such team single card");
            Integer filler_id = sm.getIntParam("filler_id", true, false);
            TeamUser filler = team.getTeamUserWithId(filler_id);
            if (teamCard.isTeamSoftwareCard()) {
                TeamSingleSoftwareCard teamSingleSoftwareCard = (TeamSingleSoftwareCard) teamCard;
                TeamUser teamUser = teamSingleSoftwareCard.getTeamUser_filler_test();
                if (teamUser != null)
                    teamUser.removeTeamSingleSoftwareCardToFill(teamSingleSoftwareCard);
                teamSingleSoftwareCard.setTeamUser_filler_test(filler);
                filler.addTeamSingleSoftwareCardToFill(teamSingleSoftwareCard);
            } else {
                TeamSingleCard teamSingleCard = (TeamSingleCard) teamCard;
                TeamUser teamUser = teamSingleCard.getTeamUser_filler();
                if (teamUser != null)
                    teamUser.removeTeamSingleCardToFill(teamSingleCard);
                teamSingleCard.setTeamUser_filler(filler);
                filler.addTeamSingleCardToFill(teamSingleCard);
            }
            sm.saveOrUpdate(teamCard);
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
