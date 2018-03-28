package com.Ease.API.V1.Common;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.User.User;
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

@WebServlet("/api/v1/common/EditEmail")
public class ServletEditEmail extends HttpServlet {
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
            String digits = sm.getStringParam("digits", true, false);
            if (!Regex.isEmail(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (new_email.length() >= 100)
                throw new HttpServletException(HttpStatus.BadRequest, "Please, provide a valid email.");
            if (user.getEmail().equals(new_email))
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already your reference email.");
            if (!new_email.equals(user.getUserStatus().getEmailRequested()))
                throw new HttpServletException(HttpStatus.BadRequest, "This is not the email you requested.");
            if (!digits.equals(user.getUserStatus().getEditEmailCode()))
                throw new HttpServletException(HttpStatus.BadRequest, "This code isn't valid.");
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u WHERE u.email = :email");
            hibernateQuery.setParameter("email", new_email);
            if (hibernateQuery.getSingleResult() != null)
                throw new HttpServletException(HttpStatus.BadRequest, "This email is already used for another Ease.space account.");
            String old_email = user.getEmail();
            user.setEmail(new_email);
            user.getUserStatus().setEmailRequested(null);
            user.getUserStatus().setEditEmailCode(null);
            sm.saveOrUpdate(user);
            sm.getUser().getCookies().forEach(response::addCookie);
            MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
            mailjetContactWrapper.updateUserEmail(old_email, user);
            sm.setSuccess("Email edited");
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
