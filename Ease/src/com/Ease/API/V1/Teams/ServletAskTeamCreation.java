package com.Ease.API.V1.Teams;

import com.Ease.Dashboard.User.User;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.*;
import com.Ease.Utils.Crypto.CodeGenerator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by thomas on 09/05/2017.
 */
@WebServlet("/api/v1/teams/AskTeamCreation")
public class ServletAskTeamCreation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            User user = sm.getUser();
            /* Check if this user already payed */
            String email = sm.getServletParam("email", true);
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new GeneralException(com.Ease.Utils.ServletManager.Code.ClientWarning, "Invalid email.");
            DataBaseConnection db = sm.getDB();
            int transaction = db.startTransaction();
            DatabaseRequest databaseRequest = db.prepareRequest("SELECT * FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE (users.email <> teamUsers.email) AND (users.email = ? OR teamUsers.email = ?);");
            databaseRequest.setString(email);
            databaseRequest.setString(email);
            DatabaseResult rs = databaseRequest.get();
            if (rs.next()) {
                if (user != null && !email.equals(user.getEmail()))
                    throw new GeneralException(com.Ease.Utils.ServletManager.Code.ClientWarning, "Email already taken.");
            }
            databaseRequest = db.prepareRequest("DELETE FROM createTeamInvitations WHERE email = ?;");
            databaseRequest.setString(email);
            databaseRequest.set();
            String digits = CodeGenerator.generateDigits(6);
            databaseRequest = db.prepareRequest("INSERT INTO createTeamInvitations values(NULL, ?, ?, DATE_ADD(NOW(), INTERVAL 3 DAY))");
            databaseRequest.setString(email);
            databaseRequest.setString(digits);
            databaseRequest.set();
            db.commitTransaction(transaction);
            SendGridMail sendGridMail = new SendGridMail("Agathe @Ease", "contact@ease.space");
            sendGridMail.sendCreateTeamEmail(email, digits);
            sm.setResponse(ServletManager.Code.Success, "Email sent");
        } catch (Exception e) {
            sm.setResponse(e);
        }
        sm.sendResponse();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
