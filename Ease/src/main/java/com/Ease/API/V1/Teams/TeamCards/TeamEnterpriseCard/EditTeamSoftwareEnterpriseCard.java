package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamEnterpriseSoftwareCard;
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

@WebServlet("/api/v1/teams/EditTeamSoftwareEnterpriseCard")
public class EditTeamSoftwareEnterpriseCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamEnterpriseSoftwareCard teamEnterpriseSoftwareCard = (TeamEnterpriseSoftwareCard) hibernateQuery.get(TeamEnterpriseSoftwareCard.class, team_card_id);
            if (teamEnterpriseSoftwareCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This card does not exist");
            Team team = teamEnterpriseSoftwareCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeAdminOfTeam(team);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamEnterpriseSoftwareCard.setName(name);
            String description = sm.getStringParam("description", true, false);
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            teamEnterpriseSoftwareCard.setDescription(description);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            teamEnterpriseSoftwareCard.setPassword_reminder_interval(password_reminder_interval);
            sm.saveOrUpdate(teamEnterpriseSoftwareCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamEnterpriseSoftwareCard.getWebSocketJson()));
            sm.setSuccess(teamEnterpriseSoftwareCard.getJson());
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
