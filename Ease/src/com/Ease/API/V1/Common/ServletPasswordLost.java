package com.Ease.API.V1.Common;

import com.Ease.Context.Variables;
import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailJetBuilder;
import com.Ease.User.PasswordLost;
import com.Ease.User.User;
import com.Ease.Utils.Crypto.CodeGenerator;
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

@WebServlet("/api/v1/common/PasswordLost")
public class ServletPasswordLost extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PostServletManager sm = new PostServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getStringParam("email", true, false);
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", email);
            User user = (User) hibernateQuery.getSingleResult();
            if (user == null)
                throw new HttpServletException(HttpStatus.BadRequest, "No user with this email.");
            String code = CodeGenerator.generateNewCode();
            hibernateQuery.queryString("SELECT p FROM PasswordLost p WHERE p.user_id = :id");
            hibernateQuery.setParameter("id", user.getDb_id());
            PasswordLost passwordLost = (PasswordLost) hibernateQuery.getSingleResult();
            if (passwordLost == null)
                passwordLost = new PasswordLost(code, user.getDb_id());
            else
                passwordLost.setCode(code);
            passwordLost.setRequest_date(new Date());
            MailJetBuilder mailJetBuilder = new MailJetBuilder();
            mailJetBuilder.setTemplateId(178530);
            mailJetBuilder.setFrom("contact@ease.space", "Ease.space");
            mailJetBuilder.addTo(email);
            mailJetBuilder.addVariable("link", Variables.URL_PATH + "resetPassword?email=" + email + "&code=" + code);
            mailJetBuilder.sendEmail();
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
