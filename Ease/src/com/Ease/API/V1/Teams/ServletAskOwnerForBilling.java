package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
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

@WebServlet("/api/v1/teams/AskOwnerForBilling")
public class ServletAskOwnerForBilling extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeConnected();
            Team team = sm.getTeam(team_id);
            TeamUser teamUser = sm.getUser().getTeamUser(team);
            if (teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (!team.isFreemium())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (team.isCard_entered())
                throw new HttpServletException(HttpStatus.Forbidden);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(218993);
            mailJetBuilder.addTo(team.getTeamUserOwner().getEmail());
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("url", Variables.URL_PATH + team.getDb_id() + "/" + team.getDefaultChannel().getDb_id() + "/settings/payment");
            mailJetBuilder.sendEmail();
            NotificationFactory.getInstance().createAskOwnerForBillingNotification(team, teamUser, sm.getUserWebSocketManager(team.getTeamUserOwner().getUser().getDb_id()), sm.getHibernateQuery());
            sm.setSuccess("Message sent");
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
