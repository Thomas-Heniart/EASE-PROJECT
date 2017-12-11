package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.JoinTeamCardRequest;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
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

@WebServlet("/api/v1/teams/DeleteJoinTeamCard")
public class DeleteJoinTeamCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            Integer request_id = sm.getIntParam("request_id", true, false);
            JoinTeamCardRequest joinTeamCardRequest = (JoinTeamCardRequest) hibernateQuery.get(JoinTeamCardRequest.class, request_id);
            if (joinTeamCardRequest == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such request");
            TeamCard teamCard = joinTeamCardRequest.getTeamCard();
            Team team = teamCard.getTeam();
            sm.needToBeAdminOfTeam(team);
            String teamKey = (String) sm.getTeamProperties(team.getDb_id()).get("teamKey");
            teamCard.decipher(teamKey);
            teamCard.removeJoinTeamCardRequest(joinTeamCardRequest);
            sm.deleteObject(joinTeamCardRequest);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("team_card_id", teamCard.getDb_id());
            jsonObject.put("request_id", request_id);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_REQUEST, WebSocketMessageAction.REMOVED, jsonObject));
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
