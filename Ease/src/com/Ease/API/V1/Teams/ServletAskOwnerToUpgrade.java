package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.Team;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
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
import java.util.Date;

@WebServlet("/api/v1/teams/AskOwnerToUpgrade")
public class ServletAskOwnerToUpgrade extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true, false);
            sm.needToBeTeamUserOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            Team team = teamManager.getTeamWithId(team_id);
            if (team.isValidFreemium())
                throw new HttpServletException(HttpStatus.Forbidden);
            if (team.isFreemium())
                throw new HttpServletException(HttpStatus.Forbidden);
            TeamUser teamUser = sm.getTeamUserForTeam(team);
            if (teamUser.isTeamOwner())
                throw new HttpServletException(HttpStatus.Forbidden);
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(218931);
            mailJetBuilder.addTo(team.getTeamUserOwner().getEmail());
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
            mailJetBuilder.addVariable("last_name", teamUser.getLastName());
            mailJetBuilder.addVariable("url", Variables.URL_PATH + "todo");
            mailJetBuilder.sendEmail();
            team.getTeamUserOwner().addNotification(teamUser.getUsername() + " suggests to upgrade your Ease.space team!", team.getDefaultChannel().getDb_id() + "/settings/information", "/resources/notifications/hand_shake.png", new Date(), sm.getDB());
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
