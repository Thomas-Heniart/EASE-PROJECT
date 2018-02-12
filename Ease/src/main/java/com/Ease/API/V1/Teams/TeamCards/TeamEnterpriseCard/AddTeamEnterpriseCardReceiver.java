package com.Ease.API.V1.Teams.TeamCards.TeamEnterpriseCard;

import com.Ease.NewDashboard.*;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCard.TeamEnterpriseCard;
import com.Ease.Team.TeamCard.TeamEnterpriseSoftwareCard;
import com.Ease.Team.TeamCardReceiver.TeamCardReceiver;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
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

@WebServlet("/api/v1/teams/AddTeamEnterpriseCardReceiver")
public class AddTeamEnterpriseCardReceiver extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer teamUser_id = sm.getIntParam("team_user_id", true, false);
            JSONObject account_information = sm.getJsonParam("account_information", true, true);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a team enterprise card");
            TeamUser teamUser_receiver = team.getTeamUserWithId(teamUser_id);
            if (teamCard.containsTeamUser(teamUser_receiver))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already a receiver of this card");
            sm.decipher(account_information);
            Account account = null;
            if (account_information != null && account_information.length() != 0) {
                String teamKey = (String) sm.getTeamProperties(team_id).get("teamKey");
                account = AccountFactory.getInstance().createAccountFromJson(account_information, teamKey, teamCard.getPassword_reminder_interval(), sm.getHibernateQuery());
            }
            AppInformation appInformation = new AppInformation(teamCard.getName());
            App app;
            if (teamCard.isTeamWebsiteCard()) {
                TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
                app = new ClassicApp(appInformation, teamEnterpriseCard.getWebsite(), account);
            } else {
                TeamEnterpriseSoftwareCard teamEnterpriseSoftwareCard = (TeamEnterpriseSoftwareCard) teamCard;
                app = new SoftwareApp(appInformation, teamEnterpriseSoftwareCard.getSoftware(), account);
            }
            TeamCardReceiver teamCardReceiver = new TeamEnterpriseCardReceiver(app, teamCard, teamUser_receiver);
            if (teamUser_receiver.isVerified()) {
                Profile profile = teamUser_receiver.getOrCreateProfile(sm.getHibernateQuery());
                app.setProfile(profile);
                app.setPosition(profile.getSize());
                sm.saveOrUpdate(app);
                profile.addApp(app);
            }
            sm.saveOrUpdate(teamCardReceiver);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (!teamUser_receiver.equals(teamUser_connected))
                NotificationFactory.getInstance().createAppSentNotification(teamUser_receiver, teamUser_connected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
            teamCard.addTeamCardReceiver(teamCardReceiver);
            teamUser_receiver.addTeamCardReceiver(teamCardReceiver);
            sm.addWebSocketMessage(WebSocketMessageFactory.createWebSocketMessage(WebSocketMessageType.TEAM_CARD_RECEIVER, WebSocketMessageAction.CREATED, teamCardReceiver.getWebSocketJson()));
            sm.setSuccess(teamCardReceiver.getCardJson());
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
