package com.Ease.API.V1.Teams;

import com.Ease.Context.Variables;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/teams/InvitePeople")
public class ServletInvitePeople extends HttpServlet {

    private final static int jackpot = 1500;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            Integer team_id = sm.getIntParam("team_id", true);
            sm.needToBeOwnerOfTeam(team_id);
            TeamManager teamManager = (TeamManager) sm.getContextAttr("teamManager");
            String email1 = sm.getStringParam("email1", true);
            String email2 = sm.getStringParam("email2", true);
            String email3 = sm.getStringParam("email3", true);
            if (email1 == null || email2 == null || email3 == null || email1.equals("") || email2.equals("") || email3.equals("") || !Regex.isEmail(email1) || !Regex.isEmail(email2) || !Regex.isEmail(email3))
                throw new HttpServletException(HttpStatus.BadRequest, "One or more emails are invalid");
            DatabaseRequest databaseRequest = sm.getDB().prepareRequest("INSERT INTO userAndEmailInvitationsMap values (null, ?, ?, ?, ?);");
            databaseRequest.setInt(sm.getUser().getDBid());
            databaseRequest.setString(email1);
            databaseRequest.setString(email2);
            databaseRequest.setString(email3);
            databaseRequest.set();
            teamManager.getTeamWithId(team_id).increaseAccountBalance(jackpot);
            /* Use mailjet api */
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(178497);
            mailJetBuilder.setFrom("victor@ease.space", "Victor @Ease");
            mailJetBuilder.addCc(sm.getTeamUserForTeamId(team_id).getEmail());
            mailJetBuilder.addTo(email1);
            mailJetBuilder.sendEmail();
            sm.setSuccess("Emails sent");
        } catch (Exception e) {
            e.printStackTrace();
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
