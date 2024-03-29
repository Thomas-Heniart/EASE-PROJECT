package com.Ease.API.V1.Teams.TeamCards.TeamSingleCard;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamCard.TeamSingleCard;
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

@WebServlet("/api/v1/teams/SendFillerReminder")
public class ServletSendFillerReminder extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer teamCard_id = sm.getIntParam("team_card_id", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            TeamSingleCard teamSingleCard = (TeamSingleCard) hibernateQuery.get(TeamSingleCard.class, teamCard_id);
            if (teamSingleCard == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No such teamCard");
            Team team = teamSingleCard.getTeam();
            sm.initializeTeamWithContext(team);
            sm.needToBeTeamUserOfTeam(team);
            TeamUser filler = teamSingleCard.getTeamUser_filler();
            TeamUser teamUser_connected = sm.getTeamUser(team);
            if (filler == null) {
                TeamUser room_manager = teamSingleCard.getChannel().getRoom_manager();
                NotificationFactory.getInstance().createRemindTeamCardFiller(teamSingleCard, sm.getTeamUser(teamSingleCard.getTeam()), room_manager, sm.getUserWebSocketManager(room_manager.getUser().getDb_id()), sm.getHibernateQuery());
            } else {
                NotificationFactory.getInstance().createRemindTeamCardFiller(teamSingleCard.getTeamCardReceiver(filler), sm.getTeamUser(team), sm.getUserIdMap(), sm.getHibernateQuery());
                if (filler.isVerified()) {
                    MailJetBuilder mailJetBuilder = new MailJetBuilder();
                    mailJetBuilder.setFrom("contact@ease.space", "Ease.Space");
                    mailJetBuilder.setTemplateId(307889);
                    mailJetBuilder.addTo(filler.getEmail(), filler.getUsername());
                    mailJetBuilder.addVariable("first_name", teamUser_connected.getUser().getPersonalInformation().getFirst_name());
                    mailJetBuilder.addVariable("last_name", teamUser_connected.getUser().getPersonalInformation().getLast_name());
                    mailJetBuilder.addVariable("app_name", teamSingleCard.getName());
                    mailJetBuilder.addVariable("url", Variables.URL_PATH);
                    mailJetBuilder.sendEmail();
                }
            }
            sm.setSuccess("Notification sent");
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
