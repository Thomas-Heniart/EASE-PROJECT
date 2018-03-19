package com.Ease.API.V1.Teams.TeamCards;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamCard;
import com.Ease.Team.TeamCardReceiver.TeamEnterpriseCardReceiver;
import com.Ease.Team.TeamUser;
import com.Ease.User.NotificationFactory;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/SendFillEnterpriseCardReminder")
public class SendFillEnterpriseCardReminder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            Team team = sm.getTeam(team_id);
            sm.needToBeAdminOfTeam(team);
            Integer team_card_id = sm.getIntParam("team_card_id", true, false);
            Integer team_card_receiver_id = sm.getIntParam("team_card_receiver_id", true, false);
            TeamCard teamCard = team.getTeamCard(team_card_id);
            if (!teamCard.isTeamEnterpriseCard())
                throw new HttpServletException(HttpStatus.Forbidden, "You must choose a team enterprise card");
            TeamEnterpriseCardReceiver teamEnterpriseCardReceiver = (TeamEnterpriseCardReceiver) teamCard.getTeamCardReceiver(team_card_receiver_id);
            TeamUser teamUser_connected = sm.getTeamUser(team);
            TeamUser teamUser = teamEnterpriseCardReceiver.getTeamUser();
            if (teamUser.isVerified()) {
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                mailJetBuilder.setTemplateId(307889);
                mailJetBuilder.addTo(teamUser.getEmail(), teamUser.getUsername());
                mailJetBuilder.addVariable("first_name", teamUser_connected.getUser().getPersonalInformation().getFirst_name());
                mailJetBuilder.addVariable("last_name", teamUser_connected.getUser().getPersonalInformation().getLast_name());
                mailJetBuilder.addVariable("app_name", teamCard.getName());
                mailJetBuilder.addVariable("url", Variables.URL_PATH);
                mailJetBuilder.sendEmail();
                NotificationFactory.getInstance().createRemindTeamCardFiller(teamCard, teamUser, sm.getTeamUser(team), sm.getUserWebSocketManager(teamUser.getUser().getDb_id()), sm.getHibernateQuery());
            }
            sm.setSuccess("Reminder sent");
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
