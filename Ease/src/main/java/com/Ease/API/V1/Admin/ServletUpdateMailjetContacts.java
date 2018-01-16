package com.Ease.API.V1.Admin;

import com.Ease.Hibernate.HibernateQuery;
import com.Ease.Mail.MailjetContactWrapper;
import com.Ease.User.User;
import com.Ease.Utils.Servlets.GetServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/admin/ServletUpdateMailjetContacts")
public class ServletUpdateMailjetContacts extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GetServletManager sm = new GetServletManager(this.getClass().getName(), request, response, true);
        try {
            sm.needToBeEaseAdmin();
            HibernateQuery hibernateQuery = sm.getHibernateQuery();
            hibernateQuery.queryString("SELECT u FROM User u");
            List<User> users = hibernateQuery.list();
            for (User user : users) {
                MailjetContactWrapper mailjetContactWrapper = new MailjetContactWrapper();
                mailjetContactWrapper.updateUserData(user);
                mailjetContactWrapper.updateUserContactLists(user);
            }
            sm.setSuccess("Done");
        } catch (Exception e) {
            sm.setError(e);
        }
        sm.sendResponse();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
        rd.forward(request, response);
    }
}
