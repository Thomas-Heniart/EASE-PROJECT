package com.Ease.API.V1.Teams;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Team.TeamManager;
import com.Ease.Team.TeamUser;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

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
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT COUNT(*) FROM userAndEmailInvitationsMap WHERE user_id = ?");
            hibernateQuery.setParameter(1, sm.getUser().getDBid());
            BigInteger count = (BigInteger) hibernateQuery.getSingleResult();
            if (count != null && count.intValue() > 0)
                throw new HttpServletException(HttpStatus.BadRequest, "You already invited people.");
            hibernateQuery.querySQLString("INSERT INTO userAndEmailInvitationsMap values (null, ?, ?, ?, ?);");
            hibernateQuery.setParameter(1, sm.getUser().getDBid());
            hibernateQuery.setParameter(2, email1);
            hibernateQuery.setParameter(3, email2);
            hibernateQuery.setParameter(4, email3);
            hibernateQuery.executeUpdate();
            teamManager.getTeamWithId(team_id).increaseAccountBalance(jackpot);
            String emails[] = new String[]{email1, email2, email3};
            /* Use mailjet api */
            TeamUser teamUser = sm.getTeamUserForTeamId(team_id);
            MailJetBuilder mailJetBuilder;
            for (String email : emails) {
                mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(180224);
                mailJetBuilder.setFrom("benjamin@ease.space", "Benjamin Prigent");
                mailJetBuilder.addCc(sm.getTeamUserForTeamId(team_id).getEmail());
                mailJetBuilder.addTo(email);
                mailJetBuilder.addVariable("first_name", teamUser.getFirstName());
                mailJetBuilder.addVariable("last_name", teamUser.getLastName());
                mailJetBuilder.sendEmail();
            }
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
