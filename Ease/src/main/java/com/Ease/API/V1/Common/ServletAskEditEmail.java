package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.User.User;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.Crypto.RSA;
import com.Ease.Utils.*;
import com.Ease.Utils.Servlets.PostServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/api/v1/common/AskEditEmail")
public class ServletAskEditEmail extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeConnected();
            String password = sm.getStringParam("password", false, false);
            String private_key = (String) sm.getContextAttr("privateKey");
            password = RSA.Decrypt(password, private_key);
            User user = sm.getUser();
            if (!user.getUserKeys().isGoodPassword(password))
                throw new HttpServletException(HttpStatus.BadRequest, "Wrong password");
            String new_email = sm.getStringParam("new_email", true, false).toLowerCase();
            if (!Regex.isEmail(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (new_email.length() >= 100)
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (user.getEmail().equals(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already your reference email.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE email = :email");
            hibernateQuery.setParameter("email", new_email);
            if (hibernateQuery.getSingleResult() != null)
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already used for another Ease.space account.");
            String edit_email_code = CodeGenerator.generateDigits(6);
            user.getUserStatus().setEdit_email_code(edit_email_code);
            user.getUserStatus().setEmail_requested(new_email);
            sm.saveOrUpdate(user.getUserStatus());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setFrom("contact@ease.space", "Agathe @Ease");
            mailJetBuilder.addVariable("first_digits", edit_email_code.substring(0, 3));
            mailJetBuilder.addVariable("last_digits", edit_email_code.substring(3));
            mailJetBuilder.setTemplateId(244982);
            mailJetBuilder.addTo(new_email);
            mailJetBuilder.sendEmail();
            sm.setSuccess("Email sent");
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
