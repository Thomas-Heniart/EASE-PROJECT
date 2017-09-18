package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import com.Ease.Utils.Regex;
import com.Ease.Utils.Servlets.PostServletManager;
import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@WebServlet("/api/v1/common/AskRegistration")
public class ServletAskRegistration extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, true);
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new HttpServletException(HttpStatus.BadRequest, "That doesn't look like a valid email address!");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.querySQLString("SELECT COUNT(*) FROM users LEFT JOIN teamUsers ON users.id = teamUsers.user_id WHERE users.email = ? OR teamUsers.email = ?;");
            hibernateQuery.setParameter(1, email);
            hibernateQuery.setParameter(2, email);
            int count = ((BigInteger) hibernateQuery.getSingleResult()).intValue();
            if (count > 0)
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already assigned to an account.");
            String digits = CodeGenerator.generateDigits(6);
            hibernateQuery.querySQLString("DELETE FROM userPendingRegistrations WHERE email = ?");
            hibernateQuery.setParameter(1, email);
            hibernateQuery.executeUpdate();
            hibernateQuery.querySQLString("INSERT INTO userPendingRegistrations VALUES (null, ?, ?, default)");
            hibernateQuery.setParameter(1, email);
            hibernateQuery.setParameter(2, digits);
            hibernateQuery.executeUpdate();
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(180976);
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addVariable("first_digits", digits.substring(0, 3));
            mailJetBuilder.addVariable("last_digits", digits.substring(3));
            mailJetBuilder.addTo(email);
            mailJetBuilder.sendEmail();
            JSONObject res = new JSONObject();
            res.put("success", true);
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
