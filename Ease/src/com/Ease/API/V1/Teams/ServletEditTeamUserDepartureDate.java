package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.SharedApp;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DataBaseConnection;
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
import java.util.Date;

/**
 * Created by thomas on 02/06/2017.
 */
@WebServlet("/api/v1/teams/EditTeamUserDepartureDate")
public class ServletEditTeamUserDepartureDate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {

            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeAdminOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            if (!team.isValidFreemium())
                throw new HttpServletException(HttpStatus.Forbidden, "This is a feature from pro plan.");
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            TeamUser teamUser_to_modify = team.getTeamUserWithId(teamUser_id);
            if (!teamUser.isSuperior(teamUser_to_modify))
                throw new HttpServletException(HttpStatus.Forbidden, "You cannot do this dude.");
            Long departureDate = sm.getLongParam("departure_date", true, false);
            if (departureDate == 0)
                teamUser_to_modify.setDepartureDate(null);
            else {
                if (departureDate <= sm.getTimestamp().getTime())
                    throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid departure date.");
                teamUser_to_modify.setDepartureDate(new Date(departureDate));
            }
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            for (SharedApp sharedApp : teamUser_to_modify.getSharedApps())
                ((App) sharedApp).setDisabled(false, db);
            db.commitTransaction(transaction);
            sm.saveOrUpdate(teamUser_to_modify);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_USER, WebSocketMessageAction.CHANGED, teamUser_to_modify.getJson(), teamUser_to_modify.getOrigin()));
            sm.setSuccess(teamUser_to_modify.getJson());
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
