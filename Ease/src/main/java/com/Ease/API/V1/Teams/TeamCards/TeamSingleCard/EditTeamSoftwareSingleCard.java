package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleSoftwareCard;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;
import com.Ease.websocketV1.WebSocketMessageAction;
import com.Ease.websocketV1.WebSocketMessageFactory;
import com.Ease.websocketV1.WebSocketMessageType;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/EditTeamSoftwareSingleCard")
public class EditTeamSoftwareSingleCard extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamSingleSoftwareCard teamSingleSoftwareCard = (TeamSingleSoftwareCard) hibernateQuery.get(TeamSingleSoftwareCard.class, team_card_id);
            if (teamSingleSoftwareCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "This card does not exist");
            Team team = teamSingleSoftwareCard.getTeam();
            sm.initializeTeamWithContext(team);
            TeamUser teamUser = sm.getTeamUser(team);
            if (!teamUser.isTeamAdmin() && (teamSingleSoftwareCard.getTeamUser_filler() == null || !teamUser.equals(teamSingleSoftwareCard.getTeamUser_filler())))
                throw new HttpServletException(HttpStatus.Forbidden);
            String name = sm.getStringParam("name", true, false);
            if (name.equals("") || name.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter name");
            teamSingleSoftwareCard.setName(name);
            String description = sm.getStringParam("description", true, false);
            if (description.length() > 255)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter description");
            teamSingleSoftwareCard.setDescription(description);
            Integer password_reminder_interval = sm.getIntParam("password_reminder_interval", true, false);
            if (password_reminder_interval < 0)
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid parameter password_reminder_interval");
            teamSingleSoftwareCard.setPassword_reminder_interval(password_reminder_interval);
            JSONObject account_information = sm.getJsonParam("account_information", false, false);
            sm.decipher(account_information);
            account_information = teamSingleSoftwareCard.getSoftware().getAllCredentialsFromJson(account_information);
            teamSingleSoftwareCard.getAccount().edit(account_information, password_reminder_interval, hibernateQuery);
            sm.saveOrUpdate(teamSingleSoftwareCard);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD, WebSocketMessageAction.CHANGED, teamSingleSoftwareCard.getWebSocketJson()));
            sm.setSuccess(teamSingleSoftwareCard.getJson());
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
