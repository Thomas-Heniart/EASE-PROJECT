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
            Integer teamId = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(teamId);
            Integer teamUserId = sm.getIntParam("team_user_id", true, false);
            JSONObject accountInformation = sm.getJsonParam("account_information", true, true);
            Integer teamCardId = sm.getIntParam("team_card_id", true, false);
            TeamCard teamCard = team.getTeamCard(teamCardId);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser teamUserConnected = sm.getTeamUser(team);
            if (!teamUserConnected.isTeamAdmin() && !teamUserConnected.equals(teamCard.getTeamUser_sender()))
                throw new HttpServletException(HttpStatus.BadRequest, "You cannot edit this card");
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "This is not a team enterprise card");
            TeamUser teamUserReceiver = team.getTeamUserWithId(teamUserId);
            if (teamCard.containsTeamUser(teamUserReceiver))
                throw new HttpServletException(HttpStatus.BadRequest, "This user is already a receiver of this card");
            sm.decipher(accountInformation);
            Account account = null;
            if (accountInformation != null && accountInformation.length() != 0) {
                String teamKey = (String) sm.getTeamProperties(teamId).get("teamKey");
                account = AccountFactory.getInstance().createAccountFromJson(accountInformation, teamKey, teamCard.getPassword_reminder_interval(), sm.getHibernateQuery());
            }
            App app;
            if (teamCard.isTeamWebsiteCard()) {
                TeamEnterpriseCard teamEnterpriseCard = (TeamEnterpriseCard) teamCard;
                if (account == null)
                    app = AppFactory.getInstance().createClassicApp(teamCard.getName(), teamEnterpriseCard.getWebsite());
                else
                    app = AppFactory.getInstance().createClassicApp(teamCard.getName(), teamEnterpriseCard.getWebsite(), account);
            } else {
                TeamEnterpriseSoftwareCard teamEnterpriseSoftwareCard = (TeamEnterpriseSoftwareCard) teamCard;
                if (account == null)
                    app = AppFactory.getInstance().createSoftwareApp(teamCard.getName(), teamEnterpriseSoftwareCard.getSoftware());
                else
                    app = AppFactory.getInstance().createSoftwareApp(teamCard.getName(), teamEnterpriseSoftwareCard.getSoftware(), account);
            }
            TeamCardReceiver teamCardReceiver = new TeamEnterpriseCardReceiver(app, teamCard, teamUserReceiver);
            if (teamUserReceiver.isVerified()) {
                Profile profile = teamUserReceiver.getOrCreateProfile(teamCard.getChannel(), sm.getHibernateQuery());
                app.setProfile(profile);
                app.setPosition(profile.getSize());
                sm.saveOrUpdate(app);
                profile.addApp(app);
            }
            if (account != null)
                ((TeamEnterpriseCardReceiver)teamCardReceiver).calculatePasswordScore();
            sm.saveOrUpdate(teamCardReceiver);
            if (!teamUserReceiver.equals(teamUserConnected))
                NotificationFactory.getInstance().createAppSentNotification(teamUserReceiver, teamUserConnected, teamCardReceiver, sm.getUserIdMap(), sm.getHibernateQuery());
            teamCard.addTeamCardReceiver(teamCardReceiver);
            teamUserReceiver.addTeamCardReceiver(teamCardReceiver);
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
