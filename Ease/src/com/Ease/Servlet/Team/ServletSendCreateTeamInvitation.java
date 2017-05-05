package com.Ease.Servlet.Team;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.Crypto.CodeGenerator;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by thomas on 03/05/2017.
 */
@WebServlet("/ServletSendCreateTeamInvitation")
public class ServletSendCreateTeamInvitation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
        try {
            String email = sm.getServletParam("email", true);
            String firstName = sm.getServletParam("firstName", true);
            if (email == null || email.equals("") || !Regex.isEmail(email))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Invalid email field.");
            if (firstName == null || firstName.equals(""))
                throw new GeneralException(ServletManager.Code.ClientWarning, "Empty first name.");
            String code;
            HibernateQuery query = new HibernateQuery();
            query.querySQLString("SELECT code FROM createTeamInvitations WHERE email = ?");
            query.setParameter(1, email);
            Object id = query.getSingleResult();
            if (id == null) {
                code = CodeGenerator.generateNewCode();
                query.querySQLString("INSERT INTO createTeamInvitations values(NULL, ?, ?);");
                query.setParameter(1, email);
                query.setParameter(2, code);
                query.executeUpdate();
            } else
                code = (String) id;
            query.commit();
            SendGridMail sendGridMail = new SendGridMail("Thomas @EaseSpace", "thomas@ease.space");
            sendGridMail.sendCreateTeamEmail(firstName, email, code);
            sm.setResponse(ServletManager.Code.Success, "Invitation to create a team sent");
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
