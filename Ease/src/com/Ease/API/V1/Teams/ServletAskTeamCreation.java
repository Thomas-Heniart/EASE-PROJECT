package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 09/05/2017.
 */
@WebServlet("/api/v1/teams/AskTeamCreation")
public class ServletAskTeamCreation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            String email = sm.getStringParam("email", true);
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "Invalid email.");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            JSONObject res = new JSONObject();
            if (user.getVerifiedEmails().contains(email))
                res.put("need_digits", false);
            else {
                DatabaseRequest databaseRequest = db.prepareRequest("DELETE FROM pendingTeamCreations WHERE email = ?;");
                databaseRequest.setString(email);
                databaseRequest.set();
                String digits = CodeGenerator.generateDigits(6);
                databaseRequest = db.prepareRequest("INSERT INTO pendingTeamCreations values(NULL, ?, ?, default)");
                databaseRequest.setString(email);
                databaseRequest.setString(digits);
                databaseRequest.set();
                db.commitTransaction(transaction);
                MailJetBuilder mailJetBuilder = new MailJetBuilder();
                mailJetBuilder.setTemplateId(178497);
                mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
                mailJetBuilder.addVariable("first_digits", digits.substring(0, 3));
                mailJetBuilder.addVariable("last_digits", digits.substring(3));
                mailJetBuilder.addTo(email);
                mailJetBuilder.sendEmail();
                res.put("need_digits", true);
            /*SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            sendGridMail.sendCreateTeamEmail(email, digits);*/
            }
            sm.setSuccess(res);

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
